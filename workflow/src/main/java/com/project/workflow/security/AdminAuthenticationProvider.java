//package com.project.workflow.security;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import java.security.Key;
//import java.util.Collections;
//import java.util.Date;
//import java.util.function.Function;
//
//public class AdminAuthenticationProvider implements AuthenticationProvider {
//    private final String adminEmail;
//    private final String adminPassword;
//
//    public AdminAuthenticationProvider(String adminEmail, String adminPassword) {
//        this.adminEmail = adminEmail;
//        this.adminPassword = adminPassword;
//    }
//
//    private static final String COOKIE_NAME = "custom_cookie";
//    private static final String SECRET_KEY="afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf";
//
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//        String tokenOrCookie = request.getHeader("Authorization");
//        if (tokenOrCookie == null) {
//            throw new BadCredentialsException("No token or cookie provided");
//        }
//        // Example: extract token from cookie and validate
//        String token = extractTokenFromCookie(request, tokenOrCookie);
//        if (isTokenValid(token)) {
//            return new UsernamePasswordAuthenticationToken(token, null, Collections.emptyList());
//        }
//        throw new BadCredentialsException("Invalid admin cookie");
//    }
//
//    private String extractTokenFromCookie(HttpServletRequest request, String tokenOrCookie) {
//        if (tokenOrCookie != null) {
//            Cookie[] cookies = request.getCookies();
//            if (cookies != null) {
//                for (Cookie cookie : cookies) {
//                    if (cookie.getName().equals(COOKIE_NAME)) {
//                        return cookie.getValue();
//                    }
//                }
//            }
//        }
//        return null;
//    }
//
//    public boolean isTokenValid(String token){
//        final String username=extractUsername(token);
//        return (username.equals(adminEmail) && !isTokenExpired(token));
//    }
//
//    private boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    private Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//    private Claims extractAllClaims(String token){
//        return Jwts.parserBuilder()
//                .setSigningKey(getSignInKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//    private Key getSignInKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//
//
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
//    }
//}
