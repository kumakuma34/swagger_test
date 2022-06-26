package snsservice.snsservice.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class TokenDTO {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TokenRequestDTO{
        @JsonProperty("fb_name")
        private String fbName;
        @JsonProperty("fb_id")
        private String fbId;
        @JsonProperty("fb_token")
        private String fbToken;
        @JsonProperty("ig_code")
        private String igCode;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AccessTokenDTO{
        @JsonProperty("fb_access_token")
        private String fb_access_token;
        @JsonProperty("ig_access_token")
        private String ig_access_token;
        @JsonProperty("fb_id")
        private String fb_id;
        @JsonProperty("ig_id")
        private String ig_id;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class checkDTO{
        private String jwt_token;
        private int count;
    }

}
