package com.group1.gosports_jojo.repository;
import com.group1.gosports_jojo.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PictureRepository extends JpaRepository<Picture, Integer> {
    List<Picture> findByProduct_ProductId(Integer productId);


}
