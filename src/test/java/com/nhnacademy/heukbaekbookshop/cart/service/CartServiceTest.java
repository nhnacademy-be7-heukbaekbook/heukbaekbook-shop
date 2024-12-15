package com.nhnacademy.heukbaekbookshop.cart.service;

import com.nhnacademy.heukbaekbookshop.book.domain.Book;
import com.nhnacademy.heukbaekbookshop.book.repository.book.BookRepository;
import com.nhnacademy.heukbaekbookshop.cart.domain.Cart;
import com.nhnacademy.heukbaekbookshop.cart.domain.CartId;
import com.nhnacademy.heukbaekbookshop.cart.dto.CartBookSummaryResponse;
import com.nhnacademy.heukbaekbookshop.cart.dto.CartCreateRequest;
import com.nhnacademy.heukbaekbookshop.cart.repository.CartRepository;
import com.nhnacademy.heukbaekbookshop.cart.service.impl.CartServiceImpl;
import com.nhnacademy.heukbaekbookshop.memberset.member.domain.Member;
import com.nhnacademy.heukbaekbookshop.memberset.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @InjectMocks
    private CartServiceImpl cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MemberRepository memberRepository;

    @Test
    void createCart() {
        //given
        Long customerId = 1L;
        Long bookId1 = 1L;
        Long bookId2 = 2L;

        Member member = Member.builder()
                .name("홍길동")
                .build();

        Book book1 = new Book();
        book1.setId(bookId1);

        Book book2 = new Book();
        book2.setId(bookId2);

        List<CartCreateRequest> cartCreateRequests = List.of(
                new CartCreateRequest(bookId1, 1),
                new CartCreateRequest(bookId2, 2)
        );

        Cart existingCart = Cart.createCart(bookId1, customerId, book1, member, 2);

        when(memberRepository.findById(customerId)).thenReturn(Optional.of(member));
        when(cartRepository.findById(new CartId(customerId, bookId1))).thenReturn(Optional.of(existingCart));
        when(cartRepository.findById(new CartId(customerId, bookId2))).thenReturn(Optional.empty());
        when(bookRepository.findById(bookId2)).thenReturn(Optional.of(book2));

        //when
        cartService.createCart(customerId, cartCreateRequests);

        // then
        ArgumentCaptor<Cart> cartCaptor = ArgumentCaptor.forClass(Cart.class);

        Mockito.verify(cartRepository, Mockito.times(1)).save(cartCaptor.capture());

        List<Cart> savedCarts = cartCaptor.getAllValues();

        // Verify the first cart update
        assertEquals(customerId, existingCart.getCustomerId());
        assertEquals(1L, existingCart.getBookId());
        assertEquals(3, existingCart.getAmount()); // 2 (existing) + 1 (new quantity)

        // Verify the second cart creation
        Cart newCart = savedCarts.getFirst();
        assertEquals(customerId, newCart.getCustomerId());
        assertEquals(2L, newCart.getBookId());
        assertEquals(2, newCart.getAmount()); // new quantity
    }

    @Test
    void getCartBooks() {
        //given
        Long customerId = 1L;
        Long bookId1 = 1L;
        Long bookId2 = 2L;

        Member member = Member.builder()
                .name("홍길동")
                .build();

        Book book1 = new Book();
        book1.setId(bookId1);

        Book book2 = new Book();
        book2.setId(bookId2);

        Cart cart1 = Cart.createCart(bookId1, customerId, book1, member, 1);
        Cart cart2 = Cart.createCart(bookId2, customerId, book2, member, 2);

        List<Cart> carts = List.of(
                cart1,
                cart2
        );

        when(cartRepository.findAllByCustomerId(customerId)).thenReturn(carts);

        //when
        List<CartBookSummaryResponse> cartBooks = cartService.getCartBooks(customerId);

        //then
        assertEquals(2, cartBooks.size());
        assertEquals(bookId1, cartBooks.getFirst().bookId());
        verify(cartRepository).findAllByCustomerId(customerId);
    }
}