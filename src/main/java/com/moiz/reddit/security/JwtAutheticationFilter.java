package com.moiz.reddit.security;

import static io.jsonwebtoken.Jwts.parser;

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtAutheticationFilter extends OncePerRequestFilter{
	
	@Value("${jwt.public.key}")
	RSAPublicKey publicKey;
	@Autowired
	private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
			
		String jwt = getJwtFromRequest(request);
		
		if (StringUtils.hasText(jwt) && validateToken(jwt)) {
			
			String username = Jwts.parser().setSigningKey(this.publicKey).parseClaimsJws(jwt).getBody().getSubject();

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                    null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		
		 if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
	            return bearerToken.substring(7);
	        }
	        return bearerToken;
	}
	
	public boolean validateToken(String jwt) {
        parser().setSigningKey(this.publicKey).parseClaimsJws(jwt);
        return true;
    }
	
	

}
