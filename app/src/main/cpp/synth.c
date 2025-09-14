#include <jni.h>
#include <math.h>
#include <aaudio/AAudio.h>

static AAudioStream *output_stream = NULL;
static double phase = 0.0;
static int32_t sample_rate;
static int32_t frames_per_burst;

static aaudio_data_callback_result_t
play_callback(AAudioStream *stream, void *user_data, void *audio_data, int32_t num_frames)
{
    float *output = audio_data;
    double freq = 440.0; // A4
    double phase_increment = (2.0 * M_PI * freq) / sample_rate;

    for (int i = 0; i < num_frames; i++) {
        output[i] = (float)sin(phase);
        phase += phase_increment;
        if (phase >= 2.0 * M_PI) {
            phase -= 2.0 * M_PI;
        }
    }

    return AAUDIO_CALLBACK_RESULT_CONTINUE;
}

static aaudio_data_callback_result_t
record_callback(AAudioStream *stream, void *user_data, void *audio_data, int32_t num_frames)
{
    return AAUDIO_CALLBACK_RESULT_CONTINUE;
}

JNIEXPORT void JNICALL
Java_com_dknaack_synth_MainActivity_setDefaultStreamValues(
        JNIEnv *env, jobject type, jint sampleRate, jint framesPerBurst)
{
    sample_rate = (int32_t)sampleRate;
    frames_per_burst = (int32_t)framesPerBurst;
}

JNIEXPORT void JNICALL
Java_com_dknaack_synth_MainActivity_playSound(JNIEnv *env, jobject thiz) {
    AAudioStreamBuilder *builder;
    AAudio_createStreamBuilder(&builder);
    AAudioStreamBuilder_setPerformanceMode(builder, AAUDIO_PERFORMANCE_MODE_LOW_LATENCY);
    AAudioStreamBuilder_setSharingMode(builder, AAUDIO_SHARING_MODE_EXCLUSIVE);
    AAudioStreamBuilder_setFormat(builder, AAUDIO_FORMAT_PCM_FLOAT);
    AAudioStreamBuilder_setChannelCount(builder, AAUDIO_CHANNEL_MONO);
    AAudioStreamBuilder_setDataCallback(builder, play_callback, NULL);

    AAudioStreamBuilder_openStream(builder, &output_stream);
    if (output_stream) {
        AAudioStream_requestStart(output_stream);
    }
}

JNIEXPORT void JNICALL
Java_com_dknaack_synth_MainActivity_stopSound(JNIEnv *env, jobject thiz) {
    if (output_stream) {
        AAudioStream_requestStop(output_stream);
        AAudioStream_close(output_stream);
        output_stream = NULL;
    }
}

JNIEXPORT void JNICALL
Java_com_dknaack_synth_MainActivity_startRecording(JNIEnv *env, jobject thiz) {

}

JNIEXPORT void JNICALL
Java_com_dknaack_synth_MainActivity_stopRecording(JNIEnv *env, jobject thiz) {

}