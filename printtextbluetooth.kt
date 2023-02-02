//Sample code to print text to a bluetooth printer, change the UUID for the ID of your printer and the text as the one you wish 

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import java.io.OutputStream
import java.util.*

fun main() {
    val adapter = BluetoothAdapter.getDefaultAdapter()
    val device = adapter.getRemoteDevice("00:11:22:33:AA:BB")
    val socket = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))
    socket.connect()
    val outputStream = socket.getOutputStream()
    outputStream.write("Hello, Bluetooth Printer".toByteArray())
    outputStream.flush()
    socket.close()
}
