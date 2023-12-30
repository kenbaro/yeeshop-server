package com.yeeshop.yeeserver.domain.dto.common;

import java.util.List;

import com.yeeshop.yeeserver.domain.model.YeeMessage;

import lombok.Data;


/**
 * A common Data Transform Object to show Message
 *
 * @author Thai Duy Bao
 * @since 2023
 */
@Data
public class YeeMessageDto {

	/** Error Flag. */
	private Boolean isError = false;
	
	/** List of Message*/
    private List<YeeMessage> messages;
}
