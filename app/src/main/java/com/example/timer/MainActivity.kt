package com.example.timer

import android.graphics.drawable.Icon
import android.media.Image
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timer.ui.theme.TimerTheme
import kotlinx.coroutines.delay
import java.util.UUID

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

data class ListValue(val id: String, val text: String)
@Composable
fun App() {
    var hour by remember { mutableStateOf(0) }
    var minute by remember { mutableStateOf(0) }
    var second by remember { mutableStateOf(0) }
    var mili by remember { mutableStateOf(0) }
    var isPaused by remember { mutableStateOf(true) }

    var listSavedTimes by remember { mutableStateOf(emptyList<ListValue>()) }

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
        listSavedTimes = listSavedTimes.filter { it.text == "" }
    }

    fun onMark (text: String) {
        listSavedTimes += ListValue(id = "${UUID.randomUUID()}", text)
    }

    fun formatNumber (num: Int): String {
        if ("$num".length <= 1) {
            return "0$num"
        }
        return "$num"
    }

    fun formatTime (hour: Int, min: Int, sec: Int, mil: Int): String {
        return "${formatNumber(hour)}:${formatNumber(min)}:${formatNumber(sec)}:${formatNumber(mil)}"
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp)) {
        Box(modifier = Modifier
            .align(Alignment.TopCenter)
            .offset(y = (100).dp)){
            Text(
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                text = formatTime(hour, minute, second, mili)
            )
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.TopCenter)
            .offset(y = (200).dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { onStop() }
            ) {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.stop), contentDescription = null)
            }
            Button(
                onClick = { isPaused = !isPaused }
            ) {
                if (isPaused) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                } else {
                    Icon(imageVector =  ImageVector.vectorResource(id = R.drawable.pause), contentDescription = null)
                }
            }
            Button(
                onClick = {
                    if (formatTime(hour, minute, second, mili).replace(":" ,"").toInt() > 0) {
                        onMark(formatTime(hour, minute, second, mili))
                    }
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }

        Box (modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f)
            .align(Alignment.BottomCenter)
        ) {
            LazyColumn(
                content = {
                    items(listSavedTimes) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp, 5.dp)
                        ){
                            Text(text = it.text, fontSize = 20.sp)
                            Text(
                                text = formatNumber(listSavedTimes.indexOf(it) + 1),
                                fontSize = 20.sp
                            )
                        }
                    }
                })
        }
        if (listSavedTimes.isEmpty()) {
            Text(color = Color.DarkGray, fontSize = 20.sp, text = "No time saved...", modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 100.dp))
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