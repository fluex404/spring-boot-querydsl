package com.fluex404.springbootquerydsl.respository;

import com.fluex404.springbootquerydsl.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, Long>, QuerydslPredicateExecutor<UserData> {
}
