package snsservice.snsservice.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Builder
@AllArgsConstructor
public class FeedDTO {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class PostDTO{
        @JsonProperty("post_id")
        private String postID;
        @JsonProperty("post_message")
        private String postMessage;
        @JsonProperty("post_create_time")
        private Timestamp postCreateTime;
        @JsonProperty("post_creator")
        private String postCreator;
        @JsonProperty("post_image")
        private String postImage;
    }
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class responseDTO{
        @JsonProperty("post_id")
        private String postId;
        @JsonProperty("sns_type")
        private String snsType;
        @JsonProperty("post_message")
        private String postMessage;
        @JsonProperty("post_create_time")
        private String postCreateTime;
        @JsonProperty("post_creator")
        private String postCreator;
        @JsonProperty("post_image")
        private String postImage;

    }

}
