package demo.spring.controller;

/**
 * 构造SFTP连接的配置类
 * @Author:QY
 */
public class SFTPConfigModel {

    /** FTP 登录用户名*/
    private String userName;
    /** FTP 登录密码*/
    private String passWord;
    /** 私钥 */
    private String privateKey;
    /** FTP 服务器地址IP地址*/
    private String host;
    /** FTP 端口*/
    private int port;
    /** FTP 指定上传路径*/
    private String uploadUrl;
    /** FTP 指定下载路径*/
    private String downloadUrl;

    public SFTPConfigModel(String userName, String passWord, String privateKey, String host, int port, String uploadUrl, String downloadUrl) {
        this.userName = userName;
        this.passWord = passWord;
        this.privateKey = privateKey;
        this.host = host;
        this.port = port;
        this.uploadUrl = uploadUrl;
        this.downloadUrl = downloadUrl;
    }

    public SFTPConfigModel(String userName, String passWord, String privateKey, String host, int port) {
        this.userName = userName;
        this.passWord = passWord;
        this.privateKey = privateKey;
        this.host = host;
        this.port = port;
    }

    public SFTPConfigModel(String userName, String passWord, String host, int port) {
        this.userName = userName;
        this.passWord = passWord;
        this.host = host;
        this.port = port;
    }

    public SFTPConfigModel(){}


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }


    // -------------------------------------------------------------------------------
    public SFTPConfigModel userName(String userName){
        this.userName = userName;
        return this;
    }

    public SFTPConfigModel passWord(String passWord){
        this.passWord = passWord;
        return this;
    }

    public SFTPConfigModel privateKey(String privateKey){
        this.privateKey = privateKey;
        return this;
    }

    public SFTPConfigModel host(String host){
        this.host = host;
        return this;
    }

    public SFTPConfigModel port(int port){
        this.port = port;
        return this;
    }

    /**
     * 默认配置，本人写着测试的。大家需要自己配
     * @return
     */
    public SFTPConfigModel getDefaultConfig(){
        this.userName = "root";
        this.passWord = "scentvh7Inckie";
        this.host = "124.220.207.224";
        this.port = 22;
        this.uploadUrl = "/home/file/upload";
        return this;
    }


    @Override
    public String toString() {
        return "SFTPConfigModel{" +
                "userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", uploadUrl='" + uploadUrl + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                '}';
    }
}


