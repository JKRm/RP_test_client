package cn.ac.iie.model;

/**
 * Created by wangyang on 14/12/31.
 */
public class RequestHeader {
    private String appid;
    private Version upv;
    private String op;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public Version getUpv() {
        return upv;
    }

    public void setUpv(Version upv) {
        this.upv = upv;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    class Version{
        int mj;
        int mn;
    }
}
