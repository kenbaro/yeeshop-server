package com.yeeshop.yeeserver.app.form.common;

import com.yeeshop.yeeserver.domain.dto.common.YeeFooterDto;
import com.yeeshop.yeeserver.domain.dto.common.YeeHeaderDto;

import lombok.Data;


/**
 * Common Form Object.
 * 
 * @author Thai Duy Bao.
 * @since 2023.
 */
@Data
public class YeeCommonForm {

	private YeeHeaderDto yeeHeaderDto;
	
	private YeeFooterDto yeeFooterDto;
}
