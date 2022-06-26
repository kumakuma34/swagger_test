package snsservice.snsservice.TokenTest;

import org.apache.catalina.connector.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import snsservice.snsservice.DTO.TokenDTO;
import snsservice.snsservice.service.TokenService;
import snsservice.snsservice.service.UserService;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class JwtTokenTest {

    @Autowired
    TokenService tokenService;
    @Autowired
    UserService userService;
    @Test
    public void jwtTokenTest(){
        //Given
        String fb_token = "y7bldXwfuBV5NfQp90D9lCpnX84uSG1iZ-lIpv_8rfc.eyJ1c2VyX2lkIjoiNTI0MzM2MTU3NTcxNTUxOCIsImNvZGUiOiJBUUN4UTN3NkNDc29Da1ZOY2tjT2ZfSjRrQWx3eVpieFJmcmxUckstZU55bWRHdDh2OEwwMW5FbzUwZ0VVbXVUOUVmaHVZemxQbUViOUlDbHpmeVFWc011clN6RUl0RGRuRGgyRGZCdVVLNU5USHAtbk9UdGYteHRLNEdqUFpWUVExUGhoNmh3Q1V5M3NtWTJqdnY0SlZVSG52WnVpTVpDb2t6U2Q2bnlESmpRanNTVnNXN0dRSnpUMWJ1SW1laWI5MkJIZ190TDNNdUlfdTVTMFcycGV6ZFc1ZF9MSU4tX3ozZ3lBaW00eHZBNlAxbE1yWi1Jd1hlWG4xckRyT2xKd1JmLTVkUjBKQXlPdXVqR3ZUODNRbDQ1d0FQOE5iY1NoNmU4X3V4NkctY3VZMW5wRk5uM2JJMlZPLXhEZDl2MTJ3YnNtTXFySHhGcXU5ajJod0RCclBVTCIsImFsZ29yaXRobSI6IkhNQUMtU0hBMjU2IiwiaXNzdWVkX2F0IjoxNjUxODAxNTYzfQ";
        String ig_token = "IGQVJXZA2U2SkJrYnhKOWRkRG81eHRxZAGt4VUxqaG0xT0kwQXo4YzdFSDA2VzNJRkxVU1dTUlEyN2V3VXRpWXJuakRPcF94LWpTb19QTG5ZAakh6Y3E0bHUyNERGSEtLUGtKcHYtMUNZAdWFCWmlrdFFsRTJiZAHRhc1ZA4UHZAN";

        //when
        String jwtToken = tokenService.makeAccessToken(fb_token, ig_token);

        //then
        System.out.println(jwtToken);

    }

    @Test
    public void makeIGAccessTokenTest(){
        //Given
        String ig_Code = "AQBVoNeMTRHmtXBXRBSKvTIp5t6t85_jW8nwxHltmmd0WgbNGAZJxbmuSnUMYypGk2OdKDzH0rmgBhmeX5BC_ra9eRidagfca8DVkMKeUtIbupLeEJjkR7QOxrAc2b9frZam6dga7LKB_ZbotccEN-KtNqTg1TKBYRwPE52vuDqUcmUOkRaZ34YqLJJ7UbfHQ4GYWHtJLBCh2KgS_iqeV7R2UKYWB_o_UqLEC0mp4UYpCQ";

        //When
        ResponseEntity<String> res = tokenService.getIGAccessToken(ig_Code);

        //Then
        System.out.println(res.toString());
        HttpStatus statusCode = res.getStatusCode();
        assertEquals("200", "200");
    }

    @Test
    public void getToken(){
        //Given
        String access_token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJFQUFKZ2lXbUpsajBCQU9XSlpBcXo3dXBjN2ZmdWs5Zk8wdzVESUt4TlBSTWU2MFRpZWhuUmM3ekM3M3cyOEdkNkdXclhzNnpTN2hrdmlJWXhaQ05sam4xb1VNSGxaQ2FkZ3k2cDJQT2dLWEJ3Q25ZSnNPcFpCOW5jQ1RmbFk2STBDeW5GY3NrbVBpWXZoMnFaQTRlZUpNRjNEbndPeURMNndtWkFwSmpiRlZoVlZCak5mSzUxQVpCSnFmTGVLN2dubEp1U0RMb3E4MG9YM0lJS1ZNN2MwcXVaQlpDR2Y0Y1h1WkNsVVpESUdRVkpYUlcxdU5EWkFZUldGQmRGODBTbFEzWkFFVmhUMDgzVlhkaFdqWkFaQWIxSTBhSFJKTkZCalZqRTRWV0pKVmw5WkFWbTlEU25kMmVIWXlhMkZLWkFucFZaQUdONVZFOVZXVkpmZERkTlRXbEpObWwwTmtST1NGRjNkVE5TWXpKaVMwRnlhR0ptZERCU1JGTlVWWEZJTkhkbVpBemQzVTNkcFUxSmlUM1k0IiwiZXhwIjoxNjUxOTU1Nzk0fQ.A2zGU3vVYEQ0IRhRRiDGCzSn17i53RoUH7_qlQ1Htp0";
        TokenDTO.AccessTokenDTO accessTokenDTO =userService.getAccessToken(access_token);

        //When
        String fb_token = accessTokenDTO.getFb_access_token();
        String ig_token = accessTokenDTO.getIg_access_token();
        String fb_id = accessTokenDTO.getFb_id();
        String ig_id = accessTokenDTO.getIg_id();
        //Then
        System.out.println(fb_token);
        System.out.println(ig_token);
        System.out.println(fb_id);
        System.out.println(ig_id);

    }

}

