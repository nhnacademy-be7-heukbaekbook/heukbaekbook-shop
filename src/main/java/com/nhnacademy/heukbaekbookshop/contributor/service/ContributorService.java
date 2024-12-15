package com.nhnacademy.heukbaekbookshop.contributor.service;

import com.nhnacademy.heukbaekbookshop.contributor.domain.Contributor;
import com.nhnacademy.heukbaekbookshop.contributor.dto.request.ContributorCreateRequest;
import com.nhnacademy.heukbaekbookshop.contributor.dto.request.ContributorUpdateRequest;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.ContributorCreateResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.ContributorDeleteResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.ContributorDetailResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.ContributorUpdateResponse;
import com.nhnacademy.heukbaekbookshop.contributor.exception.ContributorAlreadyExistException;
import com.nhnacademy.heukbaekbookshop.contributor.exception.ContributorNotFoundException;
import com.nhnacademy.heukbaekbookshop.contributor.repository.ContributorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContributorService {

    private final ContributorRepository contributorRepository;

    @Autowired
    public ContributorService(ContributorRepository contributorRepository) {
        this.contributorRepository = contributorRepository;
    }

    @Transactional
    public ContributorCreateResponse registerContributor(ContributorCreateRequest request) {
        if(contributorRepository.findByNameAndDescription(request.name(), request.description()).isPresent()) {
            throw new ContributorAlreadyExistException("Contributor already exists");
        }
        Contributor contributor = new Contributor();
        contributor.setName(request.name());
        contributor.setDescription(request.description());
        contributorRepository.save(contributor);

        return new ContributorCreateResponse(
                request.name(),
                request.description()
        );
    }

    @Transactional(readOnly = true)
    public Page<ContributorDetailResponse> getContributors(Pageable pageable) {
        return contributorRepository.findAll(pageable)
                .map(contributor -> new ContributorDetailResponse(
                        contributor.getId(),
                        contributor.getName(),
                        contributor.getDescription()
                ));
    }

    @Transactional(readOnly = true)
    public ContributorDetailResponse getContributor(Long contributorId) {
        Contributor contributor = contributorRepository.findById(contributorId)
                .orElseThrow(() -> new ContributorNotFoundException("Contributor not found"));
        return new ContributorDetailResponse(
                contributor.getId(),
                contributor.getName(),
                contributor.getDescription()
        );
    }

    @Transactional
    public ContributorUpdateResponse updateContributor(Long contributorId, ContributorUpdateRequest request) {
        Contributor contributor = contributorRepository.findById(contributorId)
                .orElseThrow(() -> new ContributorNotFoundException("Contributor not found"));
        contributor.setName(request.name());
        contributor.setDescription(request.description());
        contributorRepository.save(contributor);

        return new ContributorUpdateResponse(
                request.name(),
                request.description()
        );
    }

    @Transactional
    public ContributorDeleteResponse deleteContributor(Long contributorId) {
        Contributor contributor = contributorRepository.findById(contributorId)
                .orElseThrow(() -> new ContributorNotFoundException("Contributor not found"));
        contributorRepository.deleteById(contributorId);
        return new ContributorDeleteResponse(contributor.getName());
    }
}

