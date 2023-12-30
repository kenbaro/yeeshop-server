package com.yeeshop.yeeserver.domain.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.yeeshop.yeeserver.domain.constant.YeeCommonConst.YeeNumber;
import com.yeeshop.yeeserver.domain.constant.YeeCommonConst.YeeRole;
import com.yeeshop.yeeserver.domain.dto.auth.InfoDto;
import com.yeeshop.yeeserver.domain.dto.auth.LoginDto;
import com.yeeshop.yeeserver.domain.dto.auth.PasswordDto;
import com.yeeshop.yeeserver.domain.dto.checkout.AddressDistrictDto;
import com.yeeshop.yeeserver.domain.dto.checkout.AddressProvinceDto;
import com.yeeshop.yeeserver.domain.dto.checkout.AddressWardDto;
import com.yeeshop.yeeserver.domain.dto.common.YeeMessageDto;
import com.yeeshop.yeeserver.domain.dto.signup.SignUpDto;
import com.yeeshop.yeeserver.domain.constant.YeeCommonConst;
import com.yeeshop.yeeserver.domain.constant.YeeCommonConst.YeeError;
import com.yeeshop.yeeserver.domain.entity.Role;
import com.yeeshop.yeeserver.domain.entity.User;
import com.yeeshop.yeeserver.domain.entity.UserAddress;
import com.yeeshop.yeeserver.domain.exeption.AppException;
import com.yeeshop.yeeserver.domain.model.YeeMessage;
import com.yeeshop.yeeserver.domain.repository.IRoleRepository;
import com.yeeshop.yeeserver.domain.repository.IUserAddressRepository;
import com.yeeshop.yeeserver.domain.repository.IUserRepository;
import com.yeeshop.yeeserver.domain.security.JwtUtilities;
import com.yeeshop.yeeserver.domain.service.IAddressApiService;
import com.yeeshop.yeeserver.domain.service.IUserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import com.yeeshop.yeeserver.domain.utils.YeeDateTimeUtils;
import com.yeeshop.yeeserver.domain.utils.YeeStringUtils;

/**
 * Service Class for User.
 * 
 * @author Thai Duy Bao
 * @since 2023
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService{

    private final AuthenticationManager authenticationManager ;
    private final IUserRepository iUserRepository ;
    private final IRoleRepository iRoleRepository ;
    private final PasswordEncoder passwordEncoder ;
    private final JwtUtilities jwtUtilities ;
    @Autowired
    private ModelMapper mapper;
    private final IUserAddressRepository userAddressRepository;

    @Autowired
    private IAddressApiService addressApiService;
	@Override
	public String authenticate(final LoginDto loginDto, final YeeMessageDto yeeMessageDto) {
		
		String token = YeeStringUtils.EMPTY;

		if (!validateLogin(loginDto, yeeMessageDto)) {
			
			return token;
		}
		
		User user = this.findByEmail(loginDto.getEmail());
		
		if (null == user) {
			
			//return token;
		} else if (!user.getIsActivated()) {
			
			// set Error Flag
			yeeMessageDto.setIsError(true);

			YeeMessage msg = new YeeMessage();

			// set Message :
			msg.setMessageId("KICHHOAT");
			msg.setMessageTitle("Tài khoản chưa kích hoạt! Bạn có muốn kích hoạt tài khoản ?");;
			msg.setMessageType(YeeError.ERROR);
			yeeMessageDto.getMessages().add(msg);
			
			return token;
		}

		Authentication authentication = null;
		try {
			authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                        loginDto.getEmail(),
	                        loginDto.getPassWord()
	                )
	        );
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
		} catch (Exception e) {
			
			// set Error Flag
			yeeMessageDto.setIsError(true);

			YeeMessage msg = new YeeMessage();

			// set Message :
			msg.setMessageTitle("Tài khoản Hoặc Mật khẩu không đúng !");;
			msg.setMessageType(YeeError.ERROR);
			yeeMessageDto.getMessages().add(msg);
		}
		
		if (null != authentication) {

			user = iUserRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("Tài khoản chưa được đăng ký"));
			if (YeeStringUtils.equals(loginDto.getRole(), user.getRole().getRoleName())) {
				
				List<String> rolesNames = new ArrayList<>();
		        rolesNames.add(user.getRole().getRoleName());
		        token = jwtUtilities.generateToken(user.getUsername(),rolesNames);
		        
		        // set UserName to DTO.
		        loginDto.setFullNm(user.getUserNm());
		        loginDto.setTypeUser(user.getTypeUser());
			} else {
				
				// set Error Flag
				yeeMessageDto.setIsError(true);

				YeeMessage msg = new YeeMessage();

				// set Message :
				msg.setMessageTitle("Bạn không có quyền truy cập vào trang này!");;
				msg.setMessageType(YeeError.ERROR);
				yeeMessageDto.getMessages().add(msg);
			}
		}
        
        return token;
	}

	@Override
	public Role saveRole(Role role) {

		return iRoleRepository.save(role);
	}

	@Override
	public User saverUser(User user) {

		return iUserRepository.save(user);
	}

    @Override
    public void register(final SignUpDto signUpDto, final YeeMessageDto yeeMessageDto) {
    	
    	if (!validateSignUp(signUpDto,yeeMessageDto)) { // if validate fail.
    		
    		return; 
    	}
    	
    	// Model for Message.
		YeeMessage msg = new YeeMessage();

    	// set error
        if(iUserRepository.existsByEmail(signUpDto.getEmail())) { 
        	
        	yeeMessageDto.setIsError(true);
        	
        	msg.setMessageTitle("Email này đã được đăng ký bởi tài khoản khác !");
        	msg.setMessageType(YeeError.ERROR);
        	
        	yeeMessageDto.getMessages().add(msg);
        }
        
        else { 
        	
        	User user = new User();
        	String userCd = YeeCommonConst.YeeRole.USER_ROLE.concat(YeeDateTimeUtils.convertCurrentTimeStampToStringOnlyNumber());
        	user.setUserCd(userCd);
            user.setEmail(signUpDto.getEmail());
            user.setUserNm(signUpDto.getFullName());
            user.setUserTel(signUpDto.getPhone());
            user.setUserPw(passwordEncoder.encode(signUpDto.getPassWord()));
            user.setIsActivated(false);

            try {

            	//By Default , he/she is a simple user
                Role role = iRoleRepository.findByRoleName(YeeCommonConst.YeeRole.USER_ROLE);

                user.setRole(role);
                
            	User savedUser = iUserRepository.save(user);
                
                if (null == savedUser) {
                	
                	yeeMessageDto.setIsError(true);

                	msg.setMessageTitle("Đã xảy ra lỗi trong quá trình đăng ký. Cảm phiền thực hiện đăng ký lại");
                	msg.setMessageType(YeeError.ERROR);
                	
                	yeeMessageDto.getMessages().add(msg);
                }
            }
            catch (Exception e) {
				
            	yeeMessageDto.setIsError(true);

            	msg.setMessageTitle("Đã xảy ra lỗi trong quá trình đăng ký từ phía máy chủ. Vui lòng thực hiện lại sau vài phút.");
            	msg.setMessageType(YeeError.ERROR);
            	
            	yeeMessageDto.getMessages().add(msg);
			}
            
        }
    }
    
	private Boolean validateLogin(final LoginDto loginDto, final YeeMessageDto yeeMessageDto) {
		
		// If List Error Message of DTO is null, set new List.
		if (null == yeeMessageDto.getMessages()) {
			
			yeeMessageDto.setMessages(new ArrayList<>());
		}
		
		// Model for Message.
		YeeMessage msg = new YeeMessage();
		
		// Get Email
		String email = loginDto.getEmail();
		
		// Check email is empty
		if (YeeStringUtils.isEmpty(email)) {
			
			// set Error Flag = true
			yeeMessageDto.setIsError(true);
			
			// set Title of Message
			msg.setMessageTitle("Email không được bỏ trống!");
			
			// set type of message
			msg.setMessageType(YeeError.ERROR);
			
			yeeMessageDto.getMessages().add(msg);

			return false;

		} else {
						
			Boolean chkEmail = YeeStringUtils.chkValidEmail(loginDto.getEmail());
			
			if (false == chkEmail) {
				
				// set Error Flag = true
				yeeMessageDto.setIsError(true);
				
				// set Title of Message
				msg.setMessageTitle("Email không đúng định dạng!");
				
				// set type of message
				msg.setMessageType(YeeError.ERROR);
				yeeMessageDto.getMessages().add(msg);

				return false;

			}
			
		}
		
		// Get Password
		String passWord = loginDto.getPassWord().toString();
		
		// Check password is Empty
		if (YeeStringUtils.isEmpty(passWord)) {
			
			// set Error Flag = true
			yeeMessageDto.setIsError(true);
			
			// set Message :
			msg.setMessageTitle("Mật khẩu không được bỏ trống!");;
			msg.setMessageType(YeeError.ERROR);
			yeeMessageDto.getMessages().add(msg);
			
			// return result;
			return false;

		} else if (YeeNumber.INT_6 > passWord.length() 		// check length of password is < 6
				|| passWord.length() > YeeNumber.INT_20) {  // and > 20

			// set Error Flag
			yeeMessageDto.setIsError(true);

			// set Message: 
			msg.setMessageTitle("Mật khẩu phải nằm trong khoảng 6 đến 20 ký tự!");;
			msg.setMessageType(YeeError.ERROR);
			yeeMessageDto.getMessages().add(msg);
			
			// return result;
			return false;

		} else if (YeeStringUtils.chkSpecialCharacter(passWord)) { // check password has special characters
		
			// set Error Flag
			yeeMessageDto.setIsError(true);

			// set Message:
			msg.setMessageTitle("Mật khẩu không được chứa ký tự đặc biệt");;
			msg.setMessageType(YeeError.ERROR);
			yeeMessageDto.getMessages().add(msg);
			
			// return result;
			return false;
		}
		return true;
	}
	
	private Boolean validateSignUp(final SignUpDto signUpDto, final YeeMessageDto yeeMessageDto) {
		
		// If List Error Message of DTO is null, set new List.
		if (null == yeeMessageDto.getMessages()) {
			
			yeeMessageDto.setMessages(new ArrayList<>());
		}
		
		// Model for Message.
		YeeMessage msg = new YeeMessage();

		if (YeeStringUtils.isEmpty(signUpDto.getFullName().toString())) {
			
			// set ErrFlg 
			yeeMessageDto.setIsError(true);
			
			// set message
			msg.setMessageTitle("Tên người dùng không được bỏ trống !");
			msg.setMessageType(YeeError.ERROR);
			yeeMessageDto.getMessages().add(msg);
			
			// return false;
			return false;
		}
		
		if (YeeStringUtils.isEmpty(signUpDto.getEmail())){
			
			// set ErrFlg 
			yeeMessageDto.setIsError(true);
			
			// set message
			msg.setMessageTitle("Email không được bỏ trống !");
			msg.setMessageType(YeeError.ERROR);
			yeeMessageDto.getMessages().add(msg);
			
			// return false;
			return false;
		} else {
			
			Boolean chkEmail = YeeStringUtils.chkValidEmail(signUpDto.getEmail());
			
			if (false == chkEmail) {
				
				// set Error Flag = true
				yeeMessageDto.setIsError(true);
				
				// set Title of Message
				msg.setMessageTitle("Email không đúng định dạng!");
				
				// set type of message
				msg.setMessageType(YeeError.ERROR);
				yeeMessageDto.getMessages().add(msg);

				return false;

			}
		}
		
		return true;
	}

	@Override
	public User validateUser() {
		
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (!YeeStringUtils.equals(YeeCommonConst.ANONYMOUS, authentication.getPrincipal().toString())) {
			
			Optional<User> userOptional = this.iUserRepository.findByEmail(authentication.getPrincipal().toString());
			
			return userOptional.orElseThrow(() -> new AppException("ERROR"));
		}
			
		return new User();

	}

	@Override
	public Boolean existsByEmail(String email) {

		return this.iUserRepository.existsByEmail(email);
	}

	@Override
	public User findByEmail(String email) {

		Optional<User> userOptional = this.iUserRepository.findByEmail(email);
		
		User user = userOptional.isPresent() ? userOptional.get() : null;
		
		return user;
	}

	@Override
	public String encodeStr(String str) {
		
		if (null == str || YeeStringUtils.isEmpty(str)) {
			
			return YeeStringUtils.EMPTY;
		}
		
		return this.passwordEncoder.encode(str);
	}

	@Override
	public InfoDto getUserInfo(String email) {
		
		InfoDto infoDto = new InfoDto();
		
		User user = null;
		if (email !=null && YeeStringUtils.isNotEmpty(email)) {
			
			user = this.findByEmail(email);
		} else {

			user = this.validateUser();
		}
		
		
		if (null != user) {
			
			infoDto.setFullNm(user.getUserNm());
			infoDto.setEmail(user.getEmail());
			infoDto.setPhone(user.getUserTel());
			
			infoDto.setUserAvatar(user.getUserAvatar());
			infoDto.setUserBirth(user.getUserBirth());
			infoDto.setGender(user.getGender());
			infoDto.setTypeUser(user.getTypeUser().toString());
			infoDto.setCreatedBy(user.getCreatedBy());
			infoDto.setCreatedTime(user.getCreatedTime());
			infoDto.setUpdatedTime(user.getUpdatedTime());
			infoDto.setUpdatedBy(user.getUpdatedBy());
			
			UserAddress address = this.userAddressRepository.findByUser(user);
			
			if (null != address) {

				infoDto.setProvinces(this.addressApiService.getProvinces());
				infoDto.setProvinceCd(address.getProvinceId());
				infoDto.setDistricts(this.addressApiService.getDistricts(infoDto.getProvinceCd()));
				infoDto.setDistrictCd(address.getDistrictId());
				infoDto.setWards(this.addressApiService.getWards(infoDto.getDistrictCd()));
				infoDto.setWardCd(address.getWardId());
				infoDto.setHamlet(address.getHamlet());
			}

			return infoDto;
		}

		return null;
	}

	@Override
	public String changePassword(PasswordDto pwDto) {
		
		if(YeeStringUtils.isEmpty(pwDto.getOldPassWord())) {
			
			return "Mật khẩu cũ không được trống";
		}
		
		if (YeeStringUtils.chkSpecialCharacter(pwDto.getOldPassWord())) {
			
			return "Mật khẩu cũ không chính xác";
		}
		
		if(YeeStringUtils.isEmpty(pwDto.getNewPassWord())) {
			
			return "Mật khẩu mới không được rỗng";
		}
		
		if (YeeNumber.INT_6 > pwDto.getNewPassWord().length() 		// check length of password is < 6
				|| pwDto.getNewPassWord().length() > YeeNumber.INT_20) {
			
			return "Mật khẩu phải nằm trong khoảng 6 đến 12 ký tự!";
		}		
		
		if (!YeeStringUtils.equals(pwDto.getNewPassWord(), pwDto.getConfirmPassWord())) {
			
			return "Mật khẩu xác nhận không khớp với mật khẩu mới";
		}
		
		if(YeeStringUtils.chkSpecialCharacter(pwDto.getNewPassWord())) {
			
			return "Mật khẩu mới không được chứa ký tự đặc biệt";
		}

		User user = this.validateUser();
		if (null != user.getUserCd()) {
			
			if (!this.passwordEncoder.matches(pwDto.getOldPassWord(),user.getPassword())) {
				
				return "Mật khẩu cũ không chính xác";
			}
			
			user.setUserPw(this.passwordEncoder.encode(pwDto.getNewPassWord()));
			
			user = this.saverUser(user);
			
			if (null == user) {
				
				return "Có lỗi xảy ra trong quá trình cập nhật. Vui lòng thử lại";
			}
		}
		
		return YeeStringUtils.EMPTY;
	}

	@Override
	public Boolean updateAdminInfo(InfoDto infoDto,String msg) {
		
		if(!YeeStringUtils.chkValidEmail(infoDto.getEmail())) {
			
			return false;
		} else if (!YeeStringUtils.isNumbericChk(infoDto.getPhone())) {
			
			return false;
		} else if(YeeStringUtils.isEmpty(infoDto.getFullNm())) {
			
			return false;
		}
		
		User user1 = this.validateUser();
		
		User user2 = this.findByEmail(infoDto.getEmail());
		
		User tempUser = this.findByEmail(infoDto.getEmail());
		if (YeeStringUtils.equals(user2.getRole().getRoleName(), YeeCommonConst.YeeRole.USER_ROLE)) {
			
			return false;
		}
		
		if (YeeStringUtils.equals(user1.getUserCd(), user2.getUserCd())) {
			
			user2.setUserNm(infoDto.getFullNm());
			user2.setUserTel(infoDto.getPhone());
			user2.setUserAvatar(infoDto.getUserAvatar());
			user2.setGender(infoDto.getGender());
			user2.setUserBirth(infoDto.getUserBirth());
			UserAddress userAddress = this.userAddressRepository.findByUser(user2);
			
			if (null == userAddress) {
				
				userAddress = new UserAddress();
				
				userAddress.setUAdrCd(user2.getUserCd()+"_ADRS");
				userAddress.setUser(user2);
				userAddress.setAdrType(1);
				userAddress.setProvinceId(infoDto.getProvinceCd());
				Boolean chkAddress = false;
				
				if (infoDto.getProvinces().size() > 0) {
					
					for(AddressProvinceDto provinceDto : infoDto.getProvinces()) {
						
						if (YeeStringUtils.equals(infoDto.getProvinceCd(), provinceDto.getProvinceID().toString())) {
							
							userAddress.setProvince(provinceDto.getProvinceName());
							chkAddress = true;
							break;
						}
					}
				}
				
				if (!chkAddress) {
					return chkAddress;
				}
				chkAddress = false;
				if (infoDto.getDistricts().size() > 0) {
					
					for(AddressDistrictDto districtDto : infoDto.getDistricts()) {
						
						if (YeeStringUtils.equals(districtDto.getDistrictID().toString(), infoDto.getDistrictCd())
								&& YeeStringUtils.equals(infoDto.getProvinceCd(), districtDto.getProvinceID().toString())) {
							
							userAddress.setDistrict(districtDto.getDistrictName());
							chkAddress = true;
							break;
						}
					}
				}
				
				if (!chkAddress) {
					return chkAddress;
				}
				chkAddress = false;
				
				if (infoDto.getWards().size() > 0) {
					
					for (AddressWardDto wardDto : infoDto.getWards()) {
						
						if (YeeStringUtils.equals(wardDto.getWardId().toString(), infoDto.getWardCd())
								&& YeeStringUtils.equals(infoDto.getDistrictCd(), wardDto.getDistrictId().toString())) {
							
							userAddress.setCommune(wardDto.getWardNm());
							chkAddress = true;
							break;
							
						}
					}
				}
				if (!chkAddress) {
					return chkAddress;
				}
			} else {
				
				userAddress.setProvinceId(infoDto.getProvinceCd());
				Boolean chkAddress = false;
				
				if (infoDto.getProvinces().size() > 0) {
					
					for(AddressProvinceDto provinceDto : infoDto.getProvinces()) {
						
						if (YeeStringUtils.equals(infoDto.getProvinceCd(), provinceDto.getProvinceID().toString())) {
							
							userAddress.setProvince(provinceDto.getProvinceName());
							chkAddress = true;
							break;
						}
					}
				}
				
				if (!chkAddress) {
					return chkAddress;
				}
				chkAddress = false;
				if (infoDto.getDistricts().size() > 0) {
					
					for(AddressDistrictDto districtDto : infoDto.getDistricts()) {
						
						if (YeeStringUtils.equals(districtDto.getDistrictID().toString(), infoDto.getDistrictCd())
								&& YeeStringUtils.equals(infoDto.getProvinceCd(), districtDto.getProvinceID().toString())) {
							
							userAddress.setDistrict(districtDto.getDistrictName());
							chkAddress = true;
							break;
						}
					}
				}
				
				if (!chkAddress) {
					return chkAddress;
				}
				chkAddress = false;
				
				if (infoDto.getWards().size() > 0) {
					
					for (AddressWardDto wardDto : infoDto.getWards()) {
						
						if (YeeStringUtils.equals(wardDto.getWardId().toString(), infoDto.getWardCd())
								&& YeeStringUtils.equals(infoDto.getDistrictCd(), wardDto.getDistrictId().toString())) {
							
							userAddress.setCommune(wardDto.getWardNm());
							chkAddress = true;
							break;
							
						}
					}
				}
				if (!chkAddress) {
					return chkAddress;
				}
			}
			
			userAddress.setHamlet(infoDto.getHamlet());
			user2 = this.saverUser(user2);
			
			if (null == user2) {
				
				return false;
			} else {

				userAddress = this.userAddressRepository.save(userAddress);
				if (null == userAddress) {
					
					user2 = this.saverUser(tempUser);
					return false;
					
				}
			
			}
		}
		
		return true;
	}

	@Override
	public Boolean authenAdmin(HttpServletRequest httpServletRequest) {
		
		String token = this.jwtUtilities.getToken(httpServletRequest);
		if (null != token) {
			
			User user = this.validateUser();
			if (null != user.getEmail() && null != user.getRole()) {
				
				if (YeeStringUtils.equals(YeeCommonConst.YeeRole.ADMIN_ROLE, user.getRole().getRoleName())
						&& YeeStringUtils.equals(this.jwtUtilities.extractUsername(token), user.getEmail()) 
						&& !this.jwtUtilities.isTokenExpired(token)) {
					
					return true;
				}
				
			}
		}
		return false;
	}

	@Override
	public Boolean changeAdminInfo(final InfoDto infoDto, String msg) {
		
		User user1 = this.validateUser();
		
		//User user2 = this.findByEmail(infoDto.getEmail());
		
		if (YeeStringUtils.equals(user1.getRole().getRoleName(), YeeCommonConst.YeeRole.USER_ROLE)) {
			
			return false;
		}
		
		InfoDto orginInfoDto = this.getUserInfo(infoDto.getEmail());
		
		if (null == orginInfoDto) {
			
			return false;
		}
		
		try {
			if (!infoDto.getInitState()) {
				
				if (YeeStringUtils.isNotEmpty(infoDto.getFullNm())) {
					
					orginInfoDto.setFullNm(infoDto.getFullNm());
				}
				
				if (YeeStringUtils.isNotEmpty(infoDto.getPhone())) {
					
					orginInfoDto.setPhone(infoDto.getPhone());
				}
				
				if (YeeDateTimeUtils.validateDateMinus(infoDto.getUserBirth())) {
					
					orginInfoDto.setUserBirth(infoDto.getUserBirth());
				}
				
				if (YeeStringUtils.isNotEmpty(infoDto.getGender())) {
					
					orginInfoDto.setGender(infoDto.getGender());
				}
				
				
				if (YeeStringUtils.isNotEmpty(infoDto.getUserAvatar())) {
					
					orginInfoDto.setUserAvatar(infoDto.getUserAvatar());
				}
				
				if (YeeStringUtils.isNotEmpty(infoDto.getHamlet())) {
					
					orginInfoDto.setHamlet(infoDto.getHamlet());
				}
				
				//orginInfoDto.setProvinces(this.addressApiService.getProvinces());
				
				if (YeeStringUtils.isNumbericChk(infoDto.getProvinceCd())) {
					
					orginInfoDto.setProvinceCd(infoDto.getProvinceCd());
					orginInfoDto.setDistricts(this.addressApiService.getDistricts(orginInfoDto.getProvinceCd()));
					
					if (YeeStringUtils.isNumbericChk(infoDto.getDistrictCd())) {
						
						orginInfoDto.setDistrictCd(infoDto.getDistrictCd());
					} else {
						
						orginInfoDto.setDistrictCd(orginInfoDto.getDistricts().get(0).getDistrictID().toString());
					}

					orginInfoDto.setWards(this.addressApiService.getWards(orginInfoDto.getDistrictCd()));
					
					if (YeeStringUtils.isNumbericChk(infoDto.getWardCd())) {
						
						orginInfoDto.setWardCd(infoDto.getWardCd());
					} else {
						
						orginInfoDto.setWardCd(orginInfoDto.getWards().get(0).getWardId());
					}
				}
			}
			

			this.mapper.map(orginInfoDto, infoDto);

		} catch (Exception e) {
			
			return false;
		}
		
		
		return true;
	}
	
	
	@Override
	public Boolean updateUserInfo(final InfoDto infoDto, String msg) {
		
		if(!YeeStringUtils.chkValidEmail(infoDto.getEmail())) {
			
			return false;
		} else if (!YeeStringUtils.isNumbericChk(infoDto.getPhone())) {
			
			return false;
		} else if(YeeStringUtils.isEmpty(infoDto.getFullNm())) {
			
			return false;
		}
		
		User user1 = this.validateUser();
		
		User user2 = this.findByEmail(infoDto.getEmail());
		
		User tempUser = this.findByEmail(infoDto.getEmail());
		
		if (YeeStringUtils.equals(user1.getUserCd(), user2.getUserCd())) {
			
			user2.setUserNm(infoDto.getFullNm());
			user2.setUserTel(infoDto.getPhone());
			user2.setUserAvatar(infoDto.getUserAvatar());
			user2.setGender(infoDto.getGender());
			user2.setUserBirth(infoDto.getUserBirth());
			UserAddress userAddress = this.userAddressRepository.findByUser(user2);
			
			if (null == userAddress) {
				
				userAddress = new UserAddress();
				
				userAddress.setUAdrCd(user2.getUserCd()+"_ADRS");
				userAddress.setUser(user2);
				userAddress.setAdrType(1);
				userAddress.setProvinceId(infoDto.getProvinceCd());
				Boolean chkAddress = false;
				
				if (infoDto.getProvinces().size() > 0) {
					
					for(AddressProvinceDto provinceDto : infoDto.getProvinces()) {
						
						if (YeeStringUtils.equals(infoDto.getProvinceCd(), provinceDto.getProvinceID().toString())) {
							
							userAddress.setProvince(provinceDto.getProvinceName());
							chkAddress = true;
							break;
						}
					}
				}
				
				if (!chkAddress) {
					return chkAddress;
				}
				chkAddress = false;
				if (infoDto.getDistricts().size() > 0) {
					
					for(AddressDistrictDto districtDto : infoDto.getDistricts()) {
						
						if (YeeStringUtils.equals(districtDto.getDistrictID().toString(), infoDto.getDistrictCd())
								&& YeeStringUtils.equals(infoDto.getProvinceCd(), districtDto.getProvinceID().toString())) {
							
							userAddress.setDistrict(districtDto.getDistrictName());
							chkAddress = true;
							break;
						}
					}
				}
				
				if (!chkAddress) {
					return chkAddress;
				}
				chkAddress = false;
				
				if (infoDto.getWards().size() > 0) {
					
					for (AddressWardDto wardDto : infoDto.getWards()) {
						
						if (YeeStringUtils.equals(wardDto.getWardId().toString(), infoDto.getWardCd())
								&& YeeStringUtils.equals(infoDto.getDistrictCd(), wardDto.getDistrictId().toString())) {
							
							userAddress.setCommune(wardDto.getWardNm());
							chkAddress = true;
							break;
							
						}
					}
				}
				if (!chkAddress) {
					return chkAddress;
				}
			} else {
				
				userAddress.setProvinceId(infoDto.getProvinceCd());
				Boolean chkAddress = false;
				
				if (infoDto.getProvinces().size() > 0) {
					
					for(AddressProvinceDto provinceDto : infoDto.getProvinces()) {
						
						if (YeeStringUtils.equals(infoDto.getProvinceCd(), provinceDto.getProvinceID().toString())) {
							
							userAddress.setProvince(provinceDto.getProvinceName());
							chkAddress = true;
							break;
						}
					}
				}
				
				if (!chkAddress) {
					return chkAddress;
				}
				chkAddress = false;
				if (infoDto.getDistricts().size() > 0) {
					
					for(AddressDistrictDto districtDto : infoDto.getDistricts()) {
						
						if (YeeStringUtils.equals(districtDto.getDistrictID().toString(), infoDto.getDistrictCd())
								&& YeeStringUtils.equals(infoDto.getProvinceCd(), districtDto.getProvinceID().toString())) {
							
							userAddress.setDistrict(districtDto.getDistrictName());
							chkAddress = true;
							break;
						}
					}
				}
				
				if (!chkAddress) {
					return chkAddress;
				}
				chkAddress = false;
				
				if (infoDto.getWards().size() > 0) {
					
					for (AddressWardDto wardDto : infoDto.getWards()) {
						
						if (YeeStringUtils.equals(wardDto.getWardId().toString(), infoDto.getWardCd())
								&& YeeStringUtils.equals(infoDto.getDistrictCd(), wardDto.getDistrictId().toString())) {
							
							userAddress.setCommune(wardDto.getWardNm());
							chkAddress = true;
							break;
							
						}
					}
				}
				if (!chkAddress) {
					return chkAddress;
				}
			}
			
			userAddress.setHamlet(infoDto.getHamlet());
			user2 = this.saverUser(user2);
			
			if (null == user2) {
				
				return false;
			} else {

				userAddress = this.userAddressRepository.save(userAddress);
				if (null == userAddress) {
					
					user2 = this.saverUser(tempUser);
					return false;
					
				}
			
			}
		}
		
		return true;
	}

	@Override
	public void getAllUsers(final List<InfoDto> infoUsers) {
		
		List<User> users = this.iUserRepository.findAll();
		
		User admin = this.validateUser();
		users.stream().forEach( user -> {
			
			if (null != user 
					&& !YeeStringUtils.equals(user.getEmail(), admin.getEmail())
					&& YeeStringUtils.equals(admin.getRole().getRoleName(), YeeCommonConst.YeeRole.ADMIN_ROLE)) {
				
				if (admin.getTypeUser() == 1) {
					
					
					InfoDto infoDto = new InfoDto();
					infoDto.setUserId(user.getUserCd());
					infoDto.setFullNm(user.getUserNm());
					infoDto.setEmail(user.getEmail());
					infoDto.setPhone(user.getUserTel());

					
					infoDto.setUserAvatar(user.getUserAvatar());
					infoDto.setUserBirth(user.getUserBirth());
					infoDto.setGender(user.getGender());
					
					infoDto.setRole(user.getRole().getRoleName());
					infoDto.setUserStatus(user.getIsActivated() ? "Đã kích hoạt" : "Chưa kích hoạt");
					
					if (YeeStringUtils.equals(admin.getRole().getRoleName(), YeeCommonConst.YeeRole.ADMIN_ROLE)) {
						
						if (user.getTypeUser() == 1) {
							
							infoDto.setTypeUser(" Siêu quản trị viên");

						} else if (user.getTypeUser() == 2) {
							
							infoDto.setTypeUser(" Quản trị viên");

						} else if (user.getTypeUser() == 5) {
							
							infoDto.setTypeUser("Khách hàng thân thiết");
						} else if (user.getTypeUser() == 4) {
							
							infoDto.setTypeUser("Khách hàng tiềm năng");
						} else if (user.getTypeUser() == 3) {
							
							infoDto.setTypeUser("Khách hàng");
						}
						
					}

					UserAddress address = this.userAddressRepository.findByUser(user);
					
					if (null != address) {

//						infoDto.setProvinceCd(address.getProvinceId());
//						infoDto.setDistricts(this.addressApiService.getDistricts(infoDto.getProvinceCd()));
//						infoDto.setDistrictCd(address.getDistrictId());
//						infoDto.setWards(this.addressApiService.getWards(infoDto.getDistrictCd()));
//						infoDto.setWardCd(address.getWardId());
//						infoDto.setHamlet(address.getHamlet());
					}
					infoUsers.add(infoDto);
				} else {
					
					if (!YeeStringUtils.equals(user.getRole().getRoleName(), YeeCommonConst.YeeRole.ADMIN_ROLE)) {
						
						InfoDto infoDto = new InfoDto();
						infoDto.setUserId(user.getUserCd());
						infoDto.setFullNm(user.getUserNm());
						infoDto.setEmail(user.getEmail());
						infoDto.setPhone(user.getUserTel());

						
						infoDto.setUserAvatar(user.getUserAvatar());
						infoDto.setUserBirth(user.getUserBirth());
						infoDto.setGender(user.getGender());
						
						infoDto.setRole(user.getRole().getRoleName());
						infoDto.setUserStatus(user.getIsActivated() ? "Đã kích hoạt" : "Chưa kích hoạt");
						
						if (YeeStringUtils.equals(admin.getRole().getRoleName(), YeeCommonConst.YeeRole.ADMIN_ROLE)) {
							
							if (user.getTypeUser() == 1) {
								
								infoDto.setTypeUser(" Siêu quản trị viên");

							} else if (user.getTypeUser() == 2) {
								
								infoDto.setTypeUser(" Quản trị viên");

							} else if (user.getTypeUser() == 5) {
								
								infoDto.setTypeUser("Khách hàng thân thiết");
							} else if (user.getTypeUser() == 4) {
								
								infoDto.setTypeUser("Khách hàng tiềm năng");
							} else if (user.getTypeUser() == 3) {
								
								infoDto.setTypeUser("Khách hàng");
							}
							
						} 
						UserAddress address = this.userAddressRepository.findByUser(user);
						
						if (null != address) {

							infoDto.setProvinceCd(address.getProvinceId());
							infoDto.setDistricts(this.addressApiService.getDistricts(infoDto.getProvinceCd()));
							infoDto.setDistrictCd(address.getDistrictId());
							infoDto.setWards(this.addressApiService.getWards(infoDto.getDistrictCd()));
							infoDto.setWardCd(address.getWardId());
							infoDto.setHamlet(address.getHamlet());
						}

						infoUsers.add(infoDto);
					}
				}
				
				
				
			}
		});
		
	}

	@Override
	public Boolean deleteUserByEmail(List<String> emails) {
		
		if(null == emails || 0 > emails.size()) {
			
			return false;
		}
		List<User> users = new ArrayList<>();

		emails.stream().forEach( email -> {
			
			if (!YeeStringUtils.chkValidEmail(email)) {
				
				return;
			}
			
			User user = this.findByEmail(email);
			
			if (null == user) {
				
				return;
			}

			users.add(user);
		});
		
		if (users.size() != emails.size()) {

			return false;
		}
		
		try {
			
			this.iUserRepository.deleteAll(users);

		} catch (Exception e) {
			
			return false;
		}
			
		return true;
	}

	@Override
	public Boolean editActiveUserByAdmin(String email, String msg, Boolean isActive) {

		String phrase = isActive ? "kích hoạt" : "vô hiệu hoá";
		if (isActive) {
			
			
		}
		if (!YeeStringUtils.chkValidEmail(email)) {
			
			msg="Không thể "+phrase+" tài khoản, vui lòng thử lại sau!";
			return false;
		}
		
		User user = this.findByEmail(email);
		
		if (null == user) {
			
			msg="Người dùng có email: " +email + " chưa được đăng ký hoặc đã bị xoá khỏi hệ thống!";
			return false;
		}
		
		User admin = this.validateUser();
		
		if (!YeeStringUtils.equals(admin.getRole().getRoleName(), YeeCommonConst.YeeRole.ADMIN_ROLE)) {
			
			msg="Không thể "+phrase+" tài khoản, vui lòng thử lại sau!";
			return false;
		}
		
		if (user.getIsActivated() == isActive) {
			
			msg="Tài khoản hiện đang ở trạng thái "+phrase+", yêu cầu " +phrase+" của bạn không được thực thi!";
			
		}
		user.setIsActivated(isActive);
		String updateDate = YeeDateTimeUtils.getCurrentDateTime();
		user.setUpdatedTime(updateDate);
		
		user.setUpdatedBy(admin.getEmail());
		
		user = this.iUserRepository.save(user);
		
		if (null == user) {
			
			msg="Không thể "+phrase+" tài khoản, vui lòng thử lại sau!";
			return false;
		}

		return true;
	}

	@Override
	public Boolean addNewUser(InfoDto infoDto,YeeMessage yeeMessage) {
		
		User admin = this.validateUser();
		
		if (null == admin || !YeeStringUtils.equals(admin.getRole().getRoleName(), YeeCommonConst.YeeRole.ADMIN_ROLE)) {

			return false;
		}
		
		if (!YeeStringUtils.chkValidEmail(infoDto.getEmail())) {
			
			return false;
		}
		
		if (YeeStringUtils.isEmpty(infoDto.getFullNm())) {
			
			return false;
		}
		
		if (!YeeDateTimeUtils.validateDateMinus(infoDto.getUserBirth())) {
			
			return false;
		}
		
		if (13 > YeeDateTimeUtils.calcYearsUntilNow(infoDto.getUserBirth())) {
			
			yeeMessage.setMessageTitle("Người dùng phải từ 13 tuổi trở lên");
			return false;
		}
		
		if (!YeeStringUtils.isNumbericChk(infoDto.getPhone())) {
			
			return false;
		}
		
		//if (!YeeStringUtils.chkSpecialCharacter(infoDto.getUs))
		
		if (!YeeStringUtils.isNumbericChk(infoDto.getGender())) {
			
			return false;
		}
		
		if (!YeeStringUtils.isNumbericChk(infoDto.getRole())) {
			
			return false;
		}
		
		if (!YeeStringUtils.isNumbericChk(infoDto.getProvinceCd())) {
			
			return false;
		}
		
		if (!YeeStringUtils.isNumbericChk(infoDto.getDistrictCd())) {
			
			return false;
		}
		
		if (!YeeStringUtils.isNumbericChk(infoDto.getWardCd())) {
			
			return false;
		}
		
		if (YeeStringUtils.isEmpty(infoDto.getHamlet())) {
			
			return false;
		}
		
		if (this.existsByEmail(infoDto.getEmail())) {
			
			yeeMessage.setMessageTitle("Email "+ infoDto.getEmail() + " đã được sử dụng!");
			return false;
		}
		
		User newUser = new User();
		
		String userCd = YeeStringUtils.EMPTY;
		String suffix = YeeDateTimeUtils.getCurrentDateTime(); 
		if (Integer.parseInt(infoDto.getRole()) < 3) {
			
			userCd = YeeRole.ADMIN_ROLE + YeeDateTimeUtils.convertDateTimeToStringOnlyNumber(suffix);
		} else {
			
			userCd = YeeRole.USER_ROLE+ YeeDateTimeUtils.convertDateTimeToStringOnlyNumber(suffix);
		}
		
		newUser.setUserCd(userCd);
		newUser.setEmail(infoDto.getEmail());
		newUser.setUserNm(infoDto.getFullNm());
		newUser.setUserBirth(infoDto.getUserBirth());
		newUser.setUserTel(infoDto.getPhone());
		
		String pw = this.encodeStr(infoDto.getPassWord());
		newUser.setUserPw(pw);
		newUser.setGender(infoDto.getGender());
		if (Integer.parseInt(infoDto.getRole()) < 3) {
			
			Role role = this.iRoleRepository.findByRoleName(YeeRole.ADMIN_ROLE);
			newUser.setRole(role);
		} else {
			
			Role role = this.iRoleRepository.findByRoleName(YeeRole.USER_ROLE);
			newUser.setRole(role);
		}
		newUser.setTypeUser(Integer.parseInt(infoDto.getRole()));
		
		newUser.setUserAvatar(infoDto.getUserAvatar());
		
		newUser.setIsActivated(true);
		
		newUser.setCreatedBy(admin.getEmail());
		
		newUser.setCreatedTime(suffix);
		
		UserAddress userAddress = new UserAddress();
		
		userAddress.setUAdrCd("UADRS"+YeeDateTimeUtils.convertDateTimeToStringOnlyNumber(suffix));
		userAddress.setUser(newUser);
		userAddress.setAdrType(1);
		userAddress.setProvinceId(infoDto.getProvinceCd());
		Boolean chkAddress = false;
		
		if (infoDto.getProvinces().size() > 0) {
			
			for(AddressProvinceDto provinceDto : infoDto.getProvinces()) {
				
				if (YeeStringUtils.equals(infoDto.getProvinceCd(), provinceDto.getProvinceID().toString())) {
					
					userAddress.setProvince(provinceDto.getProvinceName());
					chkAddress = true;
					break;
				}
			}
		}
		
		if (!chkAddress) {
			return chkAddress;
		}
		chkAddress = false;
		userAddress.setDistrictId(infoDto.getDistrictCd());
		if (infoDto.getDistricts().size() > 0) {
			
			for(AddressDistrictDto districtDto : infoDto.getDistricts()) {
				
				if (YeeStringUtils.equals(districtDto.getDistrictID().toString(), infoDto.getDistrictCd())
						&& YeeStringUtils.equals(infoDto.getProvinceCd(), districtDto.getProvinceID().toString())) {
					
					userAddress.setDistrict(districtDto.getDistrictName());
					chkAddress = true;
					break;
				}
			}
		}
		
		if (!chkAddress) {
			return chkAddress;
		}
		chkAddress = false;
		userAddress.setWardId(infoDto.getWardCd());
		if (infoDto.getWards().size() > 0) {
			
			for (AddressWardDto wardDto : infoDto.getWards()) {
				
				if (YeeStringUtils.equals(wardDto.getWardId().toString(), infoDto.getWardCd())
						&& YeeStringUtils.equals(infoDto.getDistrictCd(), wardDto.getDistrictId().toString())) {
					
					userAddress.setCommune(wardDto.getWardNm());
					chkAddress = true;
					break;
					
				}
			}
		}
		if (!chkAddress) {
			return chkAddress;
		}
		userAddress.setHamlet(infoDto.getHamlet());
		newUser = this.saverUser(newUser);
		
		if (null == newUser) {

			return false;
		} else {
			
			userAddress = this.userAddressRepository.save(userAddress);
			
			if (null == userAddress) {
				
				this.iUserRepository.delete(newUser);
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public Boolean registNewUser(InfoDto infoDto,YeeMessage yeeMessage) {
		
		if (!YeeStringUtils.chkValidEmail(infoDto.getEmail())) {
			
			return false;
		}
		
		if (YeeStringUtils.isEmpty(infoDto.getFullNm())) {
			
			return false;
		}
		
		if (!YeeDateTimeUtils.validateDateMinus(infoDto.getUserBirth())) {
			
			return false;
		}
		
		if (13 > YeeDateTimeUtils.calcYearsUntilNow(infoDto.getUserBirth())) {
			
			yeeMessage.setMessageTitle("Người dùng phải từ 13 tuổi trở lên");
			return false;
		}
		
		if (!YeeStringUtils.isNumbericChk(infoDto.getPhone())) {
			
			return false;
		}
		
		//if (!YeeStringUtils.chkSpecialCharacter(infoDto.getUs))
		
		if (!YeeStringUtils.isNumbericChk(infoDto.getGender())) {
			
			return false;
		}
		
		if (!YeeStringUtils.isNumbericChk(infoDto.getRole())) {
			
			return false;
		}
		
		if (!YeeStringUtils.isNumbericChk(infoDto.getProvinceCd())) {
			
			return false;
		}
		
		if (!YeeStringUtils.isNumbericChk(infoDto.getDistrictCd())) {
			
			return false;
		}
		
		if (!YeeStringUtils.isNumbericChk(infoDto.getWardCd())) {
			
			return false;
		}
		
		if (YeeStringUtils.isEmpty(infoDto.getHamlet())) {
			
			return false;
		}
		
		if (this.existsByEmail(infoDto.getEmail())) {
			
			yeeMessage.setMessageTitle("Email "+ infoDto.getEmail() + " đã được sử dụng!");
			return false;
		}
		
		User newUser = new User();
		
		String userCd = YeeStringUtils.EMPTY;
		String suffix = YeeDateTimeUtils.getCurrentDateTime(); 
		userCd = YeeRole.USER_ROLE+ YeeDateTimeUtils.convertDateTimeToStringOnlyNumber(suffix);
		
		newUser.setUserCd(userCd);
		newUser.setEmail(infoDto.getEmail());
		newUser.setUserNm(infoDto.getFullNm());
		newUser.setUserBirth(infoDto.getUserBirth());
		newUser.setUserTel(infoDto.getPhone());
		
		String pw = this.encodeStr(infoDto.getPassWord());
		newUser.setUserPw(pw);
		newUser.setGender(infoDto.getGender());
		Role role = this.iRoleRepository.findByRoleName(YeeRole.USER_ROLE);
		newUser.setRole(role);
		newUser.setTypeUser(Integer.parseInt(infoDto.getRole()));
		
		newUser.setUserAvatar(infoDto.getUserAvatar());
		
		newUser.setIsActivated(true);
		
		newUser.setCreatedBy(infoDto.getEmail());
		
		newUser.setCreatedTime(suffix);
		
		UserAddress userAddress = new UserAddress();
		
		userAddress.setUAdrCd("UADRS"+YeeDateTimeUtils.convertDateTimeToStringOnlyNumber(suffix));
		userAddress.setUser(newUser);
		userAddress.setAdrType(1);
		userAddress.setProvinceId(infoDto.getProvinceCd());
		Boolean chkAddress = false;
		
		if (infoDto.getProvinces().size() > 0) {
			
			for(AddressProvinceDto provinceDto : infoDto.getProvinces()) {
				
				if (YeeStringUtils.equals(infoDto.getProvinceCd(), provinceDto.getProvinceID().toString())) {
					
					userAddress.setProvince(provinceDto.getProvinceName());
					chkAddress = true;
					break;
				}
			}
		}
		
		if (!chkAddress) {
			return chkAddress;
		}
		chkAddress = false;
		userAddress.setDistrictId(infoDto.getDistrictCd());
		if (infoDto.getDistricts().size() > 0) {
			
			for(AddressDistrictDto districtDto : infoDto.getDistricts()) {
				
				if (YeeStringUtils.equals(districtDto.getDistrictID().toString(), infoDto.getDistrictCd())
						&& YeeStringUtils.equals(infoDto.getProvinceCd(), districtDto.getProvinceID().toString())) {
					
					userAddress.setDistrict(districtDto.getDistrictName());
					chkAddress = true;
					break;
				}
			}
		}
		
		if (!chkAddress) {
			return chkAddress;
		}
		chkAddress = false;
		userAddress.setWardId(infoDto.getWardCd());
		if (infoDto.getWards().size() > 0) {
			
			for (AddressWardDto wardDto : infoDto.getWards()) {
				
				if (YeeStringUtils.equals(wardDto.getWardId().toString(), infoDto.getWardCd())
						&& YeeStringUtils.equals(infoDto.getDistrictCd(), wardDto.getDistrictId().toString())) {
					
					userAddress.setCommune(wardDto.getWardNm());
					chkAddress = true;
					break;
					
				}
			}
		}
		if (!chkAddress) {
			return chkAddress;
		}
		userAddress.setHamlet(infoDto.getHamlet());
		newUser = this.saverUser(newUser);
		
		if (null == newUser) {

			return false;
		} else {
			
			userAddress = this.userAddressRepository.save(userAddress);
			
			if (null == userAddress) {
				
				this.iUserRepository.delete(newUser);
				return false;
			}
		}
		
		return true;
	}

	@Override
	public Boolean updateUserAdminInfo(InfoDto infoDto, YeeMessage yeeMessage) {

		User admin = this.validateUser();
		
		if (null == admin || !YeeStringUtils.equals(admin.getRole().getRoleName(), YeeCommonConst.YeeRole.ADMIN_ROLE)) {

			return false;
		}
		
		if (!YeeStringUtils.chkValidEmail(infoDto.getEmail())) {
			
			return false;
		}
		
		if (YeeStringUtils.isEmpty(infoDto.getFullNm())) {
			
			return false;
		}
		
		if (!YeeDateTimeUtils.validateDateMinus(infoDto.getUserBirth())) {
			
			return false;
		}
		
		if (13 > YeeDateTimeUtils.calcYearsUntilNow(infoDto.getUserBirth())) {
			
			yeeMessage.setMessageTitle("Người dùng phải từ 13 tuổi trở lên");
			return false;
		}
		
		if (!YeeStringUtils.isNumbericChk(infoDto.getPhone())) {
			
			return false;
		}
		
		if (!YeeStringUtils.isNumbericChk(infoDto.getGender())) {
			
			return false;
		}
		
		if (!YeeStringUtils.isNumbericChk(infoDto.getTypeUser())) {
			
			return false;
		}
		
		if (!YeeStringUtils.isNumbericChk(infoDto.getProvinceCd())) {
			
			return false;
		}
		
		if (!YeeStringUtils.isNumbericChk(infoDto.getDistrictCd())) {
			
			return false;
		}
		
		if (!YeeStringUtils.isNumbericChk(infoDto.getWardCd())) {
			
			return false;
		}
		
		if (YeeStringUtils.isEmpty(infoDto.getHamlet())) {
			
			return false;
		}
		
		User newUser = this.findByEmail(infoDto.getEmail());
		
		String suffix = YeeDateTimeUtils.getCurrentDateTime(); 

		newUser.setUserNm(infoDto.getFullNm());
		newUser.setUserBirth(infoDto.getUserBirth());
		newUser.setUserTel(infoDto.getPhone());

		newUser.setGender(infoDto.getGender());
		if (Integer.parseInt(infoDto.getTypeUser()) < 3) {
			
			Role role = this.iRoleRepository.findByRoleName(YeeRole.ADMIN_ROLE);
			newUser.setRole(role);
		} else {
			
			Role role = this.iRoleRepository.findByRoleName(YeeRole.USER_ROLE);
			newUser.setRole(role);
		}

		newUser.setTypeUser(Integer.parseInt(infoDto.getTypeUser()));
		
		newUser.setUserAvatar(infoDto.getUserAvatar());
		
		newUser.setIsActivated(true);
		
		newUser.setUpdatedBy(admin.getEmail());
		
		newUser.setUpdatedTime(suffix);
		
		UserAddress userAddress = this.userAddressRepository.findByUser(newUser);

		userAddress.setProvinceId(infoDto.getProvinceCd());
		Boolean chkAddress = false;
		
		if (infoDto.getProvinces().size() > 0) {
			
			for(AddressProvinceDto provinceDto : infoDto.getProvinces()) {
				
				if (YeeStringUtils.equals(infoDto.getProvinceCd(), provinceDto.getProvinceID().toString())) {
					
					userAddress.setProvince(provinceDto.getProvinceName());
					chkAddress = true;
					break;
				}
			}
		}
		
		if (!chkAddress) {
			return chkAddress;
		}
		chkAddress = false;
		userAddress.setDistrictId(infoDto.getDistrictCd());
		if (infoDto.getDistricts().size() > 0) {
			
			for(AddressDistrictDto districtDto : infoDto.getDistricts()) {
				
				if (YeeStringUtils.equals(districtDto.getDistrictID().toString(), infoDto.getDistrictCd())
						&& YeeStringUtils.equals(infoDto.getProvinceCd(), districtDto.getProvinceID().toString())) {
					
					userAddress.setDistrict(districtDto.getDistrictName());
					chkAddress = true;
					break;
				}
			}
		}
		
		if (!chkAddress) {
			return chkAddress;
		}
		chkAddress = false;
		userAddress.setWardId(infoDto.getWardCd());
		if (infoDto.getWards().size() > 0) {
			
			for (AddressWardDto wardDto : infoDto.getWards()) {
				
				if (YeeStringUtils.equals(wardDto.getWardId().toString(), infoDto.getWardCd())
						&& YeeStringUtils.equals(infoDto.getDistrictCd(), wardDto.getDistrictId().toString())) {
					
					userAddress.setCommune(wardDto.getWardNm());
					chkAddress = true;
					break;
					
				}
			}
		}
		if (!chkAddress) {
			return chkAddress;
		}
		userAddress.setHamlet(infoDto.getHamlet());
		newUser = this.saverUser(newUser);
		
		if (null == newUser) {

			return false;
		} else {
			
			userAddress = this.userAddressRepository.save(userAddress);
			
			if (null == userAddress) {
				
				this.iUserRepository.delete(newUser);
				return false;
			}
		}
		
		return true;
	}

	@Override
	public Boolean changeUserInfo(InfoDto infoDto, String msg) {
		User user1 = this.validateUser();
		
		//User user2 = this.findByEmail(infoDto.getEmail());
		
		InfoDto orginInfoDto = this.getUserInfo(infoDto.getEmail());
		
		if (null == orginInfoDto) {
			
			return false;
		}
		
		try {
			if (!infoDto.getInitState()) {
				
				if (YeeStringUtils.isNotEmpty(infoDto.getFullNm())) {
					
					orginInfoDto.setFullNm(infoDto.getFullNm());
				}
				
				if (YeeStringUtils.isNotEmpty(infoDto.getPhone())) {
					
					orginInfoDto.setPhone(infoDto.getPhone());
				}
				
				if (YeeDateTimeUtils.validateDateMinus(infoDto.getUserBirth())) {
					
					orginInfoDto.setUserBirth(infoDto.getUserBirth());
				}
				
				if (YeeStringUtils.isNotEmpty(infoDto.getGender())) {
					
					orginInfoDto.setGender(infoDto.getGender());
				}
				
				
				if (YeeStringUtils.isNotEmpty(infoDto.getUserAvatar())) {
					
					orginInfoDto.setUserAvatar(infoDto.getUserAvatar());
				}
				
				if (YeeStringUtils.isNotEmpty(infoDto.getHamlet())) {
					
					orginInfoDto.setHamlet(infoDto.getHamlet());
				}
				
				//orginInfoDto.setProvinces(this.addressApiService.getProvinces());
				
				if (YeeStringUtils.isNumbericChk(infoDto.getProvinceCd())) {
					
					orginInfoDto.setProvinceCd(infoDto.getProvinceCd());
					orginInfoDto.setDistricts(this.addressApiService.getDistricts(orginInfoDto.getProvinceCd()));
					
					if (YeeStringUtils.isNumbericChk(infoDto.getDistrictCd())) {
						
						orginInfoDto.setDistrictCd(infoDto.getDistrictCd());
					} else {
						
						orginInfoDto.setDistrictCd(orginInfoDto.getDistricts().get(0).getDistrictID().toString());
					}

					orginInfoDto.setWards(this.addressApiService.getWards(orginInfoDto.getDistrictCd()));
					
					if (YeeStringUtils.isNumbericChk(infoDto.getWardCd())) {
						
						orginInfoDto.setWardCd(infoDto.getWardCd());
					} else {
						
						orginInfoDto.setWardCd(orginInfoDto.getWards().get(0).getWardId());
					}
				}
			}
			

			this.mapper.map(orginInfoDto, infoDto);

		} catch (Exception e) {
			
			return false;
		}
		
		
		return true;
	}
	
	
}
