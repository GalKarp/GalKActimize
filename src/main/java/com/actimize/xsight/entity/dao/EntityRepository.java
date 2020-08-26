package com.actimize.xsight.entity.dao;

import com.actimize.xsight.entity.controller.Entity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntityRepository extends CrudRepository<Entity, Long>  {

    List<Entity> findAllByLastNameAndPhones(String lastName, String phone);
    List<Entity> findAllByLastName(String lastName);
    List<Entity> findAllByPhones(String phone);
    List<Entity> findAll();

}