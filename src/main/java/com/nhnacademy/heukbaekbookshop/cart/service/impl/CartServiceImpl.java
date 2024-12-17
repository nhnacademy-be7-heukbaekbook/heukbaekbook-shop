package com.nhnacademy.heukbaekbookshop.cart.service.impl;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.exception.book.BookNotFoundException;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
import com.nhnacademy.heukbaekbookshop.cart.domain.Cart;
import com.nhnacademy.heukbaekbookshop.cart.domain.CartId;
import com.nhnacademy.heukbaekbookshop.cart.dto.CartBookSummaryResponse;
import com.nhnacademy.heukbaekbookshop.cart.dto.CartCreateRequest;
import com.nhnacademy.heukbaekbookshop.cart.repository.CartRepository;
import com.nhnacademy.heukbaekbookshop.cart.service.CartService;
import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.memberset.member.exception.MemberNotFoundException;
import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final BookRepository bookRepository;

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void createCart(Long customerId, List<CartCreateRequest> cartCreateRequests) {
        Member member = memberRepository.findById(customerId)
                .orElseThrow(() -> new MemberNotFoundException(customerId));

        for (CartCreateRequest cartCreateRequest : cartCreateRequests) {
            Long bookId = cartCreateRequest.bookId();

            Optional<Cart> optionalCart = cartRepository.findById(new CartId(customerId, bookId));

            if (optionalCart.isPresent()) {
                Cart cart = optionalCart.get();
                cart.setAmount(cart.getAmount() + cartCreateRequest.quantity());
            } else {
                Book book = bookRepository.findById(bookId)
                        .orElseThrow(() -> new BookNotFoundException(bookId));
                Cart cart = Cart.createCart(
                        bookId,
                        customerId,
                        book,
                        member,
                        cartCreateRequest.quantity()
                );
                cartRepository.save(cart);
            }
        }
    }

    @Override
    public List<CartBookSummaryResponse> getCartBooks(Long customerId) {
        List<Cart> carts = cartRepository.findAllByCustomerId(customerId);
        List<CartBookSummaryResponse> cartBookSummaryResponses = new ArrayList<>();

        for (Cart cart : carts) {
            CartBookSummaryResponse cartBookSummaryResponse = new CartBookSummaryResponse(cart.getBookId(), cart.getAmount());
            cartBookSummaryResponses.add(cartBookSummaryResponse);
        }

        return cartBookSummaryResponses;
    }
}
