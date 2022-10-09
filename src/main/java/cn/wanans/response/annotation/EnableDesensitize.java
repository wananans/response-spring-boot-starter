package cn.wanans.response.annotation;

import cn.wanans.response.advice.DesensitizeResponseBodyAdvice;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author w
 * @since 2022-09-16
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(DesensitizeResponseBodyAdvice.class)
public @interface EnableDesensitize {
}
