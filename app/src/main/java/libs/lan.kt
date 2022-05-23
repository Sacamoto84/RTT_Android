package libs

import android.os.StrictMode
import android.util.Log
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

// Добавить в манифест
// <uses-permission android:name= "android.permission.INTERNET" />
// <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
// <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />

/*

   * ddddddd
   # dffffff
   ! eee3333

 */


/*

*     ░░░░░░░░░
    * ▒▒▒▒▒▒▒▒▒ opopop ▒▒▒▒▒▒▒▒
    *  ╔═════════╗
    *  ║ sendUDP ║====> ┌─────┐
    *  ╚═════════╝      │  !  │
    *                   └─────┘


*/


//=====================================================
// * Отправить Udp сообщение *
// region // sendUDP(messageStr: String, ip :String, port: Int)
fun sendUDP(messageStr: String, ip :String, port: Int) {
    // Hack Prevent crash (sending should be done using an async task)
    val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
    StrictMode.setThreadPolicy(policy)
    try {
        val socket = DatagramSocket()
        socket.broadcast = true
        val sendData = messageStr.toByteArray()
        val sendPacket =
            DatagramPacket(sendData, sendData.size, InetAddress.getByName(ip), port)
        socket.send(sendPacket)
        println("sendUDP: $ip:$port")
    } catch (e: IOException) {
        Log.e("sendUDP", "IOException: " + e.message)
    }
}
//endregion
//=====================================================




