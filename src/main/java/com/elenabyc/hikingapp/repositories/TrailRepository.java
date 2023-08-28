package com.elenabyc.hikingapp.repositories;

import com.elenabyc.hikingapp.entities.Trail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrailRepository extends JpaRepository<Trail, Long> {
    Optional<Trail> findByYelpAlias(String yelpAlias);
}
