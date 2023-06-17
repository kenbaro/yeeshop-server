package com.yeeshop.app.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * Role Entity
 * 
 * @author XiaoYi
 *
 */
@Entity
@Table(name="Roles")
@Data
public class RoleEntity {
	
	/** RoleCd */
    @Id
    @NotEmpty
    private String roleCd;
    
    /** RoleNm */
    @NotEmpty
    private String roleNm;
}
