package com.example.rtt

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.MutableLiveData
import com.example.rtt.ui.theme.RttTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import libs.KeepScreenOn
import libs.ipToBroadCast
import libs.readIP
import kotlin.system.*


var telnetSlegenie = MutableLiveData<Boolean>(true)
var telnetWarning = MutableLiveData<Boolean>(false) //Для отображения значка
var lastCount = 0 //Запоминаем последнне значение при отколючении слежения

var slegenie: Boolean = true
var ipBroadcast = "0.0.0.0"
var contex: Context? = null

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contex = applicationContext

        //Создаем список цветов из Json цветов
        colorJsonToList()

        //Запуск получения пакетов
        val threadWithRunnable = Thread(udp_DataArrival())
        threadWithRunnable.start()

        LineAdd("RTT Client v8")

        setContent {
            ipBroadcast = ipToBroadCast(readIP(applicationContext))
            KeepScreenOn()
            RttTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = Color(0xFF090909) //MaterialTheme.colors.background
                ) {
                    val pagerState = rememberPagerState()
                    LaunchedEffect(pagerState) {
                        snapshotFlow { pagerState.currentPage }.collect { page ->
                            if (page == 1) {
                                slegenie = telnetSlegenie.value!!
                                telnetSlegenie.value = false
                            }
                            if (page == 0) {
                                telnetSlegenie.value = slegenie
                            }
                        }
                    }
                    HorizontalPager(count = 3, state = pagerState) { page ->
                        when (page) {
                            0 -> lazy(colorline)
                            1 -> info()
                        }
                    }
                }
            }
        }
    }
}

fun LineAdd(value: String) {
    colorline.add(
        listOf(
            pairTextAndColor(
                text = value, Color.Green, Color.Black
            )
        )
    )
}
