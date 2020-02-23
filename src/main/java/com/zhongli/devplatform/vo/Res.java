package com.zhongli.devplatform.vo;


import lombok.Data;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Data
public class Res {

    private int code;
    private String msg;
    private Date timestamp = new Date();
    private Object data;

    public static int SERVICE_ERROR_CODE = 208;


    public Res(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Res(Object data) {
        this.code = HttpServletResponse.SC_OK;
        this.data = data;
    }

    public Res(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Res error(String msg) {
        return new Res(SERVICE_ERROR_CODE, msg);
    }


}
