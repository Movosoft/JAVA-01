package com.movo.rpc.core.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
// rpc响应状态
public enum RpcStatusEnum {

    SUCCESS(200, "SUCCESS"),
    ERROR(500, "ERROR"),
    NOT_FOUND(404, "NOT FOUND");

    private int code;
    private String desc;
}
