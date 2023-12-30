package com.yeeshop.yeeserver.domain.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.yeeshop.yeeserver.domain.constant.YeeEntityConst;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@Entity
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = YeeEntityConst.USER_TABLE)
public class User implements Serializable , UserDetails {


    /** serialVersionUID. */
	private static final long serialVersionUID = 1L;
    
	/** User Id. */
    @Id @NotNull
    private String userCd;
    
    /** UserName. */
    private String userNm;
    
    /** UserPassword. */
    private String userPw;
    
    /** UserTelephone. */
    private String userTel;
    
    /** UserEmail. */
    @Email
    private String email;
    
    /** User Birthday. */
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private String userBirth;
    
    /** UserGender. */
    private String gender;
    
    /** User Is Activated. */
    private Boolean isActivated;
    
    /** User Address. */
    
    @Column(name="userAvatar")
    private String userAvatar;
    
    @Column(name="typeUser")
    private Integer typeUser;
    
    @Column(name="createdBy")
    private String createdBy;
    
    @Column(name="createdTime")
    private String createdTime;
    
    @Column(name="updatedBy")
    private String updatedBy;
    
    @Column(name="updatedTime")
    private String updatedTime;

    /**  An user can have one roles. */
    @OneToOne
    @JoinColumn(name = "RoleCd")
    private Role role ;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/MM/yyyy hh:MM:ss")
    @Column(name = "dateOtp")
	private String dateOtp;
    
    @Column(name = "userOtp")
    private String userOtp;
    
    public User (String userTel , String userPw , Role role) {
      this.userTel = userTel;
      this.userPw = userPw;
      this.role = role;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

	@Override
	public String getPassword() {
		return this.userPw;
	}
}