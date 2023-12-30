package com.yeeshop.yeeserver.domain.model.fee;

import lombok.Setter;

@Setter
public class FeeData {
	private Integer total;
	private Integer service_fee;
	private Integer insurance_fee;
	private Integer pick_station_fee;
	private Integer coupon_value;
	private Integer r2s_fee;
	private Integer return_again;
	private Integer document_return;
	private Integer double_check;
	private Integer cod_fee;
	private Integer pick_remote_areas_fee;
	private Integer deliver_remote_areas_fee;
	private Integer cod_failed_fee;
	
	public Integer getTotal() {
		
		return this.total;
	}

	public Integer getServiceFee() {
		
		return this.service_fee;
	}
	
	public Integer getInsuranceFee() {
		
		return this.insurance_fee;
	}

	public Integer getPickStationFee() {
		
		return this.pick_station_fee;
	}
	
	public Integer getCouponValue() {
		
		return this.coupon_value;
	} 
	
	public Integer getReturnAgain() {
		
		return this.return_again;
	}
	public Integer getDocumentReturn() {
			
			return this.document_return;
		}
	public Integer getCodFee() {
		
		return this.cod_fee;
	}
	public Integer getDoubleCheck() {
		
		return this.double_check;
	}
	public Integer getR2sFee() {
		
		return this.r2s_fee;
	}
	public Integer getPickRemoteAreasFee() {
		
		return this.pick_remote_areas_fee;
	}
	
	public Integer getDeliverRemoteAreasFee() {
		
		return this.deliver_remote_areas_fee;
	}
	public Integer getCodFailedFee() {
		
		return this.cod_failed_fee;
	}
	
	

	
	
	
}
