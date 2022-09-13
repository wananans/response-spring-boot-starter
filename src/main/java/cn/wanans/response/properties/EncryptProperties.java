package cn.wanans.response.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author w
 * @since 2022-09-13
 */
@ConfigurationProperties(prefix = "spring.response.encrypt")
@Data
public class EncryptProperties {

    private final static Boolean ENABLED = false;

    private final static Boolean DEFAULT_MODEL = false;

    private final static String DEFAULT_KEY = "w7sfxc!x&o8r@eoN";


    private final static String ENCRYPT_HEADER = "s-request-encrypt";


    /**
     * 是否开始参数加解密
     */
    private Boolean enabled = ENABLED;

    /**
     * 模式
     * true：加密模式（全局加密）
     * false：不加密模式（默认）
     */
    private Boolean model = DEFAULT_MODEL;

    /**
     * 秘钥
     */
    private String key = DEFAULT_KEY;

    /**
     * 请求头名称
     * 如果带了此请求头并且值为true，表示前端参数需要解密
     */
    private String encryptHeader = ENCRYPT_HEADER;
}
