package cn.wanans.response.advice;

import cn.wanans.response.annotation.crypt.Decrypt;
import cn.wanans.response.annotation.crypt.EnAndDecrypt;
import cn.wanans.response.properties.EncryptProperties;
import cn.wanans.response.utils.AESUtils;
import cn.wanans.response.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Objects;

/**
 * 参数解密
 *
 * @author w
 * @since 2022-09-22
 */
@Slf4j
@Order(0)
@EnableConfigurationProperties(EncryptProperties.class)
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = "spring.response.encrypt", value = "enabled", havingValue = "true")
@RestControllerAdvice
public class DecryptRequestBodyAdviceAdapter extends RequestBodyAdviceAdapter {

    @Autowired
    private EncryptProperties encryptProperties;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {

        String header = WebUtils.getRequest().getHeader(encryptProperties.getEncryptHeader());
        if (StringUtils.hasText(header) && Objects.equals("true", header.toLowerCase(Locale.ROOT))) {
            return true;
        }

        return methodParameter.hasMethodAnnotation(Decrypt.class) ||
                methodParameter.hasMethodAnnotation(EnAndDecrypt.class) ||
                methodParameter.hasParameterAnnotation(Decrypt.class);
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {

        byte[] body = new byte[inputMessage.getBody().available()];
        inputMessage.getBody().read(body, 0, body.length);

        try {
            byte[] decrypt = AESUtils.decrypt(body, encryptProperties.getKey().getBytes());

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decrypt);

            return new HttpInputMessage() {
                @Override
                public InputStream getBody() throws IOException {
                    return byteArrayInputStream;
                }

                @Override
                public HttpHeaders getHeaders() {
                    return inputMessage.getHeaders();
                }
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
