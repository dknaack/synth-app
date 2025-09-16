package com.dknaack.synth

import android.media.AudioManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Piano
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ScreenRotationAlt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Square
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dknaack.synth.ui.theme.SynthTheme
import kotlin.getValue


class MainActivity : ComponentActivity() {
    companion object {
        init {
            System.loadLibrary("synth")
        }
    }


    private val synthViewModel: SynthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            SynthTheme {
                MainScreen(
                    synthViewModel::onEvent,
                    synthViewModel.state.collectAsState().value,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onEvent: (SynthEvent) -> Unit,
    state: SynthState,
) {
    Scaffold { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
        ) {
            MainDisplay(onEvent, state)
            SecondaryButtonGrid(onEvent, state)
            PrimaryButtonRow(onEvent, state)
            KeyboardButtons(onEvent, state, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun MainDisplay(
    onEvent: (SynthEvent) -> Unit,
    state: SynthState,
) {
    OutlinedCard(
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 2.dp),
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
            ) {
                Box(modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .fillMaxSize())
            }

            Row(
                modifier = Modifier.padding(horizontal = 6.dp)
            ) {
                FilledTonalButton(
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = Color.Red,
                        contentColor = Color.Black.copy(alpha = 0.5f).compositeOver(Color.Red),
                    ),
                    onClick = { },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Circle,
                        contentDescription = "Record",
                        modifier = Modifier.size(16.dp),
                    )
                }
                IconButton(
                    modifier = Modifier.weight(1f),
                    onClick = { },
                ) {
                    Icon(
                        imageVector = Icons.Default.FastRewind,
                        contentDescription = "Fast Rewind",
                    )
                }
                IconButton(
                    modifier = Modifier.weight(1f),
                    onClick = { onEvent(SynthEvent.PlayPause) },
                ) {
                    Icon(
                        imageVector = if (state.isPlaying || state.isRecording) {
                            Icons.Default.Pause
                        } else {
                            Icons.Default.PlayArrow
                        },
                        contentDescription = "Play",
                    )
                }
                IconButton(
                    modifier = Modifier.weight(1f),
                    onClick = { },
                ) {
                    Icon(
                        imageVector = Icons.Filled.FastForward,
                        contentDescription = "Fast Forward",
                    )
                }
                IconButton(
                    modifier = Modifier.weight(1f),
                    onClick = { onEvent(SynthEvent.Stop) },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Stop,
                        contentDescription = "Stop",
                    )
                }
            }
        }
    }
}

@Composable
fun PrimaryButtonRow(
    onEvent: (SynthEvent) -> Unit,
    state: SynthState,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { PrimaryButton(
            onClick = { },
            imageVector = Icons.Default.Star,
            contentDescription = "Button",
            color = MaterialTheme.colorScheme.primaryContainer,
        ) }
        item { PrimaryButton(
            onClick = { },
            imageVector = Icons.Default.Favorite,
            contentDescription = "Button",
            color = MaterialTheme.colorScheme.errorContainer,
        ) }
        item { PrimaryButton(
            onClick = { },
            imageVector = Icons.Filled.PlayArrow,
            contentDescription = "Button",
            color = MaterialTheme.colorScheme.tertiaryContainer,
        ) }
        item { PrimaryButton(
            onClick = { },
            imageVector = Icons.Filled.Square,
            contentDescription = "Button",
            color = MaterialTheme.colorScheme.secondaryContainer,
        ) }
    }
}

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String,
    color: Color,
) {
    var rotation by remember { mutableFloatStateOf(0f) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(color)
            .scrollable(
                orientation = Orientation.Vertical,
                state = rememberScrollableState { delta ->
                    rotation += delta
                    delta
                },
            )
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = Modifier
                .height(32.dp)
                .graphicsLayer {
                    rotationZ = rotation
                },
        )
    }
}

@Composable
fun SecondaryButtonGrid(
    onEvent: (SynthEvent) -> Unit,
    state: SynthState,
) {
    val icons = listOf(
        1,
        2,
        3,
        4,
        5,
        Icons.Default.Refresh,
        Icons.Default.Piano,
        Icons.Default.Edit,
        Icons.Default.Equalizer,
        Icons.Default.Settings,
        Icons.AutoMirrored.Default.ArrowBack,
        Icons.Default.MusicNote,
        Icons.Default.ScreenRotationAlt,
        if (state.isMicEnabled)
            Icons.Default.Mic
        else
            Icons.Default.MicOff,
        Icons.AutoMirrored.Default.ArrowForward,
    )

    val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs,
    )

    val fontName = GoogleFont("Courier Prime")
    val fontFamily = FontFamily(
        Font(fontName, provider)
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(icons.withIndex().toList()) { (index, icon) ->
            val outerRadius = 8.dp
            val innerRadius = 2.dp
            val shape = when (index) {
                0 -> RoundedCornerShape(topStart = outerRadius)
                4 -> RoundedCornerShape(topEnd = outerRadius)
                14 -> RoundedCornerShape(bottomEnd = outerRadius)
                10 -> RoundedCornerShape(bottomStart = outerRadius)
                else -> RoundedCornerShape(innerRadius)
            }

            SecondaryButton(shape) {
                if (icon is ImageVector) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "Button",
                    )
                } else if (icon is Int) {
                    Text(
                        text = "${index+1}",
                        fontFamily = fontFamily,
                        fontSize = 32.sp,
                    )
                }
            }
        }
    }
}

@Composable
fun SecondaryButton(
    shape: Shape,
    content: @Composable () -> Unit,
) {
    FilledTonalButton(
        onClick = { },
        shape = shape,
        modifier = Modifier.aspectRatio(1f),
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {
        content()
    }
}

@Composable
fun KeyboardButtons(
    onEvent: (SynthEvent) -> Unit,
    state: SynthState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            BlackKeyboardButton { }
            BlackKeyboardButton { }
            BlackKeyboardButton { }
        }

        Row(
            modifier = Modifier.weight(2f),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            WhiteKeyboardButton { }
            WhiteKeyboardButton { }
            WhiteKeyboardButton { }
            WhiteKeyboardButton { }
        }
    }
}

@Composable
fun BlackKeyboardButton(
    onClick: () -> Unit,
) {
    Button(
        onClick,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
        modifier = Modifier
            .fillMaxHeight()
            .width(80.dp),
    ) { }
}

@Composable
fun WhiteKeyboardButton(
    onClick: () -> Unit,
) {
    Button(
        onClick,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier
            .fillMaxHeight()
            .width(80.dp),
    ) { }
}

@Preview(showSystemUi = true)
@Composable
fun MainScreenPreview() {
    SynthTheme {
        MainScreen(
            onEvent = { },
            state = SynthState()
        )
    }
}