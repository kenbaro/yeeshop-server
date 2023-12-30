package com.yeeshop.yeeserver.domain.security.oauth2;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;

import java.util.List;
import java.util.Optional;


import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import com.yeeshop.yeeserver.domain.constant.YeeCommonConst;
import com.yeeshop.yeeserver.domain.entity.Role;
import com.yeeshop.yeeserver.domain.entity.User;
import com.yeeshop.yeeserver.domain.repository.IRoleRepository;
import com.yeeshop.yeeserver.domain.security.CustomUserDetails;
import com.yeeshop.yeeserver.domain.service.IOAuth2UserService;

@Component
public class CustomOAuth2UserService extends DefaultOAuth2UserService  {

	    private final IOAuth2UserService userService;
	    private final IRoleRepository roleRepository;

	    private final List<OAuth2UserInfoExtractor> oAuth2UserInfoExtractors;

	    public CustomOAuth2UserService(IOAuth2UserService userService, List<OAuth2UserInfoExtractor> oAuth2UserInfoExtractors, IRoleRepository roleRepository) {
	        this.userService = userService;
	        this.oAuth2UserInfoExtractors = oAuth2UserInfoExtractors;
	        this.roleRepository = roleRepository;
	    }

	    @Override
	    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
	        OAuth2User oAuth2User = super.loadUser(userRequest);

	        Optional<OAuth2UserInfoExtractor> oAuth2UserInfoExtractorOptional = oAuth2UserInfoExtractors.stream()
	                .filter(oAuth2UserInfoExtractor -> oAuth2UserInfoExtractor.accepts(userRequest))
	                .findFirst();
	        if (oAuth2UserInfoExtractorOptional.isEmpty()) {
	            throw new InternalAuthenticationServiceException("The OAuth2 provider is not supported yet");
	        }

	        CustomUserDetails  customUserDetails = oAuth2UserInfoExtractorOptional.get().extractUserInfo(oAuth2User);
	        User user = upsertUser(customUserDetails);
	        customUserDetails.setId(user.getUserCd());
	        return customUserDetails;
	    }

	    public User upsertUser(CustomUserDetails customUserDetails) {
	        Optional<User> userOptional = userService.getUserByEmail(customUserDetails.getEmail());
	        User user;
	        if (userOptional.isEmpty()) {
	            user = new User();
	            user.setUserCd(customUserDetails.getId());
	            user.setUserNm(customUserDetails.getName());
	            user.setEmail(customUserDetails.getEmail());
	            user.setIsActivated(true);
	            //user.setImageUrl(customUserDetails.getAvatarUrl());
	            //user.setProvider(customUserDetails.getProvider());
	            Role role = this.roleRepository.findByRoleName(YeeCommonConst.YeeRole.USER_ROLE);
	            user.setRole(role);
	        } else {
	            user = userOptional.get();
	            user.setEmail(customUserDetails.getEmail());
	            //user.setImageUrl(customUserDetails.getAvatarUrl());
	        }
	        return userService.saveUser(user);
	    }
}
