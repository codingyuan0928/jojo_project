package com.group1.gosports_jojo.repository;
import com.group1.gosports_jojo.entity.Friend;
import com.group1.gosports_jojo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Integer> {
    // 查詢兩個使用者之間的好友邀請
    Optional<Friend> findByUserIdAndFriendId(User userId, User friendId);

    @Query(value = "SELECT * FROM friends f WHERE (f.user_id = :id OR f.friend_id = :id) AND f.status = :status", nativeQuery = true)
    List<Friend> findByUserIdOrFriendIdAndStatus(@Param("id") Integer id, @Param("status") String status);


}

