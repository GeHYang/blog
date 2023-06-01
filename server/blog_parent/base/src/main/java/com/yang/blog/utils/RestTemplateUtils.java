package com.yang.blog.utils;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author: Yang
 * @create: 2023-04-26
 * @Description:
 */
@Component
public class RestTemplateUtils<T> {

    @Resource
    private RestTemplate restTemplateBean;
    public static RestTemplate restTemplate;

    @PostConstruct
    private void init(){
        restTemplate = restTemplateBean;
    }

    public static <T, A>T get(String url, A requestData, ParameterizedTypeReference<T> responseType){

        HttpEntity<A> entity = new HttpEntity<>(requestData);

        ResponseEntity<T> exchange = restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
        return exchange.getBody();
    }

    public static <T, A>T postFile(String url, A requestData, ParameterizedTypeReference<T> responseType){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<A> entity = new HttpEntity<>(requestData, headers);

        ResponseEntity<T> exchange = restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
        return exchange.getBody();
    }

    public static <T, A>T req(String url, A requestData, HttpMethod method, HttpHeaders headers, ParameterizedTypeReference<T> responseType){
        HttpEntity<A> entity = new HttpEntity<>(requestData, headers);

        ResponseEntity<T> exchange = restTemplate.exchange(url, method, entity, responseType);
        return exchange.getBody();
    }

}
