// sample code to create an app to scan an host and their ports then save the report as txt on the device

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class PortScanner {

    public static void main(String[] args) throws IOException {
        String host = "192.168.1.1";
        int portStart = 1;
        int portEnd = 65535;
        StringBuilder sb = new StringBuilder();

        for (int port = portStart; port <= portEnd; port++) {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(host, port), 200);
                String result = String.format("%s:%s\n", host, port);
                System.out.println(result);
                sb.append(result);
            } catch (Exception e) {
                // Port is closed or filtered
            }
        }

        // Write report to file
        File file = new File(getExternalStorageDirectory(), "portscan_report.txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(sb.toString());
        }
    }
}
