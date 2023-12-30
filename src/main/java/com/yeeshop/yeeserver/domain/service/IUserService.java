package com.yeeshop.yeeserver.domain.service;

import java.util.List;
import java.util.Optional;

import com.yeeshop.yeeserver.domain.dto.auth.InfoDto;
import com.yeeshop.yeeserver.domain.dto.auth.LoginDto;
import com.yeeshop.yeeserver.domain.dto.auth.PasswordDto;
import com.yeeshop.yeeserver.domain.dto.common.YeeMessageDto;
import com.yeeshop.yeeserver.domain.dto.signup.SignUpDto;
import com.yeeshop.yeeserver.domain.entity.Role;
import com.yeeshop.yeeserver.domain.entity.User;
import com.yeeshop.yeeserver.domain.model.YeeMessage;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Interface for User.
 * @author Thai Duy Bao
 * since 2023
 */
public interface IUserService {
	
  /**
   * Method for Authenticate.
   * 
   * @param loginDto - A Data Transfer Object for Login.
   * @param yeeMessageDto - A common Data Transform Object to show Message.
   */
   public String authenticate(LoginDto loginDto, YeeMessageDto yeeMessageDto);
   
   /**
    * Method for SignUp.
    * 
    * @param loginDto - A Data Transfer Object for Login.
    * @param yeeMessageDto - A common Data Transform Object to show Message.
    */
   public void register(SignUpDto signUpDto, YeeMessageDto yeeMessageDto);
   
   public String encodeStr(String str);
   
   Role saveRole(Role role);
   User saverUser (User user);
   
   public User validateUser();
   
   public Boolean existsByEmail(String email);
   
   public User findByEmail(String email);
   
   public InfoDto getUserInfo(String email);
   
   public Boolean changeUserInfo(InfoDto infoDto,String msg);
   public Boolean updateUserInfo(InfoDto infoDto,String msg);
   public String changePassword(PasswordDto pwDto);
   
   public Boolean changeAdminInfo(InfoDto infoDto,String msg);
   
   public Boolean updateAdminInfo(InfoDto infoDto,String msg);
   
   public Boolean authenAdmin(HttpServletRequest httpServletRequest);
   
   public void getAllUsers(List<InfoDto> infoUsers);
   
   public Boolean deleteUserByEmail(List<String> emails);
   
   public Boolean editActiveUserByAdmin(String email, String msg,Boolean isActive);
   
   public Boolean addNewUser(InfoDto infoDto,YeeMessage yeeMessage);
   public Boolean registNewUser(InfoDto infoDto,YeeMessage yeeMessage);
   public Boolean updateUserAdminInfo(InfoDto infoDto,YeeMessage yeeMessage);
}
