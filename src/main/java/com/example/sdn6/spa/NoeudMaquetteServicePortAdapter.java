package com.example.sdn6.spa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.example.sdn6.entity.NoeudMaquetteEntity;
import com.example.sdn6.entity.noeud.NoeudViewEntity;
import com.example.sdn6.repository.NoeudMaquetteRepository;
import com.example.sdn6.repository.NoeudProjectionRepository;
import org.springframework.stereotype.Component;

@Component
public class NoeudMaquetteServicePortAdapter {

    private final NoeudMaquetteRepository repository;
    private final NoeudProjectionRepository noeudProjectionRepository;

    public NoeudMaquetteServicePortAdapter(NoeudMaquetteRepository repository, NoeudProjectionRepository noeudProjectionRepository) {
        this.repository = repository;
        this.noeudProjectionRepository = noeudProjectionRepository;
    }

    public Optional<NoeudMaquetteEntity> lireNoeudAvecDescendance(UUID idDefinition) {
        List<NoeudMaquetteEntity> oms = repository.findArbre(idDefinition);
        // om.enfant est null si l'om n'a pas d'enfants
        // on valorise une liste vide pour que la couche adaptateur
        // puisse distinguer les cas:
        // - relation non lue
        // - relation non existante
        oms.stream().filter(om -> om.getEnfants() == null).forEach(om -> om.setEnfants(new ArrayList<>()));
        return oms.stream().filter(it -> it.getIdDefinition().equals(idDefinition)).findAny();
    }

    public Optional<NoeudMaquetteEntity> lireNoeud(UUID idDefinition) {
        return repository.lireNoeud(idDefinition);
    }

    public Optional<NoeudViewEntity> lireNoeudProjection(UUID idDefinition) {
        return noeudProjectionRepository.lireNoeudProjection(idDefinition);
    }
}
