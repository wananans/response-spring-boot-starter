package cn.wanans.response.enums;

import cn.wanans.response.exception.ResCode;
import lombok.AllArgsConstructor;

/**
 * 常用提示信息枚举
 *
 * @author w
 * @since 2022-09-15
 */
@AllArgsConstructor
public enum CommonCodeEnum implements ResCode {

    /**
     * Basic
     */
    SUCCESS(200,"success"),
    FAIL(500,"fail"),


    /**
     * 请求异常
     */
    REQUEST_METHOD_NOT_SUPPORT_ERROR(1003, "请求方式错误"),
    REQUEST_PARAM_MISS_ERROR(1002, "请求参数未找到"),
    REQUEST_VALID_PARAM_ERROR(1003, "入参校验异常"),
    ;


    private final Integer code;

    private final String message;

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
