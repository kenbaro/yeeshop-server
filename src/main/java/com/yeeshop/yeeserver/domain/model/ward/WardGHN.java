package com.yeeshop.yeeserver.domain.model.ward;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class WardGHN {

	private int code;
    private String message;
    
    private List<WardGHNData> data;
}
