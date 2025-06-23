package com.ideamanagement.repository;

import com.ideamanagement.entity.DatabaseTracker;
import com.ideamanagement.entity.DatabaseTracker.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DatabaseTrackerRepository extends JpaRepository<DatabaseTracker, Integer> {
    Optional<DatabaseTracker> findByName(String name);
    Page<DatabaseTracker> findByStatus(Status status, Pageable pageable);
    Page<DatabaseTracker> findByVersion(String version, Pageable pageable);
    boolean existsByName(String name);
} 