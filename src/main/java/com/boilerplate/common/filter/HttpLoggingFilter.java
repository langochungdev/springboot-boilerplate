package com.boilerplate.common.filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
public class HttpLoggingFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getRequestURI();
        // loại tĩnh và health
        return path.startsWith("/actuator/health")
                || path.startsWith("/swagger")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/static/")
                || path.startsWith("/webjars/");
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest req,
                                    @NonNull HttpServletResponse res,
                                    @NonNull FilterChain chain) throws ServletException, IOException {

        String traceId = getOrCreateTraceId(req);
        MDC.put("traceId", traceId);

        ContentCachingRequestWrapper request = new ContentCachingRequestWrapper(req);
        ContentCachingResponseWrapper response = new ContentCachingResponseWrapper(res);

        long start = System.nanoTime();
        try {
            chain.doFilter(request, response);
        } finally {
            long tookMs = (System.nanoTime() - start) / 1_000_000;

            String method = request.getMethod();
            String uri = request.getRequestURI();
            String query = request.getQueryString();
            int status = response.getStatus();
            String ip = clientIp(request);
            String userId = currentUserId();

            int reqLen = request.getContentLength();
            int respLen = response.getContentSize();

            if (log.isInfoEnabled()) {
                log.info("[HTTP] {} {}{} -> {} {}ms ip={} user={} len(req={},resp={}) traceId={}",
                        method, uri, query != null ? "?" + query : "",
                        status, tookMs, ip, userId, reqLen, respLen, traceId);
            }

            // chỉ log body khi DEBUG để tránh rò rỉ
            if (log.isDebugEnabled()) {
                String reqBody = readRequestBody(request);
                String respBody = readResponseBody(response);
                log.debug("[HTTP][BODY][REQ] {}", reqBody);
                log.debug("[HTTP][BODY][RES] {}", respBody);
            }

            response.copyBodyToResponse();
            MDC.remove("traceId");
        }
    }

    private String clientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(xff)) return xff.split(",")[0].trim();
        String xr = request.getHeader("X-Real-IP");
        if (StringUtils.hasText(xr)) return xr;
        return request.getRemoteAddr();
    }

    private String currentUserId() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && auth.getPrincipal() != null) {
                return String.valueOf(auth.getName()); // thay bằng CurrentUserUtil nếu có
            }
        } catch (Exception ignored) {}
        return "anon";
    }

    private String getOrCreateTraceId(HttpServletRequest request) {
        String fromHeader = request.getHeader("X-Trace-Id");
        return StringUtils.hasText(fromHeader) ? fromHeader : UUID.randomUUID().toString();
    }

    private String readRequestBody(ContentCachingRequestWrapper request) {
        byte[] buf = request.getContentAsByteArray();
        if (buf.length == 0) return "";
        return new String(buf, 0, Math.min(buf.length, 4096), StandardCharsets.UTF_8); // giới hạn 4KB
    }

    private String readResponseBody(ContentCachingResponseWrapper response) {
        byte[] buf = response.getContentAsByteArray();
        if (buf.length == 0) return "";
        return new String(buf, 0, Math.min(buf.length, 4096), StandardCharsets.UTF_8);
    }
}
