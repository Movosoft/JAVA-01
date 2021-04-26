package com.movo.rpc.core.server.register;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @description 服务持有对象，保存具体的服务信息
 * @author Movo
 * @create 2021/3/31 14:44
 */
@AllArgsConstructor
@Getter
@Setter
public class ServiceObject {
    /**
     * 服务名称
     */
    private String name;

    /**
     * 服务Class
     */
    private Class<?> clazz;

    /**
     * 具体的服务对象
     */
    private Object obj;
}
