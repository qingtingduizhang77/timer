package com.timer.socketServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

@Component
public class SocketServer {

    private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);

    @Value("${socketconfig.socketPort}")
    private String socketPort;

    @Autowired
    private SocketService socketService;

    public void startSocket() {
        try {
            //1.创建一个服务器端Socket，即ServerSocket，指定绑定的端口，并监听此端口
            String prot = socketPort;
            if (prot != null && !prot.equals("")) {
                ServerSocket serverSocket = new ServerSocket(new Integer(prot.trim()));
                Socket socket = null;
                //记录客户端的数量
                Date d = new Date();
                logger.info("***服务器即将启动，等待客户端的连接***" + d);
                //循环监听等待客户端的连接
                while (true) {
                    //调用accept()方法开始监听，等待客户端的连接
                    socket = serverSocket.accept();
                    //创建一个新的线程
                    SocketThread serverThread = new SocketThread(socket, socketService);
                    //启动线程
                    serverThread.start();
                    InetAddress address = socket.getInetAddress();
//					System.out.println("当前客户端的IP："+address.getHostAddress());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            logger.debug(e.getMessage());
        }
    }
}
