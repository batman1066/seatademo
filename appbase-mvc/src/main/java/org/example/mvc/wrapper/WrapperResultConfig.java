package org.example.mvc.wrapper;

import com.alibaba.fastjson.JSON;
import org.apache.http.entity.ContentType;
import org.example.common.RespBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 统一返回包装配置
 */
@Configuration
public class WrapperResultConfig implements HandlerMethodReturnValueHandler {


    @Resource
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    /**
     * 程序加载完毕后将配置的实例添加到 RequestMappingHandlerAdapter
     */
    @PostConstruct
    public void compare() {
        List<HandlerMethodReturnValueHandler> handlers = requestMappingHandlerAdapter.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> list = new ArrayList<>();
        list.add(this);
        // ！！！注意这里，需要将requestMappingHandlerAdapter 原有的返回值处理器添加进去
        if (handlers != null) {
            list.addAll(handlers);
        }
        requestMappingHandlerAdapter.setReturnValueHandlers(list);
    }

    /**
     * 判断是否要进行包装返回值
     * 类上没有@RestWrapper 注解，返回false, 则该类的所以方法都不进行包装
     * 类上加了@RestWrapper 注解，但方法上加了 @IgnoreWrapper 返回false 不进行包装
     *
     * @param methodParameter
     * @return
     */
    @Override
    public boolean supportsReturnType(MethodParameter methodParameter) {
        // 类上没有@RestWrapper 注解，直接返回false，不对返回值进行包装
        if (!methodParameter.getContainingClass().isAnnotationPresent(RestWrapper.class)) {
            return false;
        }
        // 方法上加了 @IgnoreWrapper 注解表示忽略包装该方法, 返回false
        return !methodParameter.hasMethodAnnotation(IgnoreWrapper.class);
    }

    /**
     * 处理返回值，进行包装！
     *
     * @param o
     * @param methodParameter
     * @param modelAndViewContainer
     * @param nativeWebRequest
     * @throws Exception
     */
    @Override
    public void handleReturnValue(Object o, MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception {

        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
        if (null == response) {
            return;
        }
        RespBean<?> respBean;
        if (o instanceof RespBean) {
            // 如果返回值类型是 RespBean 就直接转成RespBean
            respBean = (RespBean<?>) o;
        } else {
            // 否则将该返回值设置为 RespBean类的data属性
            respBean = new RespBean<>().ok(o);
        }
        modelAndViewContainer.setRequestHandled(true);
        // 设置响应格式
        response.setContentType(ContentType.APPLICATION_JSON.toString());
        Servlets.transfer(response, JSON.toJSONString(respBean).getBytes());
    }
}