package com.example.sdn6.repository;

import java.util.Optional;
import com.example.sdn6.entity.WidgetEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WidgetRepository extends CrudRepository<WidgetEntity, Long> {

    Optional<WidgetEntity> findByCode(String code);
}
