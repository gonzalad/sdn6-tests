package com.example.sdn6.spa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.example.sdn6.entity.NoeudMaquetteEntity;
import com.example.sdn6.repository.NoeudMaquetteRepository;
import org.springframework.stereotype.Component;

@Component
public class NoeudMaquetteServicePortAdapter {

    private final NoeudMaquetteRepository repository;

    public NoeudMaquetteServicePortAdapter(NoeudMaquetteRepository repository) {
        this.repository = repository;
    }

    public Optional<NoeudMaquetteEntity> lireNoeudAvecDescendance(UUID idDefinition) {
        List<NoeudMaquetteEntity> oms = repository.findArbre(idDefinition);
        // om.enfant est null si l'om n'a pas d'enfants
        // on valorise une liste vide pour que la couche adaptateur
        // puisse distinguer les cas:
        // - relation non lue
        // - relation non existante
        // oms.stream().filter(om -> om.getEnfants() == null).forEach(om -> om.setEnfants(new ArrayList<>()));
        return oms.stream().filter(it -> it.getIdDefinition().equals(idDefinition)).findAny();
    }
}
