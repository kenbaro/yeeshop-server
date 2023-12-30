package com.yeeshop.yeeserver.app.form;

import lombok.Data;

@Data
public class Oauth2RequestForm {

	private String code;
    private String state;
}
