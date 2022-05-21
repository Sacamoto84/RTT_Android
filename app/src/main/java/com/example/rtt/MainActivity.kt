package com.example.rtt

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.os.StrictMode
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import com.example.rtt.*
import com.example.rtt.R
import com.example.rtt.ui.theme.RttTheme
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

var currentScreen = MutableLiveData<String>("telnet")
var telnetSlegenie = MutableLiveData<Boolean>(true)
var telnetWarning = MutableLiveData<Boolean>(false) //Для отображения значка
var lastCount = 0 //Запоминаем последнне значение при отколючении слежения

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Создаем список цветов из Json цветов
        colorJsonToList()

        //Запуск получения пакетов
        val threadWithRunnable = Thread(udp_DataArrival())
        threadWithRunnable.start()

        colorline.add(
            listOf(
                pairTextAndColor(
                    text = "RTT V3 21.05.2022",
                    Color.Green,
                    Color.Black
                )
            )
        )

        setContent {

            KeepScreenOn()
            RttTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF090909)//MaterialTheme.colors.background
                ) {

                    Column(modifier = Modifier.fillMaxSize()) {

                        Box(
                            Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .weight(1f)
                        )
                        {
                            val screen by currentScreen.observeAsState()
                            if (screen == "telnet") {

                                lazy(colorline)

                                val warning by telnetWarning.observeAsState()
                                if (warning == true) {
                                    Box(
                                        Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.BottomEnd
                                    )
                                    {
                                        val image: Painter = painterResource(id = R.drawable.warn2)
                                        Image(
                                            painter = image,
                                            contentDescription = "",
                                            Modifier
                                                .size(48.dp)
                                                .padding(end = 10.dp)
                                        )
                                    }
                                }
                            }
                            else
                            {
                                info()
                            }
                        }

                        //Блок кнопок
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(50.dp), contentAlignment = Alignment.Center
                        )
                        {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF1B1B1B)),   //Тут цвет
                                horizontalArrangement = Arrangement.Center

                            ) {
                                Row(
                                    modifier = Modifier.fillMaxHeight()
                                        .weight(1f)
                                        .background(Color(0xFF1B1B1B)),

                                    horizontalArrangement = Arrangement.Center

                                )
                                {
                                    //По кнопке включаем слежение
                                    val screen by currentScreen.observeAsState()
                                    val slegenie by telnetSlegenie.observeAsState()

                                    //Скрываемая часть
                                    if (screen == "telnet") {
                                        Button(
                                            modifier = Modifier.fillMaxHeight().fillMaxWidth()
                                                .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                                                .weight(1f),
                                            colors = ButtonDefaults.buttonColors(backgroundColor = if (slegenie == true) Color.Green else Color.Gray),
                                            onClick = {
                                                telnetSlegenie.value =
                                                    !telnetSlegenie.value!!

                                                lastCount = colorline.size

                                            }
                                        )
                                        {
                                            Text(text = "Слежение ${colorline.size - 1}")
                                        }
                                        //Кнопка перезагрузки контроллера
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Button(modifier = Modifier.fillMaxHeight()
                                            .width(60.dp).padding(top = 8.dp, bottom = 8.dp)
                                            //.weight(1f)
                                            ,
                                            onClick = {
                                                sendUDP("Reset")
                                                colorline.add(
                                                    listOf(
                                                        pairTextAndColor(
                                                            text = "Команда перезагрузки контролера",
                                                            Color.Green,
                                                            Color.Black
                                                        )
                                                    )
                                                )
                                            }) {
                                            Text(
                                                text = "R"
                                            )
                                        }
                                        //Кнопка завуска сервера
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Button(modifier = Modifier.fillMaxHeight()
                                            .width(60.dp).padding(top = 8.dp, bottom = 8.dp)
                                            //.weight(1f)
                                            ,
                                            onClick = {
                                                sendUDP("Activate")
                                                colorline.add(
                                                    listOf(
                                                        pairTextAndColor(
                                                            text = "Команда запуска Telnet",
                                                            Color.Green,
                                                            Color.Black
                                                        )
                                                    )
                                                )
                                            }) {
                                            Text(
                                                text = "A"
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                    }
                                }


                                /////////////////////// Кнопка смены экрана
                                val screen by currentScreen.observeAsState()

                                Box(
                                    modifier = Modifier
                                        .width(60.dp).height(65.dp)
                                        .pointerInput(Unit) {
                                            detectTapGestures(
                                                onPress = { /* Called when the gesture starts */ },
                                                onDoubleTap = { /* Called on Double Tap */ },
                                                onLongPress = { /* Called on Long Press */ },
                                                onTap = {
                                                    if (currentScreen.value == "telnet")
                                                        currentScreen.value = "info"
                                                    else
                                                        currentScreen.value = "telnet"
                                                }
                                            )
                                        }
                                        .wrapContentHeight(Alignment.CenterVertically)
                                    , contentAlignment = Alignment.Center

                                ) {

                                    Text(text = if (screen == "telnet") "Инфо" else "Назад", color = Color.White)

                                }









                                Spacer(modifier = Modifier.width(8.dp))
                            }
                        }
                    }


                    //


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

fun sendUDP(messageStr: String) {
    // Hack Prevent crash (sending should be done using an async task)
    val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
    StrictMode.setThreadPolicy(policy)
    try {
        //Open a port to send the package
        val socket = DatagramSocket()
        socket.broadcast = true
        val sendData = messageStr.toByteArray()
        val sendPacket =
            DatagramPacket(sendData, sendData.size, InetAddress.getByName("192.168.0.255"), 8889)
        socket.send(sendPacket)
        println("fun sendBroadcast: packet sent to: " + "192.168.0.255" + ":" + 8888)
    } catch (e: IOException) {
        //            Log.e(FragmentActivity.TAG, "IOException: " + e.message)
    }
}