package com.yeeshop.yeeserver.domain.model.fee;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FeeRequestBody {
    private Integer service_id;
    private Integer service_type_id;
    private Integer insurance_value;
    private String coupon;
    private Integer from_district_id;
    private Integer to_district_id;
    private String to_ward_code;
    private Integer height;
    private Integer length;
    private Integer weight;
    private Integer width;
}
