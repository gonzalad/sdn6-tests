package com.example.sdn6.projection;

import java.util.ArrayList;
import java.util.List;
import com.example.sdn6.entity.FormationEntity;
import com.example.sdn6.entity.NoeudMaquetteEntity;

public class NoeudAndFormationsParentesResult {
    private final NoeudMaquetteEntity om;
    private final List<FormationEntity> formationsParentes;

    public NoeudAndFormationsParentesResult(NoeudMaquetteEntity om, List<FormationEntity> formationsParentes) {
        this.om = om;
        this.formationsParentes = formationsParentes != null ? formationsParentes : new ArrayList<>();
    }

    public NoeudMaquetteEntity getOm() {
        return om;
    }

    public List<FormationEntity> getFormationsParentes() {
        return formationsParentes;
    }
}
