package com.losruedacarta.core.infraestructura.token;

import java.text.ParseException;

import org.springframework.stereotype.Component;

import com.losruedacarta.core.dominio.excepcion.ExcepcionAutenticacion;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;

@Component
public class ValidadorToken {
	
	private final ConfigurableJWTProcessor configurableJWTProcessor;

	public ValidadorToken(ConfigurableJWTProcessor configurableJWTProcessor) {
		this.configurableJWTProcessor = configurableJWTProcessor;
	}

	public JWTClaimsSet retornarClaimSetSiTokenEsValido(String token)
			throws ParseException, BadJOSEException, JOSEException {
		String idToken = this.extractAndDecodeJwt(token);
		JWTClaimsSet claimsSet = configurableJWTProcessor.process(idToken, null);

		if (!isIdToken(claimsSet)) {
			throw new ExcepcionAutenticacion(ExcepcionAutenticacion.MENSAJE_TIPO_TOKEN_ERRONEO);
		}

		return claimsSet;
	}

	private boolean isIdToken(JWTClaimsSet claimsSet) {
		return claimsSet.getClaim("token_use").equals("id");
	}

	private String extractAndDecodeJwt(String token) {
		String tokenResult = token;

		if (token != null && token.startsWith("Bearer ")) {
			tokenResult = token.substring("Bearer ".length());
		}
		return tokenResult;
	}
}
