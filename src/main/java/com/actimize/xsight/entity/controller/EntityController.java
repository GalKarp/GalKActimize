package com.actimize.xsight.entity.controller;


import com.actimize.xsight.entity.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class EntityController {

    @Autowired
    private EntityService entityService;

    private List<Entity> entities = new ArrayList<>();

    @RequestMapping(value = "/entities",
            consumes = {"multipart/form-data"},
            produces = {"application/json"},
            method = RequestMethod.POST)
    public ResponseEntity<Void> insertEntities(@RequestPart(value = "entitiesFile") MultipartFile entitiesFile) {
        entities.addAll(entityService.getEntitiesFromFile(entitiesFile));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/entities")
    public List<Entity> getEntities(
            @RequestParam(required = false) String sinceCreateDate,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String phone
    ) {
        return entities.stream()
                .filter(entity -> {
                    if(lastName != null ) {
                        return entity.getLastName().equals(lastName);
                    }else return true;
                })
                .filter(entity -> {
                    if(phone != null ) {
                        return entity.getPhones().contains(phone);
                    }else return true;
                })
                .filter(entity -> {
                    if(sinceCreateDate != null ) {
                        return entity.getCreateDate().compareTo(Instant.parse(sinceCreateDate)) > 0;
                    }else return true;
                })

                .collect(Collectors.toList());
    }
}
