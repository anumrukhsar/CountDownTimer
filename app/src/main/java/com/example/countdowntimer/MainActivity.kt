/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.theme.MyTheme
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp() {
    var progress = remember { mutableStateOf((5 * 60 * 1000).toFloat()) }
    var timerVal = remember { mutableStateOf("05:00") }
    var isFinish = remember { mutableStateOf(true) }

    Surface(color = MaterialTheme.colors.background) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val animatedProgress = animateFloatAsState(
                targetValue = progress.value,
                animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
            ).value
            CountDownTimerComp(animatedProgress, progress, timerVal, isFinish)

        }

    }
}

@Composable
fun CountDownTimerComp(
    animatedProgress: Float,
    progress: MutableState<Float>,
    timerVal: MutableState<String>,
    isFinish: MutableState<Boolean>
) {

    Box(modifier = Modifier.fillMaxSize()) {

        CircularProgressIndicator(
           /* progress = animatedProgress ,*/modifier = Modifier
                .size(100.dp)
                .align(Alignment.Center)
        )
        Text(text = timerVal.value, Modifier.align(Alignment.Center))
        Button(
            onClick = {if (isFinish.value) startTimer(progress, timerVal, isFinish) else stopTimer(progress,timerVal,isFinish)},
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            if (isFinish.value)
                Text(text = stringResource(R.string.start))
            else
                Text(text = stringResource(R.string.stop))
        }
    }

}

fun stopTimer(
    progress: MutableState<Float>,
    timerVal: MutableState<String>,
    finish: MutableState<Boolean>
) {

}

fun startTimer(
    progress: MutableState<Float>,
    timerVal: MutableState<String>,
    isFinish: MutableState<Boolean>
) {

     val timer = object : CountDownTimer(progress.value.toLong(), 1000) {
        override fun onTick(millisUntilFinished: Long) {
            var sec = millisUntilFinished / 1000;
            progress.value = sec.toFloat()
            timerVal.value = "" + String.format(
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                ),
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                )
            )
            isFinish.value = false
        }

        override fun onFinish() {
            progress.value = (5 * 60 * 1000).toFloat()
            timerVal.value = "05:00"
            isFinish.value= true
        }
    }
    timer.start()

}


@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}
