package com.yeeshop.yeeserver.app.form;

import com.yeeshop.yeeserver.domain.dto.common.BearerToken;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


/** Auth Form to send response.
 * 
 * @author Thai Duy Bao.
 * @since
 */
@Data
@EqualsAndHashCode(callSuper=false)
@ToString
public class AuthResponseForm{
	
	private String email;
	
	private String fullNm;
	
	/** Yee Token Model. */
	private BearerToken bearerToken;
}
