package com.psj.itembrowser.security.common.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;

import feign.Retryer;

/**
 *packageName    : com.psj.itembrowser.security.common.config
 * fileName       : OpenFeignConfig
 * author         : ipeac
 * date           : 2024-02-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-02-13        ipeac       최초 생성
 */
@Configuration
@EnableFeignClients(basePackages = "com.psj.itembrowser.security.common.openfeign.service")
public class OpenFeignConfig {

	@Bean
	public Retryer feignRetryer() {
		return new Retryer.Default(1000, 5000, 3);
	}

	@Bean
	public FeignFormatterRegistrar dateTimeFormatterRegistrar() {
		return registry -> {
			DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
			registrar.setUseIsoFormat(true);
			registrar.registerFormatters(registry);
		};
	}
}