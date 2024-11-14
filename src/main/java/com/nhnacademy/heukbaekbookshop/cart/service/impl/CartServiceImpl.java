package com.nhnacademy.heukbaekbookshop.cart.service.impl;

import com.nhnacademy.heukbaekbookshop.book.exception.book.BookNotFoundException;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
import com.nhnacademy.heukbaekbookshop.cart.domain.Cart;
import com.nhnacademy.heukbaekbookshop.cart.dto.CartCreateRequest;
import com.nhnacademy.heukbaekbookshop.cart.repository.CartRepository;
import com.nhnacademy.heukbaekbookshop.cart.service.CartService;
import com.nhnacademy.heukbaekbookshop.memberset.member.exception.MemberNotFoundException;
import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final BookRepository bookRepository;

    private final MemberRepository memberRepository;

    @Override
    public void createCart(Long customerId, List<CartCreateRequest> cartCreateRequests) {
        List<Cart> carts = cartCreateRequests.stream()
                .map(cartCreateRequest -> Cart.createCart(
                        cartCreateRequest.bookId(),
                        customerId,
                        bookRepository.findById(cartCreateRequest.bookId()).orElseThrow(() -> new BookNotFoundException(cartCreateRequest.bookId())),
                        memberRepository.findById(customerId).orElseThrow(MemberNotFoundException::new),
                        cartCreateRequest.quantity()
                ))
                .toList();

        cartRepository.saveAll(carts);
    }
}
