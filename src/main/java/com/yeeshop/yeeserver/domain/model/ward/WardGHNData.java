package com.yeeshop.yeeserver.domain.model.ward;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class WardGHNData {

	private String WardCode;
	
	private Integer DistrictID;
	
	private String WardName;
	
	private List<String> NameExtension;
	
	private Integer IsEnable;
	
	private Boolean CanUpdateCOD;
	
	private Integer UpdatedBy;
	
    private String CreatedAt;
    
    private String UpdatedAt;
    
    private Integer Status;
    
    private String UpdatedIP;
    
    private Integer UpdatedEmployee;
    
    private String UpdatedSource;
    
    private String UpdatedDate;
}
