package cn.com.heyue.utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileUtils {
    public static String saveFile(byte[] file, String filePath, String fileName) {
        int random = (int) (Math.random() * 100 + 1);
        int random1 = (int) (Math.random() * 100 + 1);
        filePath = filePath + random + File.separator + random1 + File.separator;
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filePath + fileName);
            FileChannel fileChannel = fileOutputStream.getChannel();
            ByteBuffer buf = ByteBuffer.wrap(file);
            while (fileChannel.write(buf) != 0) {
            }
        } catch (Exception e) {

        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //url
        return random + "/" + random1 + "/" + fileName;
    }

    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 读取文件内容返回到列表
     *
     * @param filePath 文件列表
     * @return
     * @throws Exception
     */
    public static List<String> getFile(String filePath) throws Exception {
        List<String> arrayList = new ArrayList<String>();
        FileReader fr = null;
        BufferedReader bf = null;
        try {
            fr = new FileReader(filePath);
            bf = new BufferedReader(fr);
            String str;
            // 按行读取字符串
            while ((str = bf.readLine()) != null) {
                arrayList.add(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bf != null) {
                bf.close();
            }
            if (fr != null) {
                fr.close();
            }
        }
        return arrayList;
    }

    public static String renameToUUID(String fileName) {
        return UUID.randomUUID() + "." + fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 将list按行写入到txt文件中
     *
     * @param strings
     * @param path
     * @throws Exception
     */
    public static void writeFileContext(List<String> strings, String path) throws Exception {
        File file = new File(path);
        //如果没有文件就创建
        if (!file.isFile()) {
            file.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        for (String l : strings) {
            writer.write(l + "\r\n");
        }
        writer.close();
    }

    /**
     * 写入文件内容到指定文件
     *
     * @param filePath    文件路径
     * @param fileContent 文件内容
     * @throws Exception
     */
    public static void writeFile(String filePath, String fileContent) throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream, "utf-8");
        PrintWriter out = new PrintWriter(writer);
        try {
            out.write(fileContent);
        } finally {
            // 关闭
            try {
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                out = null;
            }
        }
    }

    /**
     * 获取文件大小(字节byte)
     *
     * @param path
     * @return
     */
    public static long getFileLength(String path){
        File file = new File(path);
        long fileLength = 0L;
        if(file.exists() && file.isFile()){
            fileLength = file.length();
        }
        return fileLength;
    }
}
