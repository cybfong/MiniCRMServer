package com.oji.mini_crm_server.config;

import com.oji.mini_crm_server.service.JwtService;
import com.oji.mini_crm_server.service.MyUserDetailsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class JwtFilterTest {

    @AfterEach
    void cleanup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_withValidBearer_setsAuthentication() throws Exception {
        MyUserDetailsService uds = mock(MyUserDetailsService.class);
        JwtService jwtService = mock(JwtService.class);

        JwtFilter filter = new JwtFilter(uds, jwtService);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(req.getHeader("Authorization")).thenReturn("Bearer tok-123");
        when(jwtService.extractUserName("tok-123")).thenReturn("alice");

        UserDetails ud = org.springframework.security.core.userdetails.User.withUsername("alice").password("x").roles("USER").build();
        when(uds.loadUserByUsername("alice")).thenReturn(ud);
        when(jwtService.validateToken("tok-123", ud)).thenReturn(true);

        filter.doFilterInternal(req, resp, chain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNotNull();
        assertThat(auth.getName()).isEqualTo("alice");

        verify(chain, times(1)).doFilter(req, resp);
    }

    @Test
    void doFilterInternal_noHeader_callsChainAndLeavesContextEmpty() throws Exception {
        JwtFilter filter = new JwtFilter(mock(MyUserDetailsService.class), mock(JwtService.class));

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(req.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(req, resp, chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(chain, times(1)).doFilter(req, resp);
    }

    @Test
    void doFilterInternal_exception_clearsContextAndCallsChain() throws Exception {
        MyUserDetailsService uds = mock(MyUserDetailsService.class);
        JwtService jwtService = mock(JwtService.class);

        JwtFilter filter = new JwtFilter(uds, jwtService);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(req.getHeader("Authorization")).thenReturn("Bearer bad");
        when(jwtService.extractUserName("bad")).thenThrow(new RuntimeException("boom"));

        filter.doFilterInternal(req, resp, chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(chain, times(1)).doFilter(req, resp);
    }
}

