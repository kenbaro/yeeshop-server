package com.yeeshop.yeeserver.domain.model.province;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProvinccGHNData {
    private Integer ProvinceID;
    
    private String ProvinceName;
    
    private Integer CountryID;
    
    private String Code;
    
    private List<String> NameExtension;
    
    private Integer IsEnable;
    
    private Integer RegionID;
    
    private Integer RegionCPN;
    
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
