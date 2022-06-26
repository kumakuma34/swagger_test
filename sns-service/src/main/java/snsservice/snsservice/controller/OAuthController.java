package snsservice.snsservice.controller;

import antlr.Token;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import org.json.simple.JSONObject;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import snsservice.snsservice.DTO.TokenDTO;
import snsservice.snsservice.Entity.User;
import snsservice.snsservice.service.FeedService;
import snsservice.snsservice.service.TokenService;
import snsservice.snsservice.service.UserService;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OAuthController {


    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserService userservice;
    @Autowired
    private FeedService feedService;
    @CrossOrigin("*")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map<String, Object> getToken(@RequestBody TokenDTO.TokenRequestDTO request) throws Exception{
        String igCode = request.getIgCode();
        Map<String, Object> response = new LinkedHashMap<>();

        ResponseEntity<String> igResponse = tokenService.getIGAccessToken(igCode);
        HttpStatus statusCode = igResponse.getStatusCode();
        JSONParser jsonParser = new JSONParser();
        Object obj = jsonParser.parse(igResponse.getBody());
        JSONObject jsonObj = (JSONObject) obj;

        if(statusCode != HttpStatus.OK){
            throw new IllegalArgumentException(String.format("code %s is not valid", igCode));
        }

        //JSON Parse
        String ig_token = String.valueOf(jsonObj.get("access_token"));
        String ig_id = String.valueOf(jsonObj.get("user_id"));
        String fb_token = request.getFbToken();
        String access_token = tokenService.makeAccessToken(fb_token, ig_token);
        String refresh_token = tokenService.makeRefreshToken(fb_token, ig_token);

        //add Token info to userRepository
        User entity = userservice.RequestToEntity(request, ig_id, ig_token, access_token, refresh_token);
        User res = userservice.addUser(entity);

        response.put("access_token", access_token);
        response.put("refresh_token", refresh_token);

        //because access_token expires in 60 min, scheduler should run for 6 times
        TokenService.checkJWTTokenList.add(TokenDTO.checkDTO.builder().jwt_token(access_token).count(60).build());
        return response;
    }
}
