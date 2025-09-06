package com.boilerplate.infrastructure.filter;
import com.boilerplate.common.exception.BusinessException;
import com.boilerplate.common.exception.errorcode.AuthError;
import com.boilerplate.common.util.JwtUtil;
import com.boilerplate.feature.user.entity.User;
import com.boilerplate.feature.user.repository.UserRepository;
import com.boilerplate.infrastructure.security.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.UUID;
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws IOException, ServletException {
        try {
            String token = jwtUtil.getTokenBearer(request);

            if (token != null) {
                jwtUtil.validate(token);

                UUID userId = jwtUtil.extractUserId(token);
                if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new BusinessException(AuthError.USER_NOT_FOUND));

                    UserDetails userDetails = new CustomUserDetails(
                            user.getId(),
                            user.getUsername(),
                            user.getPassword(),
                            user.getEmail(),
                            user.getFullName(),
                            user.getIsActive(),
                            user.getIsVerified(),
                            user.getUserRoles().stream()
                                    .map(ur -> new SimpleGrantedAuthority("ROLE_" + ur.getRole().getName()))
                                    .toList()
                    );

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            SecurityContextHolder.clearContext();
            resolver.resolveException(request, response, null, ex);
        }
    }
}
