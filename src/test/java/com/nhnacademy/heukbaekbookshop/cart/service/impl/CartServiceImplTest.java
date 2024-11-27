package com.nhnacademy.heukbaekbookshop.cart.service.impl;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.exception.book.BookNotFoundException;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
import com.nhnacademy.heukbaekbookshop.cart.domain.Cart;
import com.nhnacademy.heukbaekbookshop.cart.dto.CartCreateRequest;
import com.nhnacademy.heukbaekbookshop.cart.repository.CartRepository;
import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.memberset.member.exception.MemberNotFoundException;
import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CartServiceImplTest {

    @InjectMocks
    private CartServiceImpl cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCart_Success() {
        // Given
        Long customerId = 1L;
        Long bookId1 = 101L;


        CartCreateRequest cartRequest1 = new CartCreateRequest(bookId1, 2);


        Book book1 = mock(Book.class);


        Member member = mock(Member.class);

        when(bookRepository.findById(bookId1)).thenReturn(Optional.of(book1));

        when(memberRepository.findById(customerId)).thenReturn(Optional.of(member));

        // When
        cartService.createCart(customerId, List.of(cartRequest1));

        // Then
        verify(bookRepository, times(1)).findById(bookId1);
        verify(memberRepository, times(1)).findById(customerId);
        verify(cartRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testCreateCart_BookNotFound() {
        // Given
        Long customerId = 1L;
        Long bookId = 101L;

        CartCreateRequest cartRequest = new CartCreateRequest(bookId, 2);

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> cartService.createCart(customerId, List.of(cartRequest)))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining(String.valueOf(bookId));

        verify(bookRepository, times(1)).findById(bookId);
        verify(memberRepository, never()).findById(anyLong());
        verify(cartRepository, never()).saveAll(anyList());
    }

    @Test
    void testCreateCart_MemberNotFound() {
        // Given
        Long customerId = 1L;
        Long bookId = 101L;

        CartCreateRequest cartRequest = new CartCreateRequest(bookId, 2);

        Book book = mock(Book.class);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(memberRepository.findById(customerId)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> cartService.createCart(customerId, List.of(cartRequest)))
                .isInstanceOf(MemberNotFoundException.class);

        verify(bookRepository, times(1)).findById(bookId);
        verify(memberRepository, times(1)).findById(customerId);
        verify(cartRepository, never()).saveAll(anyList());
    }

    @Test
    void testCreateCart_SaveAll() {
        // Given
        Long customerId = 1L;
        Long bookId = 101L;

        CartCreateRequest cartRequest = new CartCreateRequest(bookId, 2);

        Book book = mock(Book.class);
        Member member = mock(Member.class);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(memberRepository.findById(customerId)).thenReturn(Optional.of(member));

        // When
        cartService.createCart(customerId, List.of(cartRequest));

        // Then
        verify(cartRepository, times(1)).saveAll(anyList());
    }
}
