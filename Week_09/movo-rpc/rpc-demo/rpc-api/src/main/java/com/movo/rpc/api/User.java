package com.movo.rpc.api;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户POJO样例
 * @author Movo
 * @create 2021/4/21 9:03
 */
@Getter
@Setter
public class User implements Serializable {
    private Integer userId;
    private String username;
}
