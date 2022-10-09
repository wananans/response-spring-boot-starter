package cn.wanans.response.annotation.desensitize;

import cn.wanans.response.enums.DesensitizeTypeEnum;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author w
 * @since 2022-09-16
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
@Inherited
public @interface Desensitize {

    DesensitizeTypeEnum type();
}
