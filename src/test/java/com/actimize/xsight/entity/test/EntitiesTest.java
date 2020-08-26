package com.actimize.xsight.entity.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EntitiesTest {

    @Autowired
    private MockMvc mvc;
    
    @Autowired
    private ResourceLoader resourceLoader;

    @BeforeAll
    public void uploadTestFile() throws Exception {
        var entitiesFile = new MockMultipartFile(
                "entitiesFile",
                "test-entities.txt",
                "text/csv",
                resourceLoader.getResource("classpath:/test-entities.txt").getInputStream());

        mvc.perform(MockMvcRequestBuilders
                .multipart("/entities")
                .file(entitiesFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void testGetAll() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/entities")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(100)));


    }


    @Test
    public void testGetByLastName() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/entities")
                .param("lastName", "Jackson")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(6)));
        mvc.perform(MockMvcRequestBuilders
                .get("/entities")
                .param("lastName", "Williams")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(6)));
        mvc.perform(MockMvcRequestBuilders
                .get("/entities")
                .param("lastName", "Anderson")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(8)));
        


    }
    @Test
    public void testGetPhone() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/entities")
                .param("phone", "056-7986164")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value("tt8398jy-1347-rngw-8746-60944xq2700d"));
        mvc.perform(MockMvcRequestBuilders
                .get("/entities")
                .param("phone", "057-5643298")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value("tt8398jy-1347-rngw-8746-60944xq2700d"));


    }
    @Test
    public void testGetByCreateDate() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/entities")
                .param("sinceCreateDate", "2014-05-01T00:00:00.000Z")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(56)));
    }
    
    @Test
    public void testGetByLastNameAndCreateDate() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/entities")
                .param("sinceCreateDate", "2014-05-01T00:00:00.000Z")
                .param("lastName", "Anderson")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));
    }
    
    
    
    
}
