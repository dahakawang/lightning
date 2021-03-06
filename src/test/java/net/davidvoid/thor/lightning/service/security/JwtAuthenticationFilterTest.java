package net.davidvoid.thor.lightning.service.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.davidvoid.thor.lightning.config.RootConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by david on 5/4/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = { RootConfig.class })
public class JwtAuthenticationFilterTest {
    @Autowired
    JwtAuthenticationFilter filter = null;

    @Before
    public void setup() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Test
    public void doFilter_givenValidToken_WillSuccess() throws Exception {
        FilterChain filterChain = mock(FilterChain.class);
        Cookie cookie = mock(Cookie.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Cookie[] cookies = {cookie};
        when(request.getCookies()).thenReturn(cookies);

        String key = JwtAuthenticationServiceTest.load_key("config/key.hmac");
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS512, key).claim("uid", 123).setSubject("david").setIssuedAt(new Date()).compact();
        when(cookie.getName()).thenReturn("jwt-token");
        when(cookie.getValue()).thenReturn(token);

        filter.doFilter(request, response, filterChain);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        ThorAuthentication auth = (ThorAuthentication) SecurityContextHolder.getContext().getAuthentication();
        assertEquals("david", auth.getName());
        assertEquals(123, auth.getId());
    }

    @Test
    public void doFilter_givenNoToken_WillNotCreateAuthentication() throws Exception {
        FilterChain chain = mock(FilterChain.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        filter.doFilter(request, response, chain);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void doFilter_givenWrongToken_WillNotCreateAuthentication() throws Exception {
        FilterChain filterChain = mock(FilterChain.class);
        Cookie cookie = mock(Cookie.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Cookie[] cookies = {cookie};
        when(request.getCookies()).thenReturn(cookies);

        when(cookie.getName()).thenReturn("jwt-token");
        when(cookie.getValue()).thenReturn("wrongtoken");

        filter.doFilter(request, response, filterChain);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void doFilter_givenTokenIssuedInTheFuture_WillNotCreateAuthentication() throws Exception {
        FilterChain filterChain = mock(FilterChain.class);
        Cookie cookie = mock(Cookie.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Cookie[] cookies = {cookie};
        when(request.getCookies()).thenReturn(cookies);

        Date date = new Date();
        date.setTime(date.getTime() + 30*24*60*60*1000);
        String key = JwtAuthenticationServiceTest.load_key("config/key.hmac");
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS512, key).claim("uid", 123)
                .setSubject("david").setIssuedAt(date).compact();
        when(cookie.getName()).thenReturn("jwt-token");
        when(cookie.getValue()).thenReturn(token);

        filter.doFilter(request, response, filterChain);
    }
    @Test
    public void doFilter_givenExpiredToken_WillNotCreateAuthentication() throws Exception {
        FilterChain filterChain = mock(FilterChain.class);
        Cookie cookie = mock(Cookie.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Cookie[] cookies = {cookie};
        when(request.getCookies()).thenReturn(cookies);

        Date date = new Date();
        date.setTime(date.getTime() - 30*24*60*60*1000 - 1);
        String key = JwtAuthenticationServiceTest.load_key("config/key.hmac");
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS512, key).claim("uid", 123)
                .setSubject("david").setIssuedAt(date).compact();
        when(cookie.getName()).thenReturn("jwt-token");
        when(cookie.getValue()).thenReturn(token);

        filter.doFilter(request, response, filterChain);
    }

}