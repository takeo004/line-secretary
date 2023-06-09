package com.line.secretary.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * コントローラクラスの基底クラス<br>
 * 新しくコントローラを作成する場合は、以下の2つのenumに区分を追加してください<br>
 * @see com.line.secretary.api.constant.MethodType
 * @see com.line.secretary.api.constant.MethodDetailType
 * 
 * {@link com.line.secretary.api.handler.LineMessageWebhookHandler} でリフレクションしているのだけど、少し無理やり感が強いからほかの方法があれば修正。。
 */
public abstract class BaseController {
    protected <E> E generateRequest(Class<E> requestClass, String chatGptResponse) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        return (E) mapper.readValue(chatGptResponse, requestClass);
    };
}
