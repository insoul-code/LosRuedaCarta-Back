package com.losruedacarta.core.infraestructura.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.losruedacarta.core.infraestructura.filtro.FiltroHeaderSeguridad;

@Configuration
public class ConfiguracionHeader {
	
	@Bean
	public FiltroHeaderSeguridad filtroHeader() {
		return new FiltroHeaderSeguridad();
	}

}