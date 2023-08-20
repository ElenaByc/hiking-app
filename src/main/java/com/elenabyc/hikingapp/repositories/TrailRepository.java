package com.elenabyc.hikingapp.repositories;

import com.elenabyc.hikingapp.entities.Trail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrailRepository extends JpaRepository<Trail, Long> {
}

