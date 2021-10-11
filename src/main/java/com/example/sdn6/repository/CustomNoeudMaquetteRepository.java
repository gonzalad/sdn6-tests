package com.example.sdn6.repository;

import java.util.List;
import com.example.sdn6.entity.NoeudMaquetteEntity;
import com.example.sdn6.projection.NoeudAndFormationsParentesResult;

public interface CustomNoeudMaquetteRepository {

    List<NoeudAndFormationsParentesResult> findAllNoeudAndFormationsParentesByCode(String code);

    List<NoeudMaquetteEntity> findAllNoeudByCode(String code);

    List<NoeudMaquetteEntity> findAllNoeudByCodeWithNeo4jTemplate(String code);

    List<NoeudAndFormationsParentesResult> findAllNoeudAndFormationsParentesByCodeWithNeo4jTemplate(String code);

}