package com.yeeshop.yeeserver.domain.model.district;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DistrictGHNData {

	private Integer DistrictID;
	
	private Integer ProvinceID;
	
	private String DistrictName;
	
	private String Code;
	
	private Integer Type;
	
	private Integer SupportType;
	
	List<String> NameExtension;
	
	private Integer IsEnable;
	
    private Integer UpdatedBy;
    
    private String CreatedAt;
    
    private String UpdatedAt;
    
    private Boolean CanUpdateCOD;
    
    private Integer Status;
    
    private String UpdatedIP;
    
    private Integer UpdatedEmployee;
    
    private String UpdatedSource;
    
    private String UpdatedDate;
}
