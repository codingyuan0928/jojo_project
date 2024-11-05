package com.group1.gosports_jojo.repository;

import com.group1.gosports_jojo.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("select o from Order o where o.user.userId = ?1")
    List<Order> findByUser_UserId(Integer userId);


    @Override
    Optional<Order> findById(Integer integer);
}
