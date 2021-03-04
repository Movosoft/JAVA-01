package com.movo.insertdata.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingAddress {
    private int saId;
    private int buyerId;
    private String address;
}
