#include <jni.h>
#include <oboe/Oboe.h>

// Example: simple sine wave player
struct MyAudioEngine : public oboe::AudioStreamCallback {
    oboe::AudioStream *stream = nullptr;
    double phase = 0.0;

    oboe::DataCallbackResult
    onAudioReady(oboe::AudioStream *oboeStream, void *audioData, int32_t numFrames) override {
        float *output = static_cast<float *>(audioData);
        double freq = 440.0; // A4
        double phaseIncrement = (2.0 * M_PI * freq) / oboeStream->getSampleRate();

        for (int i = 0; i < numFrames; i++) {
            output[i] = static_cast<float>(sin(phase));
            phase += phaseIncrement;
            if (phase >= 2.0 * M_PI) phase -= 2.0 * M_PI;
        }
        return oboe::DataCallbackResult::Continue;
    }

    void start() {
        oboe::AudioStreamBuilder builder;
        builder.setPerformanceMode(oboe::PerformanceMode::LowLatency)
                ->setSharingMode(oboe::SharingMode::Exclusive)
                ->setFormat(oboe::AudioFormat::Float)
                ->setChannelCount(oboe::ChannelCount::Mono)
                ->setCallback(this);

        builder.openStream(&stream);
        if (stream) stream->requestStart();
    }

    void stop() {
        if (stream) {
            stream->requestStop();
            stream->close();
            stream = nullptr;
        }
    }
};

static MyAudioEngine engine;

extern "C" {

JNIEXPORT void JNICALL
Java_com_dknaack_synth_MainActivity_setDefaultStreamValues(
        JNIEnv *env, jobject type, jint sampleRate, jint framesPerBurst) {
    oboe::DefaultStreamValues::SampleRate = (int32_t) sampleRate;
    oboe::DefaultStreamValues::FramesPerBurst = (int32_t) framesPerBurst;
}

JNIEXPORT void JNICALL
Java_com_dknaack_synth_MainActivity_playSound(JNIEnv *env, jobject thiz) {
    engine.start();
}

JNIEXPORT void JNICALL
Java_com_dknaack_synth_MainActivity_stopSound(JNIEnv *env, jobject thiz) {
    engine.stop();
}

}