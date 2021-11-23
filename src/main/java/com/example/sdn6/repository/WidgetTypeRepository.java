package com.example.sdn6.repository;

import java.util.Optional;
import com.example.sdn6.entity.WidgetTypeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WidgetTypeRepository extends CrudRepository<WidgetTypeEntity, Long> {

    Optional<WidgetTypeEntity> findByCode(String code);
}
