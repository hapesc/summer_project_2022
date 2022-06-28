package com.TCPtransmit;

import com.multi_thread_sort.GetFilePath;

import java.io.*;
import java.net.Socket;

public class Client implements Runnable{
    private Socket client=null;
    private String IP;
    private int port;
    private char alpha;
    private int clientName;
    private GetFilePath path;

    public Client(String IP, int port, char alpha, int clientName,GetFilePath path) {
        this.IP = IP;
        this.port = port;
        this.alpha = alpha;
        this.clientName = clientName;
        this.path=path;
    }

    @Override
    public void run() {
        try {
            this.client = new Socket(IP, port);
            String line = null;
            OutputStream os = client.getOutputStream();
            Writer writer = new OutputStreamWriter(os);
            //先将主机序号和文件名传输到server上
            writer.write(clientName + '\n');
            writer.flush();
            writer.write(alpha + '\n');
            writer.flush();
            String fileName = path.getInputPath(0, -1, 0, alpha);
            try (BufferedReader bfr = new BufferedReader(new FileReader(fileName))) {
                while ((line = bfr.readLine()) != null) {
                    writer.write(line + '\n');
                    writer.flush();
                }
                writer.close();
                client.close();

            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
