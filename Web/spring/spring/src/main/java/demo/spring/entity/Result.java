package demo.spring.entity;
import java.io.Serializable;

public class Result implements Serializable {

    private static final long serialVersionUID = -3948389268046368059L;

    private Integer code;

    private String msg;

    private Object data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    //成功 不返回数据直接返回成功信息
    public static Result success(String m,Object d) {
        Result result = new Result();
        result.setCode(0);
        result.setMsg(m);
        result.setData(d);
        return result;
    }

    public static Result fail(String m,Object d) {
        Result result = new Result();
        result.setCode(1);
        result.setMsg(m);
        result.setData(d);
        return result;
    }
}