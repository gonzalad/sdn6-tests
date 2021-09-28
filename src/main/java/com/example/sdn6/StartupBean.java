package com.example.sdn6;

import java.util.List;
import java.util.UUID;
import com.example.sdn6.entity.ObjetFormationEntity;
import com.example.sdn6.repository.ObjetFormationRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class StartupBean implements ApplicationListener<ContextRefreshedEvent> {

    private final ObjetFormationRepository repository;

    public StartupBean(ObjetFormationRepository repository) {
        this.repository = repository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        /*repository.deleteAll();
        List<ObjetFormationEntity> entities = List.of(
            newObjetFormation("OF1"),
            newObjetFormation("OF2")
        );
        repository.saveAll(entities);*/
    }

    private ObjetFormationEntity newObjetFormation(String code) {
        ObjetFormationEntity entity = new ObjetFormationEntity();
        entity.setIdDefinition(UUID.randomUUID());
        entity.setCode(code);
        entity.setLibelleCourt("lib-" + code);
        return entity;
    }
}
