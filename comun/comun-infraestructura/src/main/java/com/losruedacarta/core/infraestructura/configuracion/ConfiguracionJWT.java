package com.losruedacarta.core.infraestructura.configuracion;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jose.util.ResourceRetriever;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

@Configuration
public class ConfiguracionJWT {
	@Value("${cognito.connection.time.out}")
	private int CONNECTION_TIME_OUT;
	
	@Value("${cognito.read.time.out}")
	private int READ_TIME_OUT;

	@Value("${cognito.public.key}")
	private String urlCognitoPublicKey;
	
	@Bean
	public ConfigurableJWTProcessor configurableJWTProcessor() throws MalformedURLException {
		ResourceRetriever resourceRetriever = new DefaultResourceRetriever(CONNECTION_TIME_OUT,READ_TIME_OUT);
		URL jwkSetURL= new URL(urlCognitoPublicKey);
		JWKSource keySource= new RemoteJWKSet(jwkSetURL, resourceRetriever);
		ConfigurableJWTProcessor jwtProcessor= new DefaultJWTProcessor();
		//RSASSA-PKCS-v1_5 using SHA-256 hash algorithm
	 	JWSKeySelector keySelector= new JWSVerificationKeySelector(JWSAlgorithm.RS256, keySource);
	 	jwtProcessor.setJWSKeySelector(keySelector);
	 	return jwtProcessor;
	}
}