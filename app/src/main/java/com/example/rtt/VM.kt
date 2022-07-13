package com.example.rtt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket

class VM : ViewModel() {

    //Канал передачи
    val channel = Channel<String>(1000000)


    fun launchUDPRecive() {
        viewModelScope.launch {
            UDPRoutine()
        }
    }

    fun launchUIChanelRecive() {
        viewModelScope.launch {
            reciveUI();
        }
    }

    private suspend fun reciveUI() = withContext(Dispatchers.Main)
    {
        while (true) {
            val string = channel.receive()
            val list = string.split("\n")
            for (str in list) {
                if (str == "") {
                    continue
                } else {
                    val linepair = stringcalculate(str) //Создаем список пар для одной строки
                    colorline.add(linepair)
                }
            }
        }
    }

    private suspend fun UDPRoutine() = withContext(Dispatchers.IO) {
        println("Запуск UDPRoutine ")
        val buffer = ByteArray(2048 * 1024)
        var socket: DatagramSocket? = null
        socket = DatagramSocket(8888)
        socket.broadcast = true
        val packet = DatagramPacket(buffer, buffer.size)
        while (true) {
            //socket.soTimeout = 0
            socket.receive(packet)
            var buffer1: ByteArray = packet.data.copyOfRange(0, packet.length)
            val string = String(buffer1)
            //println("!!!!!!!!!!UDP!!!!!!!!!:$string")
            channel.send(string)
        }
        socket.close()
    }
}