package com.example.rtt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
fun info(modifier: Modifier = Modifier) {

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
                for (i in 2..30) {
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
            }

        }
    }
}