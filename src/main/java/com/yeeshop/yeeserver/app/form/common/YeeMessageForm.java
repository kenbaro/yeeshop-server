package com.yeeshop.yeeserver.app.form.common;

import java.util.List;

import com.yeeshop.yeeserver.domain.model.YeeMessage;

import lombok.Data;


/**
 * A common Form Object to show Message
 *
 * @author Thai Duy Bao
 * @since 2023
 */
@Data
public class YeeMessageForm {

	/** Error Flag. */
	private Boolean isError = false;
	
	/** List of Message*/
    private List<YeeMessage> messages;
}
