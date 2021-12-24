package cn.com.heyue.utils;

import sun.net.ftp.FtpClient;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class FtpUtils
{
    
    private FtpClient ftpClient;
    
    private String server;
    
    private String port;
    
    private String user;
    
    private String password;
    
    private String timeout;
    
    public void setServer(String server)
    {
        this.server = server;
    }
    
    public void setPort(String port)
    {
        this.port = port;
    }
    
    public void setUser(String user)
    {
        this.user = user;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public void setTimeout(String timeout)
    {
        this.timeout = timeout;
    }
    
    public void connectServer(String path)
        throws Exception
    {
        // server：FTP服务器的IP地址；user:登录FTP服务器的用户名
        // password：登录FTP服务器的用户名的口令；path：FTP服务器上的路径
        ftpClient = FtpClient.create();
        ftpClient.setConnectTimeout(Integer.parseInt(timeout));
        ftpClient.connect(new InetSocketAddress(server, Integer.valueOf(port)));
        ftpClient.login(user, password.toCharArray());
        // path是ftp服务下主目录的子目录
        if (path.length() != 0)
            ftpClient.changeDirectory(path);
        // 用2进制上传、下载
        ftpClient.setBinaryType();
    }
    
    public void upload(String filename, String newname)
        throws Exception
    {
        OutputStream os = null;
        FileInputStream is = null;
        try
        {
            File file_in = new File(filename);
            if (!file_in.exists())
            {
                file_in.createNewFile();
            }
            os = ftpClient.putFileStream(newname);
            is = new FileInputStream(file_in);
            byte[] bytes = new byte[1024];
            int c;
            while ((c = is.read(bytes)) != -1)
            {
                os.write(bytes, 0, c);
            }
        }
        finally
        {
            if (is != null)
            {
                is.close();
            }
            if (os != null)
            {
                os.close();
            }
        }
    }

    public void upload(InputStream is, String newname)
            throws Exception
    {
        OutputStream os = null;
        try
        {
            os = ftpClient.putFileStream(newname);
            byte[] bytes = new byte[1024];
            int c;
            while ((c = is.read(bytes)) != -1)
            {
                os.write(bytes, 0, c);
            }
        }
        finally
        {
            if (is != null)
            {
                is.close();
            }
            if (os != null)
            {
                os.close();
            }
        }
    }
    
	public void writer(List<String> readList, String newname) throws Exception {
		OutputStream os = null;
		BufferedWriter bufWr = null;
		try {

			os = ftpClient.putFileStream(newname);
			bufWr = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			for (String str : readList) {
				bufWr.write(str);
				bufWr.newLine();
			}
			bufWr.flush();
		} finally {
			if (bufWr != null) {
				bufWr.close();
			}
			if (os != null) {
				os.close();
			}
		}
	}
    
    /**
     * upload
     * 
     * @throws Exception
     * @return
     * @param filename
     * @throws IOException
     */
    public void upload(String filename)
        throws Exception
    {
        String newname = "";
        if (filename.indexOf(File.separator) > -1)
        {
            newname = filename.substring(filename.lastIndexOf(File.separator) + 1);
        }
        else
        {
            newname = filename;
        }
        upload(filename, newname);
    }

    /**
     * 将ftp上的文件转移目录
     *
     * @throws Exception
     * @return
     * @param filename 原文件
     * @param newfilename 新文件
     */
    public void moveFile(String filename, String newfilename)
            throws Exception
    {
        ftpClient.rename(filename,newfilename);
    }

    /**
     * download 从ftp下载文件到本地
     * 
     * @throws Exception
     * @return
     * @param newfilename 本地生成的文件名
     * @param filename 服务器上的文件名
     */
    public void download(String filename, String newfilename)
        throws Exception
    {
        InputStream is = null;
        FileOutputStream os = null;
        try
        {
            is = ftpClient.getFileStream(filename);
            File outfile = new File(newfilename.trim());
            if (!outfile.exists())
            {
                String directory =
                    outfile.getAbsolutePath().substring(0, outfile.getAbsolutePath().lastIndexOf(File.separator) + 1);
                File dir = new File(directory);
                if (!dir.exists())
                {
                    dir.mkdirs();
                }
                outfile.createNewFile();
            }
            os = new FileOutputStream(outfile);
            byte[] bytes = new byte[1024];
            int c;
            while ((c = is.read(bytes)) != -1)
            {
                os.write(bytes, 0, c);
            }
            
        }catch (Exception e) {
			e.printStackTrace();
		}
        finally
        {
            
            if (is != null)
            {
                is.close();
            }
            if (os != null)
            {
                os.close();
            }
        }
    }
    
    /**
     * 取得某个目录下的所有文件列表
     * 
     */
    public List<String> getFileList(String path)
    {
        List<String> list = new ArrayList<String>();
        try
        {
            InputStream in = ftpClient.nameList(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String filename = reader.readLine();
            while (filename != null && !filename.equals(""))
            {
                list.add(filename);
                filename = reader.readLine();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return list;
    }
    
    /**
     * 取得当前目录下的所有文件
     * 
     * @throws IOException
     * 
     */
    public List<String> getAllFile(String localPath)
        throws Exception
    {
        List<String> list = getFileList("");
        List<String> newList = new ArrayList<String>();
        File file;
        for (String filename : list)
        {
            file = new File(localPath, filename);
            download(filename, file.getPath());
            newList.add(filename);
        }
        return newList;
    }
    
    /**
     * 获取指定目录下的所有文件
     * @param remotPath
     * @param localPath
     * @return
     * @throws IOException
     */
    public List<String> getZdFile(String remotPath,String localPath)
        throws Exception
    {
        List<String> list = getFileList(remotPath);
        List<String> newList = new ArrayList<String>();
        File file;
        for (String filename : list)
        {
            file = new File(localPath, filename);
            download(filename, file.getPath());
            newList.add(filename);
        }
        return newList;
    }
    
    /**
     * closeServer 断开与ftp服务器的链接
     * 
     * @throws IOException
     */
    public void closeServer()
        throws IOException
    {
        try
        {
            if (ftpClient != null)
            {
                ftpClient.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws Exception
    {
        FtpUtils ftpUtils = new FtpUtils();
        ftpUtils.setServer("110.42.198.165");
        ftpUtils.setPort("21");
        ftpUtils.setUser("tsm");
        ftpUtils.setPassword("~S;/mUh3Wz._j2");
        ftpUtils.setTimeout("30000");
        try {
            ftpUtils.connectServer("/data/tsm_cardfile/createcard_loacl_catalog/");
            System.out.println("登录成功。。。");
//            ftpUtils.upload("D:/bak/upload.txt", "upload.txt");// 本地路径,ftp路径
//            File f = new File("D:/bak", "tsmFTP用户.txt");
//            ftpUtils.download("tsmFTP用户.txt", f.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
