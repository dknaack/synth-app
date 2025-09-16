package com.dknaack.synth

data class SynthState(
    val isPlaying: Boolean = false,
    val isRecording: Boolean = false,
    val isMicEnabled: Boolean = false,
)
