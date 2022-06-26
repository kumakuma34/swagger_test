package snsservice.snsservice.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import snsservice.snsservice.DTO.TokenDTO;
import snsservice.snsservice.Entity.User;
import snsservice.snsservice.Repository.UserRepository;


@Service
@AllArgsConstructor
public class UserService {
    public final UserRepository userRepository;

    public User RequestToEntity(TokenDTO.TokenRequestDTO request, String ig_id, String ig_token, String access_token, String refresh_token){
        return User.builder()
                .fbName(request.getFbName())
                .fbId(request.getFbId())
                .fbToken(request.getFbToken())
                .igId(ig_id)
                .igToken(ig_token)
                .accessToken(access_token)
                .refreshToken(refresh_token)
                .build();
    }

    public User addUser(User entity){
        return userRepository.save(entity);
    }

    public TokenDTO.AccessTokenDTO getAccessToken(String Token){
        User entity = userRepository.findOneByAccessToken(Token);
        return TokenDTO.AccessTokenDTO.builder()
                .fb_access_token(entity.getFbToken())
                .ig_access_token(entity.getIgToken())
                .fb_id(entity.getFbId())
                .ig_id(entity.getIgId())
                .build();
    }
}
