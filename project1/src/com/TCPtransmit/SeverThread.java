package com.TCPtransmit;

import com.multi_thread_sort.GetFilePath;

import java.io.*;
import java.net.Socket;

public class SeverThread implements Runnable {
    private Socket socket;
    private InputStream in;
    private char alpha;
    private int clientName;
    private GetFilePath path;

    public SeverThread(Socket socket,GetFilePath path) {
        this.socket = socket;
        this.path=path;
    }

    @Override
    public void run() {
        String line = null;
        Reader inr = null;
        BufferedReader bfr = null;
        //获取输入流
        try {
            in = socket.getInputStream();
            inr = new InputStreamReader(in);
            bfr = new BufferedReader(inr);
            this.clientName = Integer.valueOf(bfr.readLine());
            this.alpha = bfr.readLine().charAt(0);
            System.out.println("连接已建立：client" + clientName);
            System.out.println("即将传输result" + alpha);
            String outputPath = path.getClientPath(alpha, clientName);
            FileWriter out = new FileWriter(outputPath);
            while ((line = bfr.readLine()) != null) {
                out.write(line + '\n');
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //close socket
        try {
            bfr.close();
            inr.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
