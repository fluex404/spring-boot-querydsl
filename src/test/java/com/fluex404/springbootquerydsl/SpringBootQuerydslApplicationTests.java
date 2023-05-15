package com.fluex404.springbootquerydsl;

import com.fluex404.springbootquerydsl.entity.QBook;
import com.fluex404.springbootquerydsl.entity.QBorrow;
import com.fluex404.springbootquerydsl.entity.QUserData;
import com.fluex404.springbootquerydsl.respository.UserDataRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class SpringBootQuerydslApplicationTests {
    @Autowired
    UserDataRepository userDataRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void ngetest_aja() {
        System.out.println("NGETEST AJA!");
    }

    @Test
    void ngetest_find_all_userdata() {
        var qUserData = QUserData.userData;
        var query = new JPAQuery(entityManager);

        query.from(qUserData).fetch().forEach(System.out::println);
    }

    @Test
    void ngetest_find_all_userdata_with_where() {
        var qUserData = QUserData.userData;
        var query = new JPAQuery(entityManager);

        query.from(qUserData).where(qUserData.name.like("%test%"));

        query.where(qUserData.email.eq("test1@gmail.com"));

        query.fetch().forEach(System.out::println);
    }

    @Test
    void ngetest_where_localdatetime() {
        var qBorrow = QBorrow.borrow;
        var query = new JPAQuery(entityManager);

        query.from(qBorrow).where(qBorrow.insertDate.after(LocalDateTime.of(2023, 05, 18, 0, 0, 0))
                .and(qBorrow.book.title.eq("programming with assembly")));
        query.fetch().forEach(System.out::println);
    }

    @Test
    void ngetest_querydsl_on_repository_userdata() {
        var qUserData = QUserData.userData;
        userDataRepository.findAll(
                qUserData.name.like("%test%"),
                qUserData.id.desc()
        ).forEach(System.out::println);
    }

    @Test
    void ngetest_querydsl_with_pageable() {
        var qUserData = QUserData.userData;
        userDataRepository.findAll(
                PageRequest.of(1, 1, Sort.by("email").descending())
        ).forEach(System.out::println);
    }

    @Test
    void ngetest_querydsl_with_two_books() {
        var book1 = QBook.book;
        var book2 = QBook.book;
        var query = new JPAQuery(entityManager);

        query.from(book1, book2).where(book1.author.eq("isa").or(book2.author.eq("josh")));

        query.fetch().forEach(System.out::println);
    }

    @Test
    void cobain_tuple() {
        var qBook = QBook.book;
        var query = new JPAQuery(entityManager);


        NumberPath<Long> aliasKuda = Expressions.numberPath(Long.class, "kuda");
        query.select(qBook.id, qBook.title, qBook.author, qBook.id.multiply(100).as(aliasKuda))
                .from(qBook)
                .orderBy(aliasKuda.desc())
        ;

//        query.fetch().forEach(System.out::println);
        List<Tuple> list = query.fetch();
        for (Tuple tuple : list) {
            System.out.println(tuple);
            System.out.println(tuple.get(3, Integer.class));
        }

    }

    @Test
    void cobain_Qcustome() {
        var qBook = new QBook("book");
        var qCustomer = new QBook("customer");
        var query = new JPAQuery(entityManager);

        query.from(qBook).fetch().forEach(System.out::println);
        query.from(qCustomer).fetch().forEach(System.out::println);

    }
}
