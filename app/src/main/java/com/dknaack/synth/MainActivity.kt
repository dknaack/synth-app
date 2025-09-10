package com.dknaack.synth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.OpenWith
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Square
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dknaack.synth.ui.theme.SynthTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SynthTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Scaffold { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
        ) {
            Screen()
            SecondaryButtonGrid()
            PrimaryButtonRow()
            KeyboardButtons(Modifier.weight(1f))
        }
    }
}

@Composable
fun Screen() {
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
                        contentDescription = "Play",
                    )
                }
                IconButton(
                    modifier = Modifier.weight(1f),
                    onClick = { },
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                    )
                }
                IconButton(
                    modifier = Modifier.weight(1f),
                    onClick = { },
                ) {
                    Icon(
                        imageVector = Icons.Filled.FastForward,
                        contentDescription = "Stop",
                    )
                }
                IconButton(
                    modifier = Modifier.weight(1f),
                    onClick = { },
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
fun PrimaryButtonRow() {
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
            imageVector = Icons.Filled.Circle,
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
    FilledIconButton(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.aspectRatio(1f),
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = color,
        )
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
        )
    }
}

@Composable
fun SecondaryButtonGrid() {
    val icons = listOf(
        1,
        2,
        3,
        4,
        5,
        Icons.Default.AcUnit,
        Icons.Default.Notifications,
        Icons.Default.Lock,
        Icons.Default.Info,
        Icons.Default.Settings,
        Icons.Default.WatchLater,
        Icons.Default.MusicOff,
        Icons.Default.MusicNote,
        Icons.Default.Mic,
        Icons.Default.OpenWith,
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(icons.withIndex().toList()) { (index, icon) ->
            val shape = when (index) {
                0 -> RoundedCornerShape(topStart = 8.dp)
                4 -> RoundedCornerShape(topEnd = 8.dp)
                14 -> RoundedCornerShape(bottomEnd = 8.dp)
                10 -> RoundedCornerShape(bottomStart = 8.dp)
                else -> RoundedCornerShape(2.dp)
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
                        fontFamily = FontFamily.Monospace,
                        fontSize = 30.sp,
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
        MainScreen()
    }
}