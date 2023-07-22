package dev.nhason.security;

import dev.nhason.error.BadRequestException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTProvider {

    @Value("${dev.nhason.hotel.secret}")
    private  String secret;

    @Value("${dev.nhason.hotel.expires}")
    private Long expire;

    // Random KEY
    // SecretKey kew = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private static Key mSecretkey ;

    @PostConstruct
    private void init(){
        mSecretkey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateToken(String username){
        var currentDate = new Date();
        var expireDate = new Date(currentDate.getTime() + expire);
        return Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(mSecretkey)
                .compact();
    }

    public boolean validateToken(String jwt){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(mSecretkey)
                    .build()
                    .parseClaimsJws(jwt);

        }catch (ExpiredJwtException e){
            throw new BadRequestException("Token","Expire");
        }catch (MalformedJwtException e){
            throw new BadRequestException("Token","Invalid");
        }catch (JwtException e){
            throw new BadRequestException("Token","Exception" + e.getMessage());
        }
        return true;
    }

    public String getUserNameFromToken(String jwt){
        return Jwts.parserBuilder()
                .setSigningKey(mSecretkey)
                .build()
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
    }

}
