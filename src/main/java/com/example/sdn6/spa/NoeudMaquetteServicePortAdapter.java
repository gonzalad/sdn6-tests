package com.example.sdn6.spa;

import java.util.Optional;
import java.util.UUID;
import com.example.sdn6.entity.NoeudMaquetteEntity;
import com.example.sdn6.projection.NoeudProjection;
import com.example.sdn6.repository.NoeudMaquetteRepository;
import org.springframework.stereotype.Component;

@Component
public class NoeudMaquetteServicePortAdapter {

    private final NoeudMaquetteRepository repository;

    public NoeudMaquetteServicePortAdapter(NoeudMaquetteRepository repository) {
        this.repository = repository;
    }

    public Optional<NoeudMaquetteEntity> lireNoeud(UUID idDefinition) {
        return repository.findByIdDefinition(idDefinition);
    }

    public Optional<NoeudProjection> findProjectionByIdDefinition(UUID idDefinition) {
        return repository.findProjectionByIdDefinition(idDefinition);
    }
}
