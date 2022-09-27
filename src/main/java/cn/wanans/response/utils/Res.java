package cn.wanans.response.utils;

import cn.wanans.response.enums.CommonCodeEnum;
import cn.wanans.response.exception.ResCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 统一返回
 * @author w
 * @since 2022-09-27
 */
@Data
public class Res <T>{

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String message;

    /**
     * 时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh_CN", timezone = "GMT+8")
    private Date dateTime;

    /**
     * 返回数据
     */
    private T data;


    public static <T> Res<T> ok(T t) {
        Res<T> res = new Res<>();
        res.setCode(CommonCodeEnum.SUCCESS.getCode());
        res.setMessage(CommonCodeEnum.SUCCESS.getMessage());
        res.setData(t);
        res.setDateTime(new Date());
        return res;
    }

    public static <T> Res<T> ok(T t, ResCode resCode) {
        Res<T> res = new Res<>();
        res.setCode(resCode.getCode());
        res.setMessage(resCode.getMessage());
        res.setData(t);
        res.setDateTime(new Date());
        return res;
    }

    public static <T> Res<T> fail(String message) {
        Res<T> res = new Res<>();
        res.setCode(CommonCodeEnum.FAIL.getCode());
        res.setMessage(message);
        res.setData(null);
        res.setDateTime(new Date());
        return res;
    }

    public static <T> Res<T> fail(Integer code, String message) {
        Res<T> res = new Res<>();
        res.setCode(code);
        res.setMessage(message);
        res.setData(null);
        res.setDateTime(new Date());
        return res;
    }

    public static <T> Res<T> fail(ResCode resCode) {
        Res<T> res = new Res<>();
        res.setCode(resCode.getCode());
        res.setMessage(resCode.getMessage());
        res.setData(null);
        res.setDateTime(new Date());
        return res;
    }
}
