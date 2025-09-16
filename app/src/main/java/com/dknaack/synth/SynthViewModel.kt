package com.dknaack.synth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SynthViewModel: ViewModel() {
    private val _state = MutableStateFlow(SynthState())
    val state = _state.asStateFlow()

    fun onEvent(event: SynthEvent) {
        when (event) {
            SynthEvent.PlayPause -> {
                _state.update { it.copy(isPlaying = !it.isPlaying) }
            }
            SynthEvent.Record -> {
                _state.update { it.copy(isRecording = true) }
            }
            SynthEvent.Stop -> {
                if (state.value.isPlaying) {
                    _state.update { it.copy(isPlaying = false) }
                } else if (state.value.isRecording) {
                    _state.update { it.copy(isRecording = false) }
                }
            }
            SynthEvent.ToggleMic -> {
                _state.update { it.copy(isMicEnabled = !it.isMicEnabled) }
            }
        }
    }
}