package com.yeeshop.yeeserver.domain.dto.auth;

import lombok.Data;

@Data
public class OAuth2Body {
	
   private String access_token;
   private Integer expires_in;
   private String scope;
   private String token_type;
   private String id_token;
}
