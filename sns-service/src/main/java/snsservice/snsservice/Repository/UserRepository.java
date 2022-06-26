package snsservice.snsservice.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import snsservice.snsservice.Entity.User;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    public User findOneByAccessToken(String access_token);
}
