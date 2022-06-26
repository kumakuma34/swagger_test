package snsservice.snsservice.Repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import snsservice.snsservice.Entity.Feed;
import snsservice.snsservice.Entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedRepository extends JpaRepository<Feed,Long> {
    Optional<Feed> findOneByPostId(String post_id);
    @Query(value = "SELECT * FROM feed f WHERE f.user_id = ?1 or f.user_id = ?2 order by f.post_create_time DESC", nativeQuery = true)
    Optional<List<Feed>> findAllByUserAndSort(String fb_id, String ig_id);

    @Query(value = "SELECT * FROM feed a " +
            "where (a.user_id = ?1 OR a.user_id = ?2 ) AND a.post_create_time >= ?3 AND a.post_create_time < ?4 " +
            "order by a.post_create_time DESC", nativeQuery = true)
    Optional<List<Feed>> findAllByUserAndDateAndSort(String fb_id, String ig_id, String start, String end);

    @Query(value = "SELECT * FROM feed a " +
            "where (a.user_id = ?1 OR a.user_id = ?2 ) AND a.post_message like %?3% " +
            "order by a.post_create_time DESC", nativeQuery = true)
    Optional<List<Feed>> findAllByUserAndKewwordSort(String fb_id, String ig_id, String keyword);

}
