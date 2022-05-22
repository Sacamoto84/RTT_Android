package com.example.rtt

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

private val heghtHabigation = 50.dp
private val colorBg = Color(0xFF1B1B1B)

@Composable
fun bottomNavigationLazy() {
    Box(
        Modifier
            .fillMaxWidth()
            .height(heghtHabigation)
            .background(colorBg), contentAlignment = Alignment.Center
    )
    {

        Row(
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {

            //По кнопке включаем слежение
            val slegenie by telnetSlegenie.observeAsState()

            // Кнопка включения слежения
            Button(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(backgroundColor = if (slegenie == true) Color.Green else Color.Gray),
                onClick = {
                    telnetSlegenie.value =
                        !telnetSlegenie.value!!
                    lastCount = colorline.size
                }
            ) {
                Text(text = "Слежение ${colorline.size - 1}")
            }

            //Кнопка перезагрузки контроллера
            Spacer(modifier = Modifier.width(8.dp))
            Button(modifier = Modifier
                .fillMaxHeight()
                .width(80.dp)
                .padding(top = 8.dp, bottom = 8.dp),
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
                    text = "RST"
                )
            }

            //Кнопка завуска сервера
            Spacer(modifier = Modifier.width(8.dp))
            Button(modifier = Modifier
                .fillMaxHeight()
                .width(80.dp)
                .padding(top = 8.dp, bottom = 8.dp)
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
                    text = "JLINK"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
fun bottomNavigationInfo() {
    Box(
        Modifier
            .fillMaxWidth()
            .height(heghtHabigation)
            .background(colorBg)

, contentAlignment = Alignment.Center
    )
    {

    }
}