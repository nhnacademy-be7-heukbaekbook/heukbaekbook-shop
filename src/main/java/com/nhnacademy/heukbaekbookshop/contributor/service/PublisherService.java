package com.nhnacademy.heukbaekbookshop.contributor.service;

import com.nhnacademy.heukbaekbookshop.contributor.domain.Publisher;
import com.nhnacademy.heukbaekbookshop.contributor.dto.request.PublisherCreateRequest;
import com.nhnacademy.heukbaekbookshop.contributor.dto.request.PublisherUpdateRequest;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherCreateResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherDeleteResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherDetailResponse;
import com.nhnacademy.heukbaekbookshop.contributor.dto.response.PublisherUpdateResponse;
import com.nhnacademy.heukbaekbookshop.contributor.exception.PublisherAlreadyExistException;
import com.nhnacademy.heukbaekbookshop.contributor.exception.PublisherNotFoundException;
import com.nhnacademy.heukbaekbookshop.contributor.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;

    @Autowired
    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public PublisherCreateResponse registerPublisher(PublisherCreateRequest request) {
        if (publisherRepository.findByName(request.name()).isPresent()) {
            throw new PublisherAlreadyExistException("이미 존재하는 출판사입니다.");
        }
        Publisher publisher = new Publisher();
        publisher.setName(request.name());
        publisherRepository.save(publisher);
        return new PublisherCreateResponse(publisher.getName());
    }

    public Page<PublisherDetailResponse> getPublishers(Pageable pageable) {
        Page<Publisher> publishers = publisherRepository.findAll(pageable);
        return publishers.map(publisher ->
                new PublisherDetailResponse(publisher.getId(), publisher.getName()));
    }

    public PublisherUpdateResponse updatePublisher(Long publisherId, PublisherUpdateRequest request) {
        if (publisherRepository.findById(publisherId).isEmpty()) {
            throw new PublisherNotFoundException("출판사를 찾을 수 없습니다.");
        }
        Publisher publisher = publisherRepository.findById(publisherId).get();
        publisher.setName(request.name());
        publisherRepository.save(publisher);
        return new PublisherUpdateResponse(publisher.getName());
    }

    public PublisherDeleteResponse deletePublisher(Long publisherId) {
        if (publisherRepository.findById(publisherId).isEmpty()) {
            throw new PublisherNotFoundException("출판사를 찾을 수 없습니다.");
        }
        Publisher publisher = publisherRepository.findById(publisherId).get();
        publisherRepository.delete(publisher);
        return new PublisherDeleteResponse(publisher.getName());
    }

    public PublisherDetailResponse getPublisherById(Long publisherId) {
        if (publisherRepository.findById(publisherId).isEmpty()) {
            throw new PublisherNotFoundException("출판사를 찾을 수 없습니다.");
        }
        Publisher publisher = publisherRepository.findById(publisherId).get();
        return new PublisherDetailResponse(publisher.getId(), publisher.getName());
    }
}
