package com.losruedacarta.core.infraestructura.filtro;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;

import com.losruedacarta.core.infraestructura.token.ValidadorToken;
import com.losruedacarta.core.infraestructura.error.Error;
import com.losruedacarta.core.dominio.excepcion.ExcepcionAutenticacion;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;

@Order(1)
public class FiltroValidacionToken implements Filter {
	private static final String HEADER_AUTHORIZATION = "Authorization";
	private static final String CONTENT_TYPE = "application/json;charset=UTF-8";
	private static final String NOMBRE_COOKIE = "cookie";
	private final ObjectMapper mapper = new ObjectMapper();
	private final ValidadorToken validadorToken;

	public FiltroValidacionToken(ValidadorToken validadorToken) {
		this.validadorToken = validadorToken;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		try {
			if (httpRequest.getCookies() != null) {
				hacerFiltroConCookie(httpRequest, httpResponse, chain);
			} else if (httpRequest.getHeader(HEADER_AUTHORIZATION) != null) {
				hacerFiltroConHeader(httpRequest, httpResponse, chain);
			} else {
				lanzarError(httpResponse, ExcepcionAutenticacion.MENSAJE_CREDENCIALES_ERRONEAS,
						HttpStatus.UNAUTHORIZED);
			}

		} catch (ParseException | BadJOSEException | JOSEException e) {
			lanzarError(httpResponse, ExcepcionAutenticacion.MENSAJE_CREDENCIALES_ERRONEAS, HttpStatus.UNAUTHORIZED);
		}

	}

	private void hacerFiltroConCookie(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ParseException, BadJOSEException, JOSEException, IOException, ServletException {
		String token = null;
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (NOMBRE_COOKIE.equals(cookie.getName())) {
				token = cookie.getValue();
			}
		}
		validadorToken.retornarClaimSetSiTokenEsValido(token);
		filterChain.doFilter(request, response);
	}

	private void hacerFiltroConHeader(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ParseException, BadJOSEException, JOSEException, IOException, ServletException {
		String token = request.getHeader(HEADER_AUTHORIZATION);
		validadorToken.retornarClaimSetSiTokenEsValido(token);
		filterChain.doFilter(request, response);
	}

	private Error lanzarError(HttpServletResponse servletResponse, String mensaje, HttpStatus httpStatus)
			throws IOException {
		Error error = new Error(ExcepcionAutenticacion.class.getSimpleName(), mensaje);
		servletResponse.setContentType(CONTENT_TYPE);
		PrintWriter writer = servletResponse.getWriter();
		writer.write(mapper.writeValueAsString(error));
		servletResponse.setStatus(httpStatus.value());
		writer.flush();
		writer.close();
		return error;
	}
}
