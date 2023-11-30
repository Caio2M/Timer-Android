package com.example.timer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timer.ui.theme.TimerTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimerTheme {
                    App()
            }
        }
    }
}

@Composable
fun App() {
    var hour by remember { mutableStateOf(0) }
    var minute by remember { mutableStateOf(0) }
    var second by remember { mutableStateOf(0) }
    var mili by remember { mutableStateOf(0) }
    var isPaused by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = isPaused, key2 = mili, block = {
        if (!isPaused) {
            delay(0)
            if (mili == 99) {
                mili = 0
                second += 1
            }
            if (second == 59) {
                second = 0
                minute += 1
            }
            if (minute == 59) {
                minute = 0
                hour += 1
            }
            mili += 1
        }
    })

    fun onStop () {
        mili = 0; second = 0; minute = 0; hour = 0
        isPaused = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.align(Alignment.Center)){
            Text(
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                text =
                    "${if ("$hour".length <= 1) "0$hour" else hour}:" +
                    "${if ("$minute".length <= 1) "0$minute" else minute}:" +
                    "${if ("$second".length <= 1) "0$second" else second}:" +
                    "${if ("$mili".length <= 1) "0$mili" else mili}"
            )
        }
        Row(modifier = Modifier
            .align(Alignment.Center)
            .offset(y = (100).dp),
        ) {
            Button(
                onClick = { isPaused = !isPaused }
            ) {
                Text(text = "${if (isPaused) "Play" else "Pause"}")
            }
            Box(modifier = Modifier.width(20.dp))
            Button(
                onClick = { onStop() }
            ) {
                Text(text = "Stop")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TimerTheme {
        App()
    }
}