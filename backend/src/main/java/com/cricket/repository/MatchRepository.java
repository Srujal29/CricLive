package com.cricket.repository;

import com.cricket.entity.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<MatchEntity, Long> {

    Optional<MatchEntity> findByExternalId(String externalId);

    List<MatchEntity> findByMatchCategoryOrderByLastUpdatedDesc(String matchCategory);

    List<MatchEntity> findAllByOrderByLastUpdatedDesc();

    boolean existsByExternalId(String externalId);

    void deleteByExternalIdStartingWith(String prefix);
}
