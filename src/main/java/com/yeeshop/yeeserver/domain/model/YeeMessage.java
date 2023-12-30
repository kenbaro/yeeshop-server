package com.yeeshop.yeeserver.domain.model;

import lombok.Data;


/**
 * Model for Message.
 *	
 * @author Thai Duy Bao
 * @since 2023
 */
@Data
public class YeeMessage {
	
	/** MessageId. */
	private String messageId;
	
	/** MessageTitle. */
	private String messageTitle;
	
	/** MessageType. */
	private int messageType;
}
