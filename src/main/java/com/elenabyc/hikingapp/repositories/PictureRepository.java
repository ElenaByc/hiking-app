package com.elenabyc.hikingapp.repositories;

import com.elenabyc.hikingapp.entities.Picture;
import com.elenabyc.hikingapp.entities.Trail;
import com.elenabyc.hikingapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {
    List<Picture> findAllByUserEquals(User user);
    List<Picture> findAllByTrailEquals(Trail trail);
}
