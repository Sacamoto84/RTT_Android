package com.example.rtt

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import com.example.rtt.ui.theme.RttTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import libs.KeepScreenOn
import libs.ipToBroadCast
import libs.readIP


var telnetSlegenie = MutableLiveData<Boolean>(true)
var telnetWarning = MutableLiveData<Boolean>(false) //Для отображения значка
var lastCount = 0 //Запоминаем последнне значение при отколючении слежения

var slegenie: Boolean = true
var ipBroadcast = "0.0.0.0"
var contex: Context? = null

var console_text = 12.sp

lateinit var shared : SharedPreferences

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contex = applicationContext

        shared = getSharedPreferences("size" , Context.MODE_PRIVATE)
        console_text = shared.getString("size" , "12" )?.toInt()?.sp ?: 12.sp

        //Создаем список цветов из Json цветов
        colorJsonToList()

        //Запуск получения пакетов
        //val threadWithRunnable = Thread(udp_DataArrival())
        //threadWithRunnable.start()

        val vm: VM by viewModels();
        vm.launchUDPRecive()

        LineAdd("RTT Client v12")

        setContent {
            ipBroadcast = ipToBroadCast(readIP(applicationContext))
            KeepScreenOn()
            vm.launchUIChanelRecive()
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
                    HorizontalPager(count = 2, state = pagerState) { page ->
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
