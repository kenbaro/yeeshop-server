package com.yeeshop.yeeserver.domain.exeption;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper=false)
public class AppException extends RuntimeException{
    /** serialVersionUID. */
	private static final long serialVersionUID = 1L;
	private String message;

    public AppException(String message) {
       this.message = message;
    }
}
