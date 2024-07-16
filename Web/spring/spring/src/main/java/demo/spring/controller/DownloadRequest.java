package demo.spring.controller;

public class DownloadRequest {
    String serverfile;
    String savefile;

    public String getServerfile() {
        return serverfile;
    }

    public void setServerfile(String serverfile) {
        this.serverfile = serverfile;
    }

    public String getSavefile() {
        return savefile;
    }

    public void setSavefile(String savefile) {
        this.savefile = savefile;
    }
}
