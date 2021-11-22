package com.example.sdn6;

import com.example.sdn6.SampleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleRepository extends CrudRepository<SampleEntity, Long> {
}
