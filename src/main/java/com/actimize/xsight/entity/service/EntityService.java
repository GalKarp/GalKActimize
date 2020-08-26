package com.actimize.xsight.entity.service;

import com.actimize.xsight.entity.controller.Entity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class EntityService {

    @Autowired
    private ObjectMapper mapper;

    /**
     * Helper for loading entities from a file
     * @param entitiesFile
     * @return
     */
    public List<Entity> getEntitiesFromFile(MultipartFile entitiesFile){
        List<Entity> entities = new ArrayList<>();
        try {
            new String(entitiesFile.getBytes())
                    .lines()
                    .filter(entityLine -> !entityLine.isBlank())
                    .forEach(entityLine -> {
                        try {
                            entities.add(mapper.readValue(entityLine, Entity.class));
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }catch (IOException exception){
            throw new RuntimeException(exception);
        }
        return entities;
    }

}
