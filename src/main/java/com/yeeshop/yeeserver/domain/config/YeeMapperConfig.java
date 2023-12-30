package com.yeeshop.yeeserver.domain.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * A configuration Class to config mapper.
 * @author Thai Duy Bao.
 * since 2023
 */
@Configuration
public class YeeMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        // Create an instance of Mapper.
        ModelMapper modelMapper = new ModelMapper();
        
        // Config Mapper.
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        
        // return Mapper
        return modelMapper;
    }
}
