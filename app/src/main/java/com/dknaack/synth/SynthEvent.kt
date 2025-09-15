package com.dknaack.synth

sealed interface SynthEvent {
    data object PlayPause: SynthEvent
    data object Stop: SynthEvent
    data object Record: SynthEvent
}