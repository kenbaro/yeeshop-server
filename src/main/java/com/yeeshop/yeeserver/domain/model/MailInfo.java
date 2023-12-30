package com.yeeshop.yeeserver.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailInfo {

	private String from;
	private String to;
	private String subject;
	private String body;
	private String attachments;

	public MailInfo(String to, String subject, String body) {
		this.from = "XiaoYi Shop <nptkeii@gmail.com>";
		this.to = to;
		this.subject = subject;
		this.body = body;
	}
}
