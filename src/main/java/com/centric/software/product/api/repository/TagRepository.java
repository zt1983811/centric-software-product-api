package com.centric.software.product.api.repository;

import com.centric.software.product.api.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long>{
    Optional<Tag> findByName(final String name);
}