package net.de1mos.example.oauth2demo.repository;

import net.de1mos.example.oauth2demo.entities.TestEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StrangeDao extends CrudRepository<TestEntity, Long> {
}
