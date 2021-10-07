package com.example.sdn6.repository;

import com.example.sdn6.entity.NoeudMaquetteEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoeudMaquetteRepository extends CrudRepository<NoeudMaquetteEntity, Long>, CustomNoeudMaquetteRepository {
}
