package com.example.rtt

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay


@Composable
fun lazy(messages: SnapshotStateList<List<pairTextAndColor>>) {

    var update by remember { mutableStateOf(true) }  //для мигания

    println("---lazy---")

    val lazyListState: LazyListState = rememberLazyListState()

    //println("Индекс первого видимого элемента = " + lazyListState.firstVisibleItemIndex.toString())
    //println("Смещение прокрутки первого видимого элемента = " + lazyListState.firstVisibleItemScrollOffset.toString())
    //println("Количество строк выведенных на экран lastIndex = " + lazyListState.layoutInfo.visibleItemsInfo.lastIndex.toString())

    var lastVisibleItemIndex by remember {
        mutableStateOf(0)
    }

    lastVisibleItemIndex =
        lazyListState.layoutInfo.visibleItemsInfo.lastIndex + lazyListState.firstVisibleItemIndex
    //println("Последний видимый индекс = $lastVisibleItemIndex")
    //println("Количество записей = ${messages.size}")

    LaunchedEffect(key1 = messages) {
        while (true) {
            delay(700L)
            update = !update
            telnetWarning.value = (telnetSlegenie.value == false) && (messages.size > lastCount )
        }
    }

    LaunchedEffect(key1 = lastVisibleItemIndex, key2 = messages) {
        while (true) {
            delay(200L)
            val s = messages.size
            if ((s > 20) && (telnetSlegenie.value == true)) {
                lazyListState.scrollToItem(index = messages.size - 1) //Анимация (плавная прокрутка) к данному элементу.
            }
        }
    }

    Column(Modifier.fillMaxSize()) {

        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(1f)
        )
        {

            //Верхний блок Теминала
            ///////////////////////////////////////////////////////////
            LazyColumn(                                              //
                modifier = Modifier
                    .fillMaxHeight()
                    , state = lazyListState                          //
            ) {
                itemsIndexed(messages)
                { index, l ->
                    Row()
                    {
                        var s = l.size
                        if (s > 0) {
                            val str: String = when (index) {
                                in 0..9 -> String.format("   %d>", index)
                                in 10..99 -> String.format("  %d>", index)
                                in 100..999 -> String.format(" %d>", index)
                                else -> String.format("%d>", index)
                            }
                            Text(text = "$str", color = Color.Gray, fontFamily = FontFamily.Monospace)
                        }

                        for (i in 0 until s) {
                            if (l[i].flash == 1) {
                                if (update) {
                                    Text(
                                        text = l[i].text,
                                        color = l[i].colorText,
                                        modifier = Modifier.background(l[i].colorBg),
                                        textDecoration = if (l[i].underline == 1) TextDecoration.Underline else null,
                                        fontWeight = if (l[i].bold == 1) FontWeight.Bold else null,
                                        fontStyle = if (l[i].italic == 1) FontStyle.Italic else null,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            } else {
                                Text(
                                    text = l[i].text,
                                    color = l[i].colorText,
                                    modifier = Modifier.background(l[i].colorBg),
                                    textDecoration = if (l[i].underline == 1) TextDecoration.Underline else null,
                                    fontWeight = if (l[i].bold == 1) FontWeight.Bold else null,
                                    fontStyle = if (l[i].italic == 1) FontStyle.Italic else null,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }                                                  //
            ///////////////////////////////////////////////////////////

            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            )
            {
                val image: Painter = painterResource(id = R.drawable.warn2)
                val warning by telnetWarning.observeAsState()
                if (warning == true) {

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

        //Блок кнопок
        bottomNavigationLazy()

    }
}