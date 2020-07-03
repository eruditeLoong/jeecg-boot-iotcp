package org.jeecg.modules.network.network.tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketTest {

    public static void main(String[] args) {
        String host = "192.168.1.100";
        int port = 8888;
        boolean running = true;
        String msg = "Client Data";
        try {
            //创建一个Socket,跟本机的8080端口连接
            Socket socket = new Socket();
            socket.bind(new InetSocketAddress(2333));//绑定本地端口
            socket.connect(new InetSocketAddress(host, port));//连接远程服务端接口

            //使用Socket创建PrintWriter和BufferedReader进行读写数据
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (running) {
                //发送数据
                pw.println(msg);
                pw.flush();
                //接收数据
                String line = is.readLine();
                System.out.println("received from server:" + line);

                Thread.sleep(2000);
            }
            //关闭资源
            // pw.close();
            // is.close();
            // socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
