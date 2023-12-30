package com.yeeshop.yeeserver.domain.dto.auth;

import lombok.Data;

@Data
public class OAuth2RequestDto {

	private String code;
    private String client_id;
    private String client_secret;
    private String redirect_uri;
    private String grant_type;
}
