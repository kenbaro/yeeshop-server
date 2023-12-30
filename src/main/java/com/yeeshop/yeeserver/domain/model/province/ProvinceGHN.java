package com.yeeshop.yeeserver.domain.model.province;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProvinceGHN {

	private int code;
    private String message;
    private List<ProvinccGHNData> data;
}
