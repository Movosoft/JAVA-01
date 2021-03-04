package com.movo.insertdata.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Dictionary {
    private int dictionaryId;
    private int dictionaryCategoryId;
    private String dictionaryName;
    private int dictionaryOrder;
}
