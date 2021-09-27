package com.example.sdn6.repository;

import com.example.sdn6.entity.NoeudTypeEntity;
import com.example.sdn6.entity.ObjetFormationEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoeudTypeRepository extends CrudRepository<NoeudTypeEntity, Long> {
}
