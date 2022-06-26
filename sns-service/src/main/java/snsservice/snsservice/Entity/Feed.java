package snsservice.snsservice.Entity;

import TemplateClass.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "feed")
@ToString
@Builder
public class Feed{
    @Id
    @Column(name = "feed_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", length = 2000, nullable = false)
    private String userId;

    @Column(name = "sns_type")
    private String snsType;

    @Column(name = "post_id", length = 2000, nullable = false)
    private String postId;

    @Column(name = "post_message")
    private String postMessage;

    @Column(name = "post_creator", length = 1000)
    private String postCreator;

    @Column(name = "post_create_time")
    private Timestamp postCreateTime;

    @Column(name = "post_image", length = 2000)
    private String postImage;



}
