package com.example.sdn6.repository;

import com.example.sdn6.entity.FormationEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormationRepository extends CrudRepository<FormationEntity, Long> {
}
