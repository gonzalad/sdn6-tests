package com.example.sdn6.projection;

import java.util.List;
import com.example.sdn6.entity.FormationEntity;
import com.example.sdn6.entity.NoeudMaquetteEntity;

public interface NoeudAndFormationsParentesInterface {
    NoeudMaquetteEntity getOm();

    List<FormationEntity> getFormationsParentes();
}
