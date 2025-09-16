package com.dknaack.synth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SynthViewModel(
    private val audioEngine: AudioEngine,
): ViewModel() {
    private val _state = MutableStateFlow(SynthState())
    val state = _state.asStateFlow()

    fun onEvent(event: SynthEvent) {
        when (event) {
            SynthEvent.PlayPause -> {
                _state.update {
                    if (it.isPlaying) {
                        audioEngine.stopPlayback()
                        it.copy(isPlaying = false)
                    } else if (it.isRecording) {
                        audioEngine.stopRecording()
                        it.copy(isRecording = false)
                    } else {
                        audioEngine.startPlayback()
                        it.copy(isPlaying = true)
                    }
                }
            }
            SynthEvent.Record -> {
                if (!state.value.isPlaying) {
                    _state.update { it.copy(isRecording = true) }
                    audioEngine.startRecording()
                }
            }
            SynthEvent.Stop -> {
                _state.update { it.copy(isPlaying = false, isRecording = false) }
                audioEngine.stopPlayback()
                audioEngine.stopRecording()
            }
            SynthEvent.ToggleMic -> {
                _state.update { it.copy(isMicEnabled = !it.isMicEnabled) }
            }
        }
    }
}