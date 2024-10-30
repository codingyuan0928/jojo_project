package com.group1.gosports_jojo.repository;
import com.group1.gosports_jojo.dto.chatroom.UserRelationshipDTO;
import com.group1.gosports_jojo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Override
    Optional<User> findById(Integer integer);

    @Override
    List<User> findAll();

    @Query("SELECT u.userId AS userId, u.username AS username, u.avatar AS avatar,\n" +
            "       CASE " +
            "           WHEN f1.status IS NOT NULL THEN f1.status\n" +
            "           WHEN f2.status IS NOT NULL THEN f2.status\n" +
            "           ELSE 'NO RELATION' " +
            "           END AS relationshipStatus, " +
            "       CASE " +
            "           WHEN f1.status = 'PENDING' THEN true " +
            "           WHEN f2.status = 'PENDING' THEN false " +
            "           ELSE ''" +
            "           END AS initiatorByMe " +
            "FROM User u " +
            "     LEFT JOIN Friend f1 ON u.userId = f1.friendId.userId AND f1.userId.userId = :currentUserId " +
            "     LEFT JOIN Friend f2 ON u.userId = f2.userId.userId AND f2.friendId.userId = :currentUserId " +
            "WHERE u.userId != :currentUserId "+
            "AND u.username LIKE CONCAT('%', :username, '%')")

    List<UserRelationshipDTO> findUserRelationshipsByUsername(Integer currentUserId, String username);

}