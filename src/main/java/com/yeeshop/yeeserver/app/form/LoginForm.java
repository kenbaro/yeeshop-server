package com.yeeshop.yeeserver.app.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * A Form Object for Login.
 * 
 * @author Thai Duy Bao.
 * since 2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@ToString
public class LoginForm {
	
	/** email. */
    private String email;
    
    /** password. */
    private String passWord;
    
    // Full Name.
    private String fullNm;

}
