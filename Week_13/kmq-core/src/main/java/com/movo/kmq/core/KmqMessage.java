package com.movo.kmq.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
 * @author Movo
 * @create 2021/5/11 18:53
 */
@AllArgsConstructor
@Getter
@Setter
public class KmqMessage<T> {
    private HashMap<String, Object> headers;
    private T body;
}
