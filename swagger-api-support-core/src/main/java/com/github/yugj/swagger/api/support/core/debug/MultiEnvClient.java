package com.github.yugj.swagger.api.support.core.debug;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

/**
 * 多环境调用客户端
 * @author yugj
 * @date 2019/9/19 上午9:08.
 */
public class MultiEnvClient {

    private static final String CUSTOM_SERVER_URL_HEADER = "sv-host";

    private static RestTemplate TEMPLATE = null;

    static {
        TEMPLATE = new RestTemplate();

        TEMPLATE.getMessageConverters().forEach(httpMessageConverter -> {
            if(httpMessageConverter instanceof StringHttpMessageConverter){
                ((StringHttpMessageConverter) httpMessageConverter).setDefaultCharset(StandardCharsets.UTF_8);
            }
        });
    }
    private String serverUrl;

    public MultiEnvClient(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String doPost(String body) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Enumeration<String> headers =  request.getHeaderNames();

        String customServerUrl = request.getHeader(CUSTOM_SERVER_URL_HEADER);
        String baseUrl = null;
        if (customServerUrl == null) {
            baseUrl = serverUrl;
        } else {
            baseUrl = customServerUrl;
        }

        String context = request.getRequestURI();
        String remoteUri = baseUrl + context;

        HttpHeaders requestHeaders = new HttpHeaders();
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            String headerValue = request.getHeader(header);
            requestHeaders.add(header, headerValue);
        }

        HttpEntity<Object> requestEntity = new HttpEntity<>(body, requestHeaders);
        ResponseEntity<String> response = TEMPLATE.exchange(remoteUri, HttpMethod.POST, requestEntity, String.class);

        return response.getBody();
    }
}
