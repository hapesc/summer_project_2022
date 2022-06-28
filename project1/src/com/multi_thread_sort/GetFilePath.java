package com.multi_thread_sort;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class GetFilePath {
    //单例设计模式

    //临时文件总文件夹路径
    private final static String tempPath = "/Users/michael-liang/Desktop/IO_Test/Temp/";
    private final static String clientDir = "/Users/michael-liang/Desktop/Result/FromClient/";
    //各级临时文件的文件夹路径
    private ArrayList<ArrayList<ArrayList<String>>> tempFiles = new ArrayList<>();


    //为临时文件计数
    private static ArrayList<int[]> cnt = new ArrayList<>();


    private GetFilePath() {
        //  创建临时文件总文件夹
        try {
            Path tempDir = Paths.get(tempPath);
            if (!new File(tempPath).isDirectory()) {

                Files.createDirectory(tempDir);
            }
            String outputDir = "/Users/michael-liang/Desktop/Result/";
            if (!new File(outputDir).isDirectory()) {
                    Files.createDirectory(Paths.get(outputDir));
            }
            if (!new File(clientDir).isDirectory())
                Files.createDirectory(Paths.get(clientDir));
            for (int i = 0; i < 20; i++) {
                String tempdir = tempPath + "Temp" + i + "/";
                if (!new File(tempdir).isDirectory()) {
                    Files.createDirectory(Paths.get(tempdir));
                }
                cnt.add(new int[26]);
                tempFiles.add(i, new ArrayList<>(26));
                for (int j = 0; j < 26; j++) {
                    tempFiles.get(i).add(j, new ArrayList<>());
                    outputDir = tempPath + "Temp" + i + "/" + "result" + (char) ('a' + j) + "/";
                    //新建TempX文件夹
                    Path dir = Paths.get(outputDir);
                    File f = new File(outputDir);
                    if (!f.isDirectory() && !f.exists()) {
                        Files.createDirectory(dir);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
        //创建客户端文件夹

    }

    private static GetFilePath path = new GetFilePath();

    public static GetFilePath getFilePath() {
        return path;
    }

    /**
     * 获得"dataXX.txt"或"resultaTempX0.txt"的文件地址
     *
     * @param num         源文件序号
     * @param tempNum     -1代表源文件，大于0代表临时文件序号
     * @param alpha       临时文件首字母
     * @param mergedTimes 归并次数
     * @return 对应文件路径
     */
    public String getInputPath(int num, int tempNum, int mergedTimes, char alpha) {
        String dir = "/Users/michael-liang/Desktop/IO_Test/";
        if (tempNum == -1) {
            if (num < 10)
                return dir + "data" + "0" + num + ".txt";
            else
                return dir + "data" + num + ".txt";
        } else if (tempNum >= 0) {
            return tempPath + "Temp" + mergedTimes + "/result" + alpha + "/" + tempNum + ".txt";
        }
        return null;
    }


    /**
     * @param alpha       字符串首字母
     * @param num         源文件序号 -1代表最终输出文件，1-8代表临时文件
     * @param tempNum     代表每一个batch产生的临时文件序号,-1代表输出最终文件
     * @param mergedTimes 归并次数
     * @return 对应文件路径
     */
    public String getOutputPath(char alpha, int num, int tempNum, int mergedTimes) {
        String outPath = null;
        //最终文件的输出路径
        if (num == -1 && tempNum == -1) {
            String outputDir = "/Users/michael-liang/Desktop/Result/";
            Path tempDir = Paths.get(outputDir);
            if (!new File(outputDir).isDirectory()) {
                try {
                    Files.createDirectory(tempDir);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            outPath = "/Users/michael-liang/Desktop/Result/result" + alpha + ".txt";
        }
        //临时文件输出路径：Temp/Temp+mergedTimes/result+a-z/num.txt
        if (num > 0) {
            String outputDir = tempPath + "Temp" + mergedTimes + "/" + "result" + alpha + "/";

            if (tempFiles.get(mergedTimes).size() == 0)
                this.tempFiles.get(mergedTimes).add(alpha - 'a', new ArrayList<>());
            outPath = outputDir + (getNum(alpha, mergedTimes)) + ".txt";
            tempFiles.get(mergedTimes).get(alpha - 'a').add(outPath);
        }
        return outPath;
    }

    public ArrayList<ArrayList<ArrayList<String>>> getTempFiles() {
        return tempFiles;
    }

    private synchronized int getNum(char alpha, int mergedTimes) {
        int a = cnt.get(mergedTimes)[alpha - 'a']++;
        return a;
    }

    //server从client接收到字符以后，要写入的文件路径
    public String getClientPath(char alpha, int clientName) {

        return clientDir+alpha + clientName + ".txt";
    }

    //待传输的文件的文件路径
    public String getTransmitPath(char alpha, int clientName) {

        return getOutputPath(alpha,-1,-1,0);
    }
}
