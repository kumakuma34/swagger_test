package snsservice.snsservice.service;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import snsservice.snsservice.DTO.TokenDTO;
import snsservice.snsservice.Entity.User;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
@Component
public class TokenService {
    @Value("${ACCESS_TOKEN_LIFETIME}")
    private int ACCESS_TOKEN_LIFETIME;
    @Value("${REFRESH_TOKEN_LIFETIME}")
    private int REFRESH_TOKEN_LIFETIME;
    @Value("${JWT_SECRET}")
    private String JWT_SECRET;

    private String IG_APP_ID = "359822086174240";
    private String IG_APP_SCRETE_CODE = "506a1e8682ec5121302e46223d6b5307";
    private String IG_REDIRECT_URI = "https://skt-test.netlify.app/";


    public static List<TokenDTO.checkDTO> checkJWTTokenList = new ArrayList<>();

    public String makeAccessToken(String fbToken,  String igToken){
        return makeJwtToken(fbToken, igToken, ACCESS_TOKEN_LIFETIME);
    }

    public String makeRefreshToken(String fbToken,  String igToken){
        return makeJwtToken(fbToken, igToken, REFRESH_TOKEN_LIFETIME);
    }

    public String makeJwtToken(String fbToken, String igToken,  int tokenLifeTime){
        String JktKey = fbToken + igToken;
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        final Key signingKey = new SecretKeySpec(generateKeyAsByte(), signatureAlgorithm.getJcaName());
        return Jwts.builder()
                .setId(String.valueOf(JktKey))
                .signWith(signingKey, signatureAlgorithm)
                .setExpiration(new Date(System.currentTimeMillis() + tokenLifeTime))
                .compact();
    }

    public byte[] generateKeyAsByte(){
        return DatatypeConverter.parseBase64Binary(JWT_SECRET);
    }

    public ResponseEntity<String> getIGAccessToken(String ig_Code){
        String url = "https://api.instagram.com/oauth/access_token";

        AsyncRestTemplate restTemplate = new AsyncRestTemplate(); // 비동기 전달
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        JSONObject jsonObject = new JSONObject();

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", IG_APP_ID);
        params.add("client_secret", IG_APP_SCRETE_CODE);
        params.add("grant_type","authorization_code");
        params.add("redirect_uri" , IG_REDIRECT_URI);
        params.add("code", ig_Code);

        HttpHeaders headers = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        RestTemplate rt = new RestTemplate();

        ResponseEntity<String> response = rt.exchange(
                url, //{요청할 서버 주소}
                HttpMethod.POST, //{요청할 방식}
                entity, // {요청할 때 보낼 데이터}
                String.class);

        System.out.println(response);
        return response;
    }


}
