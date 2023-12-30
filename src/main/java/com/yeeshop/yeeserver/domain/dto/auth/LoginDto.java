package com.yeeshop.yeeserver.domain.dto.auth;

import com.yeeshop.yeeserver.domain.dto.common.BearerToken;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * A Data Transfer Object for Login.
 * 
 * @author Thai Duy Bao.
 * since 2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@ToString
public class LoginDto {
	
    /** email. */
    private String email;
    
    /** password. */
    private String passWord;
    
    // Full Name.
    private String fullNm;
    
	/** Yee Token Model. */
	private BearerToken bearerToken;
	
	private String role;
	
	private Integer typeUser;
     
}
