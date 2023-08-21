package com.elenabyc.hikingapp.repositories;

import com.elenabyc.hikingapp.entities.Review;
import com.elenabyc.hikingapp.entities.Trail;
import com.elenabyc.hikingapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByUserEquals(User user);

    List<Review> findAllByTrailEquals(Trail trail);
}
