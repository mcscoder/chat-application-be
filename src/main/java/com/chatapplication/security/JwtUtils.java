package com.chatapplication.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtils {

    // Use SHA256 to encode, original ascii: MCSMUSCLE
    public static final String SECRET_KEY = "3FC55D90B32A713DA07D65EB06EB1D7A1473E4FFF843A7606F6607DB348B6FFA";

    /**
     * Extract the username from the provided JWT
     * 
     * @param token The JWT string
     * @return The extracted username
     */
    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    /**
     * Extract the username from the client request
     * 
     * @param request The client request
     * @return The extracted username from request
     */
    public String extractUsername(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            username = extractUsername(jwt);
        }
        return username;
    }

    /**
     * Generate a JWT for the given username
     * 
     * @param username The username
     * @return The generated JWT string
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    /**
     * Check if the provided JWT is valid for the given user details
     * 
     * @param token       The JWT string
     * @param userDetails The user details
     * @return True if the token is valid for the user, false otherwise
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        return (extractUsername(token)
                .equals(userDetails.getUsername()) &&
                !isTokenExpired(token));
    }

    /**
     * Extract specific claim from the provided JWT
     * 
     * @param token          The JWT string
     * @param claimsResolver The claimsResolver function that will specify which
     *                       claim should be extracted
     * @return The specified extracted claims
     */
    private <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the provided JWT
     * Kiểu như giải mã cái token đã được mã hóa thành 1 dạng hợp lệ trong Java
     * 
     * @param token The JWT string
     * @return The claims extracted from the token
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Create a JSON Web Token (JWT) from the provided claims and username
     * 
     * @param claims   The claims of the JWT
     * @param username The username
     * @return The created token (JWT) String
     */
    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setSubject(username) // Set the username
                .setIssuedAt(new Date(System.currentTimeMillis())) // Set the creation date
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Expires after 1 day
                .signWith(getSignKey(), SignatureAlgorithm.HS256) // Set the secret key and encryption algorithm
                .compact(); // Returns an encrypted and compressed JWT string
    }

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }

    /**
     * Check if the provided JWT is expired
     * 
     * @param token The JWT string
     * @return True if the token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token)
                .before(new Date(System.currentTimeMillis()));
    }

    /**
     * Extract the expiration from the provided JWT
     * 
     * @param token The JWT string
     * @return The extracted expiration
     */
    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

}
