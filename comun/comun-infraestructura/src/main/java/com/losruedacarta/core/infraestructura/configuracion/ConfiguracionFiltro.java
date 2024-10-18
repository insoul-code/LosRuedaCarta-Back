package com.losruedacarta.core.infraestructura.configuracion;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.losruedacarta.core.infraestructura.filtro.FiltroValidacionToken;
import com.losruedacarta.core.infraestructura.token.ValidadorToken;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class ConfiguracionFiltro {

	@Value("${endpoint.security}")
	private String endPointFiltrar;
	
	@Bean
	public FiltroValidacionToken filtroValidacionToken(ValidadorToken validadorToken) {
		return new FiltroValidacionToken(validadorToken);
	}

	@Bean
	public FilterRegistrationBean<FiltroValidacionToken> filtroValidacionTokenRegistrationBean(
			FiltroValidacionToken filtroValidacionToken) {
		FilterRegistrationBean<FiltroValidacionToken> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(filtroValidacionToken);
//		la linea de abajo es para registrar url especificas
		registrationBean.addUrlPatterns(this.endPointFiltrar + "*");
		return registrationBean;
	}
	
}
