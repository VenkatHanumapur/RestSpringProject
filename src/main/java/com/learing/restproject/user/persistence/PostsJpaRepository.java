package com.learing.restproject.user.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learing.restproject.user.Post;

public interface PostsJpaRepository extends JpaRepository<Post,Integer>{

}
