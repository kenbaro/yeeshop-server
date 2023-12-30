package com.yeeshop.yeeserver.domain.dto.common;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Yee Token Model.
 * 
 * @author Thai Duy Bao.
 * since 2023
 */
@Data
@Getter @Setter
public class BearerToken {
	
	/** Access Token. */
	private String accessToken;
	
	/** Type of Token. */
	private String tokenType;

	public BearerToken(String accessToken , String tokenType) {
        this.tokenType = tokenType ;
        this.accessToken = accessToken;
    }
}
