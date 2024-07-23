package demo.spring.controller;


import com.jcraft.jsch.*;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;
import java.util.Vector;

/**
 * @Author:QY
 * @ClassName: SFTPUtil
 * @Description: sftp连接工具类
 * @date 2020-12-03
 * @version 1.0.0
 */
public class SFTPUtil {

    private final Logger log = LoggerFactory.getLogger(SFTPUtil.class);

    private ChannelSftp sftp;

    private Session session;

    /** FTP 登录用户名*/
    private String username;
    /** FTP 登录密码*/
    private String password;
    /** 私钥 */
    private String privateKey;
    /** FTP 服务器地址IP地址*/
    private String host;
    /** FTP 端口*/
    private int port;


    private SFTPUtil(){}

    /**
     * QY
     * 建议使用配置构参方式，可拓展性比较强
     * @param sftpConfigModel
     */
    public SFTPUtil(SFTPConfigModel sftpConfigModel){
        this.username = sftpConfigModel.getUserName();
        this.password = sftpConfigModel.getPassWord();
        this.privateKey = sftpConfigModel.getPrivateKey();
        this.host = sftpConfigModel.getHost();
        this.port = sftpConfigModel.getPort();
    }


    /**
     * 连接sftp服务器
     *
     * @throws Exception
     */
    public void login(){
        try {
            JSch jsch = new JSch();
            if (privateKey != null) {
                jsch.addIdentity(privateKey);// 设置私钥
                log.info("sftp connect,path of private key file：{}" , privateKey);
            }
            log.info("sftp connect by host:{} username:{}",host,username);

            session = jsch.getSession(username, host, port);
            log.info("Session is build");
            if (password != null) {
                session.setPassword(password);
            }
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");

            session.setConfig(config);
            session.connect();
            log.info("Session is connected");

            Channel channel = session.openChannel("sftp");
            channel.connect();
            log.info("channel is connected");

            sftp = (ChannelSftp) channel;
            log.info(String.format("sftp server host:[%s] port:[%s] is connect successfull", host, port));
        } catch (JSchException e) {
            log.error("Cannot connect to specified sftp server : {}:{} \n Exception message is: {}", new Object[]{host, port, e.getMessage()});
        }
    }

    /**
     * 关闭连接 server
     */
    public void logout(){
        if (sftp != null) {
            if (sftp.isConnected()) {
                sftp.disconnect();
                log.info("sftp is closed already");
            }
        }
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
                log.info("sshSession is closed already");
            }
        }
    }

    /**
     * 将输入流的数据上传到sftp作为文件
     *
     * @param directory
     *            上传到该目录
     * @param sftpFileName
     *            sftp端文件名
     * @param input
     *            输入流
     * @throws SftpException
     * @throws Exception
     */
    public void upload(String directory, String sftpFileName, InputStream input) throws SftpException{
        try {
            sftp.cd(directory);
        } catch (SftpException e) {
            log.warn("directory is not exist");
            sftp.mkdir(directory);
            sftp.cd(directory);
        }
        sftp.put(input, sftpFileName);
        log.info("file:{} is upload successful" , sftpFileName);
    }

    /**
     * 上传单个文件
     *
     * @param directory
     *            上传到sftp目录
     * @param uploadFile
     *            要上传的文件,包括路径
     * @throws FileNotFoundException
     * @throws SftpException
     * @throws Exception
     */
    public void upload(String directory, String uploadFile) throws FileNotFoundException, SftpException{
        File file = new File(uploadFile);
        upload(directory, file.getName(), new FileInputStream(file));
    }

    /**
     * 将byte[]上传到sftp，作为文件。注意:从String生成byte[]是，要指定字符集。
     *
     * @param directory
     *            上传到sftp目录
     * @param sftpFileName
     *            文件在sftp端的命名
     * @param byteArr
     *            要上传的字节数组
     * @throws SftpException
     * @throws Exception
     */
    public void upload(String directory, String sftpFileName, byte[] byteArr) throws SftpException{
        upload(directory, sftpFileName, new ByteArrayInputStream(byteArr));
    }

    /**
     * 将字符串按照指定的字符编码上传到sftp
     *
     * @param directory
     *            上传到sftp目录
     * @param sftpFileName
     *            文件在sftp端的命名
     * @param dataStr
     *            待上传的数据
     * @param charsetName
     *            sftp上的文件，按该字符编码保存
     * @throws UnsupportedEncodingException
     * @throws SftpException
     * @throws Exception
     */
    public void upload(String directory, String sftpFileName, String dataStr, String charsetName) throws UnsupportedEncodingException, SftpException{
        upload(directory, sftpFileName, new ByteArrayInputStream(dataStr.getBytes(charsetName)));
    }

    /**
     * 下载文件
     *
     * @param directory
     *            下载目录
     * @param downloadFile
     *            下载的文件
     * @param saveFile
     *            存在本地的路径
     * @throws SftpException
     * @throws FileNotFoundException
     * @throws Exception
     */
    public void download(String directory, String downloadFile, String saveFile) throws SftpException, FileNotFoundException{
        if (directory != null && !"".equals(directory)) {
            sftp.cd(directory);
        }
        File file = new File(saveFile);
        OutputStream output=new FileOutputStream(file);
        sftp.get(downloadFile, output);
        log.info("file:{} is download successful" , downloadFile);
    }

    /**
     * 下载文件
     * @param directory 下载目录
     * @param downloadFile 下载的文件名
     * @return 字节数组
     * @throws SftpException
     * @throws IOException
     * @throws Exception
     */
    public byte[] download(String directory, String downloadFile) throws SftpException, IOException{
        if (directory != null && !"".equals(directory)) {
            sftp.cd(directory);
        }
        InputStream is = sftp.get(downloadFile);

        byte[] fileData = IOUtils.toByteArray(is);

        log.info("file:{} is download successful" , downloadFile);
        return fileData;
    }

    /**
     * 删除文件
     *
     * @param directory
     *            要删除文件所在目录
     * @param deleteFile
     *            要删除的文件
     * @throws SftpException
     * @throws Exception
     */
    public void delete(String directory, String deleteFile) throws SftpException{
        if (isExist(directory,deleteFile)){
            sftp.cd(directory);
            sftp.rm(deleteFile);
            log.info("file:{} is delete successful" , deleteFile);
        }else {
            log.info("file:{} is delete failure,because of file is not exist" , deleteFile);
        }
    }

    /**
     * 列出目录下的文件
     *
     * @param directory 要列出的目录
     * @return
     * @throws SftpException
     */
    public Vector<?> listFiles(String directory) throws SftpException {
        return sftp.ls(directory);
    }


    /**
     * qy
     * 判断是否存在该文件
     * @param directory 路径
     * @param fileName  文件名称
     * @return
     * @throws SftpException
     */
    public boolean isExist(String directory, String fileName) throws SftpException {
        return isExist(directory+"/"+fileName);
    }


    /**
     * qy
     * 判断是否存在该文件
     * 文件不存在的话会抛 SftpException，e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE
     * @param path 文件(绝对路径)
     * @return
     * @throws SftpException
     *
     */
    public boolean isExist(String path) throws SftpException {
        boolean flag = false;
        try {
            sftp.stat(path);
            flag = true;
        }
        catch (SftpException e){
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE){
                log.info("path:{} is not exist" , path);
                flag = false;
            }
        }
        return flag;
    }

    /**
     * Qy
     * 修改文件名
     * @param path      路径
     * @param oldName   原始名称
     * @param newName   修改名称
     * @throws SftpException
     */
    public boolean rename(String path, String oldName, String newName) throws SftpException{
        boolean flag = false;
        try {
            System.out.println(path+"/"+oldName+"------->"+path+"/"+newName);
            sftp.rename(path+"/"+oldName,path+"/"+newName);
            flag = true;
            log.info("old file name:[{}] rename to new file name:[{}] successful" , oldName, newName);
        }
        catch (SftpException e){
            log.warn("old file name:[{}] is not exist or new file name:[{}] is exist" , oldName, newName);
            flag = false;
        }
        return flag;
    }


    // 单元测试
    /*public static void main(String[] args) throws SftpException, IOException {
        SFTPConfigModel sftpConfigModel = new SFTPConfigModel().getDefaultConfig();
        SFTPUtil sftp = new SFTPUtil(sftpConfigModel);
        sftp.login();

        File file = new File("D:\\inbound\\NCOMP_STHO_20201103_03_0001_CONTROL.txt");
        InputStream is = new FileInputStream(file);
        sftp.delete(sftpConfigModel.getUploadUrl(),"NCOMP_STHO_20201103_03_0001_CONTROL.txt");
        sftp.upload(sftpConfigModel.getUploadUrl(), "NCOMP_STHO_20201103_03_0001_CONTROL.txt", is);
        sftp.logout();
    }*/

}

