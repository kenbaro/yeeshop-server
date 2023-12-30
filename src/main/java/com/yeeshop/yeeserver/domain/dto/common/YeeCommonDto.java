package com.yeeshop.yeeserver.domain.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Common Data Transfer Object.
 * 
 * @author Thai Duy Bao.
 * @since 2023.
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class YeeCommonDto {

	private YeeHeaderDto yeeHeaderDto;
	
	private YeeFooterDto yeeFooterDto;
}
