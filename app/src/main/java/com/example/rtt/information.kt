package com.example.rtt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


val textSize = 12.sp
val fontWeight = FontWeight.Normal
val boxSize = 32.dp

@Composable
fun info() {

    val scrollState = rememberScrollState()

    Column(Modifier.fillMaxSize()) {

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .weight(1f)
        )
        {

            Text(text = "01 Bold 03 Italic 04 Underline 07 Revers 08 Flash", color = Color.White)
            Text(text = """\x1B[0m \033[ \u001b 38;05;xxx Text 48;05;xxx Bg""", color = Color.White)
            Text(text = """Конец строки \r\n""", color = Color.White)

            //Рисуем таблицу
            Column(
                Modifier
                    .fillMaxSize(),
                //verticalArrangement = Arrangement.SpaceBetween
            ) {

                for (i in 0..1) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    )
                    {

                        for (x in 0..7) {

                            Box(
                                Modifier
                                    .padding(start = 0.5.dp, top = 0.5.dp)
                                    .height(boxSize)
                                    .weight(1f)
                                    .background(colorIn256(x + i * 8)),
                                contentAlignment = Alignment.Center
                            )
                            {
                                val textcolor = when (x + i * 8) {
                                    in 0..4, in 16..27, in 232..243 -> Color(0xFFBBBBBB)
                                    else -> Color.Black
                                }

                                Text(
                                    text = "${x + i * 8}",
                                    color = textcolor,
                                    fontSize = textSize,
                                    fontWeight = fontWeight
                                )
                            }
                        }
                    }
                }

                var index = 16

                for (i in 0..14) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    )
                    {

                        for (x in 0..15) {

                            Box(
                                Modifier
                                    .height(boxSize)
                                    .padding(start = 0.5.dp, top = 0.5.dp)
                                    .weight(1f)
                                    .background(colorIn256(index)),
                                contentAlignment = Alignment.Center
                            )
                            {


                                val textcolor = when (index) {
                                    in 0..4, in 16..27, in 232..243 -> Color(0xFFBBBBBB)
                                    else -> Color.Black
                                }

                                Text(
                                    text = "${index}",
                                    color = textcolor,
                                    fontSize = textSize,
                                    fontWeight = fontWeight
                                )

                                index++
                            }
                        }
                    }
                }
            }

            val buttonFontSize = 12.sp

            Row(
                modifier = Modifier.padding(top = 4.dp)
                    .fillMaxWidth()
                    .height(50.dp)
            ) {

                Button(onClick = {
                    console_text = 12.sp
                    LineAdd("Изменение шрифта")
                    shared.edit().putString("size" , "12").apply()
                }, modifier = Modifier.fillMaxWidth().weight(1f)
                )
                {
                    Text("12", fontSize = buttonFontSize)
                }

                Button(onClick = {
                    console_text = 14.sp
                    LineAdd("Изменение шрифта")
                    shared.edit().putString("size" , "14").apply()
                }, modifier = Modifier.fillMaxWidth().weight(1f).padding(start = 1.dp)
                )
                {
                    Text("14", fontSize = buttonFontSize)
                }

                Button(onClick = {
                    console_text = 16.sp
                    LineAdd("Изменение шрифта")
                    shared.edit().putString("size" , "16").apply()
                }, modifier = Modifier.fillMaxWidth().weight(1f).padding(start = 1.dp))
                {
                    Text("16", fontSize = buttonFontSize)
                }

                Button(onClick = {
                    console_text = 18.sp
                    LineAdd("Изменение шрифта")
                    shared.edit().putString("size" , "18").apply()
                }, modifier = Modifier.fillMaxWidth().weight(1f).padding(start = 1.dp))
                {
                    Text("18", fontSize = buttonFontSize)
                }

                Button(onClick = {
                    console_text = 20.sp
                    LineAdd("Изменение шрифта")
                    shared.edit().putString("size" , "20").apply()
                }, modifier = Modifier.fillMaxWidth().weight(1f).padding(start = 1.dp))
                {
                    Text("20", fontSize = buttonFontSize)
                }

                Button(onClick = {
                    console_text = 22.sp
                    LineAdd("Изменение шрифта")
                    shared.edit().putString("size" , "22").apply()
                }, modifier = Modifier.fillMaxWidth().weight(1f).padding(start = 1.dp))
                {
                    Text("22", fontSize = buttonFontSize)
                }

                Button(onClick = {
                    console_text = 24.sp
                    LineAdd("Изменение шрифта")
                    shared.edit().putString("size" , "24").apply()
                },modifier = Modifier.fillMaxWidth().weight(1f).padding(start = 1.dp))
                {
                    Text("24", fontSize = buttonFontSize)
                }

                Button(onClick = {
                    console_text = 26.sp
                    LineAdd("Изменение шрифта")
                    shared.edit().putString("size" , "26").apply()

                }, modifier = Modifier.fillMaxWidth().weight(1f).padding(start = 1.dp))
                {
                    Text("26", fontSize = buttonFontSize)
                }
            }
        }
    }
}