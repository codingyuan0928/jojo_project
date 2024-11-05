package com.group1.gosports_jojo.repository;
import com.group1.gosports_jojo.entity.MemberList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface
MemberListRepository extends JpaRepository<MemberList, Integer> {
    @Query(
        value = "SELECT g.group_id, g.group_name FROM member_lists m JOIN group_lists g ON m.group_id = g.group_id WHERE m.user_id = :userId",
        nativeQuery = true
    )
    List<Object[]> findGroupByUserIdNative(@Param("userId") Integer userId);

    @Query("select m.user.userId, m.user.username from MemberList m where m.groupId.groupId = ?1")
    List<Object[]> findByGroupId_GroupId(Integer groupId);


}