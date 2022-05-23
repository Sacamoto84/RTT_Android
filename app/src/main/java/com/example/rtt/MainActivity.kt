package com.example.rtt

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.MutableLiveData
import com.example.rtt.ui.theme.RttTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import libs.ipToBroadCast
import libs.readIP

var telnetSlegenie = MutableLiveData<Boolean>(true)
var telnetWarning = MutableLiveData<Boolean>(false) //Для отображения значка
var lastCount = 0 //Запоминаем последнне значение при отколючении слежения

var slegenie : Boolean = true

var ipBroadcast = "0.0.0.0"

var contex : Context? = null

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

        colorline.add(
            listOf(
                pairTextAndColor(
                    text = "RTT V5 23.05.2022",
                    Color.Green,
                    Color.Black
                )
            )
        )




        setContent {

            ipBroadcast = ipToBroadCast(readIP(applicationContext))

            KeepScreenOn()
            RttTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF090909)//MaterialTheme.colors.background
                )
                {
                    val pagerState = rememberPagerState()

                    LaunchedEffect(pagerState) {
                        // Collect from the pager state a snapshotFlow reading the currentPage
                        snapshotFlow { pagerState.currentPage }.collect { page ->
                            if (page == 1)
                            {
                                slegenie = telnetSlegenie.value!!
                                telnetSlegenie.value = false
                            }
                            if( page == 0)
                            {
                                telnetSlegenie.value = slegenie
                            }
                        }
                    }

                    HorizontalPager(count = 2, state = pagerState)
                    {
                        page ->
                       when(page)
                       {
                           0 -> lazy(colorline)
                           1 -> info()
                       }
                    }
                }
            }
        }
    }
}


@Composable
fun KeepScreenOn() {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val window = context.findActivity()?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

