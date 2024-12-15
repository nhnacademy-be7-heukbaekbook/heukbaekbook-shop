package com.nhnacademy.heukbaekbookshop.contributor.repository;

import com.nhnacademy.heukbaekbookshop.contributor.domain.ContributorRole;
import com.nhnacademy.heukbaekbookshop.contributor.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(ContributorRole roleName);
}
