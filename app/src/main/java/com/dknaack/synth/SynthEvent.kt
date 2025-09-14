package com.dknaack.synth

sealed interface SynthEvent {
    data object Play: SynthEvent
    data object Stop: SynthEvent
}