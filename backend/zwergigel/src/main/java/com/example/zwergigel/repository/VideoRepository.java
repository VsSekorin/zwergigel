package com.example.zwergigel.repository;

import com.example.zwergigel.entity.User;
import com.example.zwergigel.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface VideoRepository extends JpaRepository<Video, Long> {

    Set<Video> findByUser(User user);

    Optional<Video> findByUrlAndUser(String url, User user);

    void deleteByIdAndUser(Long id, User user);

    void deleteAllByUser(User user);

    Optional<Video> findByIdAndUser(Long id, User user);
}
