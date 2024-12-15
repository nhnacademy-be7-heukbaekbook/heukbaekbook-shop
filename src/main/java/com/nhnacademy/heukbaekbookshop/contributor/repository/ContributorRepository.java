package com.nhnacademy.heukbaekbookshop.contributor.repository;

import com.nhnacademy.heukbaekbookshop.contributor.domain.Contributor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContributorRepository extends JpaRepository<Contributor, Long> {
    Optional<Contributor> findByName(String name);

    Optional<Contributor> findByNameAndDescription(String name, String description);
}
