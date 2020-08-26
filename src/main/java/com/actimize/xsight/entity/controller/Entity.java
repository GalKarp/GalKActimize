package com.actimize.xsight.entity.controller;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.Instant;
import java.util.List;

@RedisHash("Entity")
@Data
public class Entity {
    private String id;
    private String firstName;
    @Indexed
    private String lastName;
    @Indexed
    private List<String> phones;
    private String country;
    private String city;
    private Instant createDate;
    
    
    
}
