package com.dknaack.synth

import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.media.AudioManager

class AudioEngine(
    private val context: Context,
) {
    companion object {
        init {
            System.loadLibrary("synth")
        }
    }

    private var handle: Long = 0

    fun start() {
        val audioManager = context.getSystemService(AUDIO_SERVICE) as AudioManager
        val sampleRateString = audioManager.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE)
        val defaultSampleRate = sampleRateString.toInt()
        val framesPerBurstString = audioManager.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER)
        val defaultFramesPerBurst = framesPerBurstString.toInt()

        handle = startEngine(defaultSampleRate, defaultFramesPerBurst)
    }

    fun stop() { stopEngine(handle) }

    fun startPlayback() {
        println(">>> Starting $handle")
        startPlaybackNative(handle)
    }

    fun stopPlayback() {
        println(">>> Stopping $handle")
        stopPlaybackNative(handle)
    }

    fun startRecording() { startRecordingNative(handle) }
    fun stopRecording() { stopRecordingNative(handle) }

    private external fun startEngine(sampleRate: Int, framesPerBurst: Int): Long
    private external fun stopEngine(handle: Long)
    private external fun startPlaybackNative(handle: Long)
    private external fun stopPlaybackNative(handle: Long)
    private external fun startRecordingNative(handle: Long)
    private external fun stopRecordingNative(handle: Long)
}