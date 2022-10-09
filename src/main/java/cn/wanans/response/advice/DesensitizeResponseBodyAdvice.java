package cn.wanans.response.advice;

import cn.wanans.response.annotation.desensitize.Desensitize;
import cn.wanans.response.annotation.desensitize.DesensitizeSupport;
import cn.wanans.response.enums.DesensitizeTypeEnum;
import cn.wanans.response.utils.DesensitizeUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author w
 * @date 2022/8/30
 */
@Order(0)
@RestControllerAdvice
public class DesensitizeResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        Method method = returnType.getMethod();
        DesensitizeSupport clazzSup = method.getDeclaringClass().getAnnotation(DesensitizeSupport.class);
        return returnType.hasMethodAnnotation(DesensitizeSupport.class) || clazzSup != null;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        List filedValue = null;
        try {
            if (body instanceof List) {
                filedValue = (List) body;
                //对值进行脱敏
                for (Object obj : filedValue) {
                    dealValue(obj);
                }
            }
            dealValue(body);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return body;
    }

    public void dealValue(Object obj) throws IllegalAccessException {
        Class<?> clazz = obj.getClass();
        //获取奔雷和父类的属性
        List<Field> fieldList = getAllFields(clazz);
        for (Field field : fieldList) {
            //获取属性上的注解
            Desensitize annotation = field.getAnnotation(Desensitize.class);
            if (annotation == null) {
                continue;
            }
            Class<?> type = field.getType();
            //判断属性的类型
            if (String.class != type) {
                //非字符串的类型 直接返回
                continue;
            }
            //获取脱敏类型  判断是否脱敏
            DesensitizeTypeEnum annotType = annotation.type();
            field.setAccessible(true);
            String oldValue = (String) field.get(obj);
            String newVal = DesensitizeUtils.dataMasking(annotType, oldValue);
            field.set(obj, newVal);
        }
    }

    /**
     * 获取所有的字段（包括父类的）
     * @param clazz
     * @return
     */
    public List<Field> getAllFields(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            Field[] declaredFields = clazz.getDeclaredFields();
            fieldList.addAll(Arrays.asList(declaredFields));
            //获取父类，然后获取父类的属性
            clazz = clazz.getSuperclass();
        }
        return fieldList;
    }




}
