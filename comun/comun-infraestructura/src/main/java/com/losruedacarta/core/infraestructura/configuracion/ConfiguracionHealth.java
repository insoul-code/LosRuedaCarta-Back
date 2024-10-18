package com.losruedacarta.core.infraestructura.configuracion;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.losruedacarta.core.infraestructura.actuator.DataSourceHealthIndicator;

@Configuration
public class ConfiguracionHealth {
	
	
	private final  DataSource dataSource;
	
	
	public ConfiguracionHealth(DataSource dataSource) {
		this.dataSource = dataSource;
	}


	@Bean
    public DataSourceHealthIndicator dataSourceHealthIndicator() {
		return new DataSourceHealthIndicator(this.dataSource);
       
    } 

}
