package cn.wanans.response.annotation.wrap;

import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author w
 * @since 2022-09-27
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ResponseBody
@Documented
public @interface NotWrapResponseBody {
}
