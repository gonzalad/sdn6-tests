package com.example.sdn6.repository;

import java.util.List;
import com.example.sdn6.projection.NoeudAndFormationsParentesResult;

public interface CustomNoeudMaquetteRepository {

    List<NoeudAndFormationsParentesResult> findAllNoeudAndFormationsParentesByCode(String code);
}
