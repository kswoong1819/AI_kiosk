package com.web.blog.jwt;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.blog.model.user.User;
import com.web.blog.model.user.Useradmin;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService{

	private static final String ENCRYPT_STRING =  "pretty";
	private static final Logger LOGGER  = LoggerFactory.getLogger(JwtService.class);
	private static final String DATA_KEY = "user";
	
	@Autowired
	private ObjectMapper objectMapper;
	
	public String createLoginToken(User user) {
		long curTime = System.currentTimeMillis();
		return  Jwts.builder()
                 .setHeaderParam("typ", "JWT")
				//  .setExpiration(new Date(curTime + 3600000))
				 .setIssuedAt(new Date(curTime))
				 .claim(DATA_KEY, user)
				 .signWith(SignatureAlgorithm.HS256, this.generateKey())
				 .compact();
	}
	
	//admin
	public String createAdminLoginToken(Useradmin useradmin) {
		long curTime = System.currentTimeMillis();
		return  Jwts.builder()
                 .setHeaderParam("typ", "JWT")
				//  .setExpiration(new Date(curTime + 3600000))
				 .setIssuedAt(new Date(curTime))
				 .claim(DATA_KEY, useradmin)
				 .signWith(SignatureAlgorithm.HS256, this.generateKey())
				 .compact();
	}


	private byte[] generateKey(){
		byte[] key = null;
		try {
			key = ENCRYPT_STRING.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Making secret Key Error :: ", e);
		}
		
		return key;
	}
	
	public User getUser(String jwt) {
		Jws<Claims> claims = null;
		try {
			claims = Jwts.parser()
						 .setSigningKey(this.generateKey())
						 .parseClaimsJws(jwt);
		} catch (Exception e) {
			LOGGER.debug(e.getMessage(), e);
			throw new JWTException("decodeing failed");
		}
		return objectMapper.convertValue(claims.getBody().get(DATA_KEY), User.class);
        // return claims.getBody().get(DATA_KEY);
	}
	
	//admin
	public Useradmin getaUseradmin(String jwt) {
		Jws<Claims> claims = null;
		try {
			claims = Jwts.parser()
						 .setSigningKey(this.generateKey())
						 .parseClaimsJws(jwt);
		} catch (Exception e) {
			LOGGER.debug(e.getMessage(), e);
			throw new JWTException("decodeing failed");
		}
		return objectMapper.convertValue(claims.getBody().get(DATA_KEY), Useradmin.class);
        // return claims.getBody().get(DATA_KEY);
    }


    public Object get(final String jwt) {
        Jws<Claims> claims = null;
        try {
            claims = Jwts.parser().setSigningKey(this.generateKey()).parseClaimsJws(jwt);
        } catch (final Exception e) {
            throw new RuntimeException();
        }

        // Claims는 Map의 구현체이다.
        return claims.getBody().get(DATA_KEY);
    }
}