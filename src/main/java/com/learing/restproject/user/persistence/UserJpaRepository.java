package com.learing.restproject.user.persistence;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learing.restproject.user.User;

public interface UserJpaRepository extends JpaRepository<User,Integer> {

	List<User> findByName(String name);

}
