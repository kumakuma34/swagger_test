package snsservice.snsservice.Entity;

import lombok.*;

import javax.persistence.*;
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
@ToString
@Builder
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fb_name", length = 2000, nullable = false)
    private String fbName;

    @Column(name = "fb_id", length = 2000, nullable = false)
    private String fbId;

    @Column(name = "fb_token", length = 2000, nullable = false)
    private String fbToken;

    @Column(name = "ig_id", length = 2000, nullable = false)
    private String igId;

    @Column(name = "ig_token", length = 2000, nullable = false)
    private String igToken;

    @Column(name = "access_token", length = 2000, nullable = false)
    private String accessToken;

    @Column(name = "refresh_token", length = 2000, nullable = false)
    private String refreshToken;

}
