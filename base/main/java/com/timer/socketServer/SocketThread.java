package com.timer.socketServer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class SocketThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(SocketThread.class);

    // 和本线程相关的Socket
    Socket socket = null;
    SocketService socketService = null;

    public SocketThread(Socket socket, SocketService socketService) {
        this.socket = socket;
        this.socketService = socketService;
    }

    public void run() {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        OutputStreamWriter os = null;
        PrintWriter pw = null;
        try {
            // 获取输入流，并读取客户端信息
            is = socket.getInputStream();
            isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            br = new BufferedReader(isr);
            String info = null;

            int retype = 0;
            String jsonResult = "{\"Code\":\"0\",\"Msg\":\"成功！\"}";
            try {
                while ((info = br.readLine()) != null) {// 循环读取客户端的信息
                    // info=new String(info.);
                    jsonResult = null;
                    Date d = new Date();
                    logger.info("参数：" + info + "  " + d);
                    JSONObject jsonObject = JSON.parseObject(info);
                    Date startTime = jsonObject.getDate("startTime");
                    Date endTime = jsonObject.getDate("endTime");

                    if (startTime != null && endTime != null) {

                        jsonResult = socketService.initData(startTime, endTime);
                    } else {
                        jsonResult = "{\"Code\":\"1\",\"Msg\":\"失败！\"}";
                    }

                }
            } catch (Exception e) {
                logger.debug(e.getMessage());
                e.printStackTrace();
                jsonResult = "{\"Code\":\"1\",\"Msg\":\"失败！\"}";
            }
            socket.shutdownInput();// 关闭输入流
            // 获取输出流，响应客户端的请求
            os = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);

            pw = new PrintWriter(os, true);

            pw.write(jsonResult);

            pw.flush();// 调用flush()方法将缓冲输出
        } catch (IOException e) {
            logger.debug(e.getMessage());
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (pw != null)
                    pw.close();
                if (os != null)
                    os.close();
                if (br != null)
                    br.close();
                if (isr != null)
                    isr.close();
                if (is != null)
                    is.close();
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
    }
}
