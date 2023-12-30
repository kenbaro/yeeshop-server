package com.yeeshop.yeeserver.domain.entity;

import java.io.Serializable;

import com.yeeshop.yeeserver.domain.constant.YeeEntityConst;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = YeeEntityConst.ROLE_TABLE)
public class Role implements Serializable  {


    /** serialVersionUID */
	private static final long serialVersionUID = 1L;

	@Id
    private String uRoleCd;
	
	@Column(name="URoleNm")
	private String roleName;

   
    //public Role (RoleName roleName) {this.uRoleNm = roleName;}

}
