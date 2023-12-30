package com.yeeshop.yeeserver.domain.model.deliveryservice;

import lombok.Setter;

@Setter
public class ServiceData {

	private Integer service_id;
    private String short_name;
    private Integer service_type_id;
    private String config_fee_id;
    private String extra_cost_id;
    private String standard_config_fee_id;
    private String standard_extra_cost_id;
    
    public Integer getServiceId() {
    	
    	return this.service_id;
    }
    
	public Integer getServiceTypeId() {
	    	
    	return this.service_type_id;
    }
	
	public String getShortName() {
    	
    	return this.short_name;
    }
	
	public String getConfigFeeId() {
	    	
	    	return this.config_fee_id;
	    }
	
	public String getExtraCostId() {
		
		return this.extra_cost_id;
	}
	public String getStandardConfigFeeId() {
		
		return this.standard_config_fee_id;
	}
	public String getStandardExtraCostId() {
		
		return this.standard_extra_cost_id;
	}
}
