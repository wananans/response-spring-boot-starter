package cn.wanans.response.advice;

import cn.wanans.response.annotation.wrap.NotWrapResponseBody;
import cn.wanans.response.utils.Res;
import cn.wanans.response.utils.WebUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Objects;

/**
 * @author w
 * @since 2022-09-27
 */
@Order(2)
@ConditionalOnWebApplication
@RestControllerAdvice
public class WrapResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {

        if (returnType.getParameterType().isAssignableFrom(Void.TYPE) ||
                returnType.hasMethodAnnotation(NotWrapResponseBody.class) ||
                returnType.getParameterType().isAssignableFrom(Res.class)) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        HttpServletResponse httpServletResponse = WebUtils.getResponse();
        //如果请求正常，直接返回
        if (Objects.equals(200, httpServletResponse.getStatus())) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

            // String类型不能直接包装
            if (returnType.getGenericParameterType().equals(String.class)) {
                try {
                    return objectMapper.writeValueAsString(Res.ok(body));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
                return Res.ok(body);
        }

        //否则直接返回
        return body;
    }
}
