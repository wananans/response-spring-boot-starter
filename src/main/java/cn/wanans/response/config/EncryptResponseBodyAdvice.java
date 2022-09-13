package cn.wanans.response.config;

import cn.wanans.response.annotation.EnAndDecrypt;
import cn.wanans.response.annotation.Encrypt;
import cn.wanans.response.annotation.NotEncrypt;
import cn.wanans.response.properties.EncryptProperties;
import cn.wanans.response.utils.AESUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Objects;

/**
 * 接口加密
 *
 * @author w
 * @since 2022-09-13
 */
@Order(1)
@EnableConfigurationProperties(EncryptProperties.class)
@ConditionalOnProperty(prefix = "spring.response.encrypt", value = "enabled", havingValue = "true")
@RestControllerAdvice(annotations = RestController.class)
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Autowired
    private EncryptProperties encryptProperties;


    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {

        //如果返回为空，不进行加密
        if (returnType.getParameterType().isAssignableFrom(Void.TYPE)) {
            return Boolean.FALSE;
        }

        //如果为true，为加密模式
        if (encryptProperties.getModel()) {
            return !returnType.hasMethodAnnotation(NotEncrypt.class);
        } else {
            //如果方法有@Encrypt或@EnAndDecrypt注解，表示该方法需要加密
            return returnType.hasMethodAnnotation(Encrypt.class) || returnType.hasMethodAnnotation(EnAndDecrypt.class);
        }
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        //获取解密秘钥
        byte[] keyBytes = encryptProperties.getKey().getBytes();

        String encrypt = null;

        try {
            if (Objects.nonNull(body)) {
                if (body instanceof String) {
                    //如果返回是String类型，直接加密
                    encrypt = AESUtil.encrypt(body.toString().getBytes(StandardCharsets.UTF_8), keyBytes);
                } else {
                    //如果返回是对象类型，先转换为json再加密
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                    encrypt = AESUtil.encrypt(objectMapper.writeValueAsString(body).getBytes(StandardCharsets.UTF_8), keyBytes);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return encrypt;
    }
}
