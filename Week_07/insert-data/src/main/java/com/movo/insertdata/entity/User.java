package com.movo.insertdata.entity;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class User {
    private int userId;
    private String username;
    private String password;
    private String nickname;
    private String idCardNo;
    private String mobile;
    private Timestamp lastLoginTime;
    private Timestamp createTime;
    private Timestamp lastUpdateTime;
    private Boolean deleteTag;
}
