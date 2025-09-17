#include <jni.h>
#include <math.h>
#include <aaudio/AAudio.h>
#include <string.h>
#include <stdlib.h>

typedef struct {
    AAudioStream *output_stream;
    int32_t sample_rate;
    int32_t frames_per_burst;
    double phase;

    AAudioStream *input_stream;
    int16_t *input_buffer;
    int32_t max_input_size;
    int32_t input_size;
} AudioEngine;

static aaudio_data_callback_result_t
output_callback(AAudioStream *stream, void *user_data, void *audio_data, int32_t num_frames)
{
    AudioEngine *engine = user_data;
    int32_t sample_rate = AAudioStream_getSampleRate(stream);
    int32_t num_channels = AAudioStream_getChannelCount(stream);

    int16_t *output = audio_data;
    double freq = 440.0; // A4
    double phase_increment = (2.0 * M_PI * freq) / sample_rate;
    double phase = engine->phase;

    for (int i = 0; i < num_frames; i++) {
        int16_t sample = (int16_t)(32767.0 * sin(phase));
        for (int c = 0; c < num_channels; c++){
            *output++ = sample;
        }

        phase += phase_increment;
        if (phase >= 2.0 * M_PI) {
            phase -= 2.0 * M_PI;
        }
    }

    engine->phase = phase;
    return AAUDIO_CALLBACK_RESULT_CONTINUE;
}

static aaudio_data_callback_result_t
input_callback(AAudioStream *stream, void *user_data, void *audio_data, int32_t num_frames)
{
    AudioEngine *engine = user_data;

    int32_t num_channels = AAudioStream_getChannelCount(stream);
    int32_t num_samples = num_frames * num_channels;

    while (engine->input_size + num_samples >= engine->max_input_size) {
        if (engine->max_input_size == 0) {
            engine->max_input_size = engine->sample_rate * 2;
        } else {
            engine->max_input_size *= 2;
        }

        size_t byte_count = engine->max_input_size * sizeof(*engine->input_buffer);
        engine->input_buffer = realloc(engine->input_buffer, byte_count);
    }

    int16_t *src = audio_data;
    int16_t *dst = engine->input_buffer;
    for (int32_t i = 0; i < num_samples; i++) {
        dst[i] = src[i];
    }

    engine->input_size += num_samples;
    return AAUDIO_CALLBACK_RESULT_CONTINUE;
}

JNIEXPORT jlong JNICALL
Java_com_dknaack_synth_AudioEngine_startEngine(
        JNIEnv *env, jobject thiz, jint sample_rate, jint frames_per_burst)
{
    AudioEngine *engine = calloc(1, sizeof(*engine));
    if (!engine) {
        return 0;
    }

    engine->sample_rate = (int32_t)sample_rate;
    engine->frames_per_burst = (int32_t)frames_per_burst;

    // Initialize an output stream
    {
        AAudioStreamBuilder *builder = NULL;
        AAudio_createStreamBuilder(&builder);
        AAudioStreamBuilder_setPerformanceMode(builder, AAUDIO_PERFORMANCE_MODE_LOW_LATENCY);
        AAudioStreamBuilder_setSharingMode(builder, AAUDIO_SHARING_MODE_EXCLUSIVE);
        AAudioStreamBuilder_setFormat(builder, AAUDIO_FORMAT_PCM_I16);
        AAudioStreamBuilder_setChannelCount(builder, 1);
        AAudioStreamBuilder_setSampleRate(builder, engine->sample_rate);
        AAudioStreamBuilder_setDataCallback(builder, output_callback, engine);

        AAudioStreamBuilder_openStream(builder, &engine->output_stream);
        AAudioStreamBuilder_delete(builder);
    }

    // Initialize an input stream
    {
        AAudioStreamBuilder *builder = NULL;
        AAudio_createStreamBuilder(&builder);
        AAudioStreamBuilder_setDirection(builder, AAUDIO_DIRECTION_INPUT);
        AAudioStreamBuilder_setSampleRate(builder, engine->sample_rate);
        AAudioStreamBuilder_setChannelCount(builder, 1);
        AAudioStreamBuilder_setFormat(builder, AAUDIO_FORMAT_PCM_I16);
        AAudioStreamBuilder_setDataCallback(builder, input_callback, engine);

        AAudioStreamBuilder_openStream(builder, &engine->input_stream);
        AAudioStreamBuilder_delete(builder);
    }

    return (jlong)engine;
}

JNIEXPORT void JNICALL
Java_com_dknaack_synth_AudioEngine_stopEngine(JNIEnv *env, jobject thiz, jlong ptr)
{
    AudioEngine *engine = (AudioEngine *)ptr;
    if (engine) {
        AAudioStream_requestStop(engine->output_stream);
        AAudioStream_close(engine->output_stream);
        AAudioStream_close(engine->input_stream);
        free(engine);
    }
}

JNIEXPORT void JNICALL
Java_com_dknaack_synth_AudioEngine_startPlaybackNative(JNIEnv *env, jobject thiz, jlong ptr)
{
    AudioEngine *engine = (AudioEngine *)ptr;
    if (engine) {
        AAudioStream_requestStart(engine->output_stream);
    }
}

JNIEXPORT void JNICALL
Java_com_dknaack_synth_AudioEngine_stopPlaybackNative(JNIEnv *env, jobject thiz, jlong ptr)
{
    AudioEngine *engine = (AudioEngine *)ptr;
    if (engine) {
        AAudioStream_requestStop(engine->output_stream);
    }
}

JNIEXPORT void JNICALL
Java_com_dknaack_synth_AudioEngine_startRecordingNative(JNIEnv *env, jobject thiz, jlong ptr)
{
    AudioEngine *engine = (AudioEngine *)ptr;
    if (engine) {
        AAudioStream_requestStart(engine->input_stream);
    }
}

JNIEXPORT void JNICALL
Java_com_dknaack_synth_AudioEngine_stopRecordingNative(JNIEnv *env, jobject thiz, jlong ptr)
{
    AudioEngine *engine = (AudioEngine *)ptr;
    if (engine) {
        AAudioStream_requestStop(engine->input_stream);
    }
}