package com.movo.insertdata.entity;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class Wares {
    private String waresCode;
    private int waresVersion;
    private String waresName;
    private int waresCategory;
    private int waresPriceI;
    private int waresPriceD;
    private String waresDesc;
    private long waresImg;
    private Timestamp createTime;
    private Timestamp lastUpdateTime;
    private Timestamp deleteTag;
}
