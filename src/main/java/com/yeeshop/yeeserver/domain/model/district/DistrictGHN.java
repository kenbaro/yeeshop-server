package com.yeeshop.yeeserver.domain.model.district;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DistrictGHN {

	private int code;
    private String message;
    private List<DistrictGHNData> data;
}
