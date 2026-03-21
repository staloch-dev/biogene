package com.biogene.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biogene.domain.Laboratory;

public interface LaboratoryRepository extends JpaRepository<Laboratory, Long> {

    List<Laboratory> findByNameContainingIgnoreCase(String name);

}
