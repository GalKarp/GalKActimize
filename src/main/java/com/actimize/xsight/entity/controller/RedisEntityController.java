package com.actimize.xsight.entity.controller;


//import com.actimize.xsight.entity.dao.RedisRepository;

import com.actimize.xsight.entity.dao.EntityRepository;
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
@RequestMapping("/redis")
public class RedisEntityController {

    @Autowired
    private EntityRepository redisRepository;

    @Autowired
    private EntityService entityService;


    @RequestMapping(value = "/entities",
            consumes = {"multipart/form-data"},
            produces = {"application/json"},
            method = RequestMethod.POST)
    public ResponseEntity<Void> insertEntities(@RequestPart(value = "entitiesFile") MultipartFile entitiesFile) {
        List<Entity> entities = new ArrayList<>(entityService.getEntitiesFromFile(entitiesFile));
        redisRepository.saveAll(entities);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/entities")
    public List<Entity> getEntities(
            @RequestParam(required = false) String sinceCreateDate,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String phone
    ) {
        List<Entity> entities;
        if(phone != null && lastName != null) {
            entities = redisRepository.findAllByLastNameAndPhones(lastName, phone);
        }else if(phone == null && lastName != null){
            entities = redisRepository.findAllByLastName(lastName);
        }else if(phone != null){
            entities = redisRepository.findAllByPhones(phone);
        }else{
            entities = redisRepository.findAll();
        }
        return entities.stream().filter(entity -> {
            if(sinceCreateDate != null ) {
                return entity.getCreateDate().compareTo(Instant.parse(sinceCreateDate)) > 0;
            }else return true;
        }).collect(Collectors.toList());
    }
}
