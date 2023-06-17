package com.yeeshop.app.entity;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * User Entity
 * 
 * @author XiaoYi
 *
 */
@Entity
@Table(name="Users")
@Data
public class UserEntity {

	/** UserId */
    @Id
    @NotEmpty
    private String id;
    
    /** PassWrd */
    @NotEmpty
    @Length(min=6)
    private String passwrd;
    
    /** FirstNm */
    @NotEmpty
    private String firstnm;
    
    /** LastNm */
    @NotEmpty
    private String lastnm;
    
    /** Address */
    @NotEmpty
    private String address;
    
    /** Email */
    @NotEmpty
    private String email;
    
    /** Phone */
    @NotEmpty
    private String phone;
    
    /** Photo */
    @NotEmpty
    private String photo;
    
    /** CreatedDate */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/yyyy/MM HH:mm:ss")
    LocalDateTime createdDate ;
    
    /** CreatedBy */
    @NotEmpty
    private String createdBy;
    
    /** ModifiedDate */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/yyyy/MM HH:mm:ss")
    LocalDateTime modifiedDate ;
    
    /** ModifiedBy */
    @NotEmpty
    private String modifiedBy;
    
    /** IsActivated */
    @NotEmpty
    private Boolean isActivated;
    
    /** Role */
    @ManyToOne
    @JoinColumn(name="roleCd")
    RoleEntity roles;
}
