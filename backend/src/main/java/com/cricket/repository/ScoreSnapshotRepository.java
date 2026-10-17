package com.cricket.repository;

import com.cricket.entity.ScoreSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreSnapshotRepository extends JpaRepository<ScoreSnapshot, Long> {

    List<ScoreSnapshot> findByMatchIdOrderByTimestampDesc(Long matchId);
}
