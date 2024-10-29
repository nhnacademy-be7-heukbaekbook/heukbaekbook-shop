package com.nhnacademy.heukbaekbookshop.contributor.repository;

import com.nhnacademy.heukbaekbookshop.contributor.domain.BookContributor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookContributorRepository extends JpaRepository<BookContributor, Long> {

}
