package com.actimize.xsight.entity.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EntitiesIT {

    public static final String SERVER_URL = "http://localhost:8080/redis/entities";

    private CloseableHttpClient httpClient = HttpClients.createDefault();
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public void uploadTestFile() throws Exception {
        File file = new File(getClass().getResource("/test-entities.txt").toURI());

        HttpPost post = new HttpPost(SERVER_URL);
        FileBody fileBody = new FileBody(file, ContentType.TEXT_PLAIN);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addPart("entitiesFile", fileBody);
        HttpEntity entity = builder.build();

        post.setEntity(entity);
        CloseableHttpResponse response = httpClient.execute(post);
        Assertions.assertEquals(HttpStatus.SC_OK, response.getCode());

    }

    @Test
    public void testGetAll() throws Exception {
        HttpGet httpGet = new HttpGet(SERVER_URL);
        CloseableHttpResponse response = httpClient.execute(httpGet);

        Assertions.assertEquals(HttpStatus.SC_OK, response.getCode());

        String responseStr = EntityUtils.toString(response.getEntity());
        JsonNode jsonNode = objectMapper.readTree(responseStr);
        Assertions.assertEquals(100, jsonNode.size());

    }


    @Test
    public void testGetByLastName() throws Exception {
        JsonNode jsonNode1 = executeGet(Arrays.asList(new BasicNameValuePair("lastName", "Jackson")));
        Assertions.assertEquals(6, jsonNode1.size());

        JsonNode jsonNode2 = executeGet(Arrays.asList(new BasicNameValuePair("lastName", "Williams")));
        Assertions.assertEquals(6, jsonNode2.size());

        JsonNode jsonNode3 = executeGet(Arrays.asList(new BasicNameValuePair("lastName", "Anderson")));
        Assertions.assertEquals(8, jsonNode3.size());
    }

    @Test
    public void testGetPhone() throws Exception {
        JsonNode jsonNode1 = executeGet(Arrays.asList(new BasicNameValuePair("phone", "056-7986164")));
        Assertions.assertEquals(1, jsonNode1.size());

        JsonNode jsonNode2 = executeGet(Arrays.asList(new BasicNameValuePair("phone", "057-5643298")));
        Assertions.assertEquals(1, jsonNode2.size());

    }

    @Test
    public void testGetByCreateDate() throws Exception {
        JsonNode jsonNode1 = executeGet(Arrays.asList(new BasicNameValuePair("sinceCreateDate", "2014-05-01T00:00:00.000Z")));
        Assertions.assertEquals(56, jsonNode1.size());


    }

    @Test
    public void testGetByLastNameAndCreateDate() throws Exception {
        JsonNode jsonNode1 = executeGet(Arrays.asList(
                new BasicNameValuePair("lastName", "Anderson"),
                new BasicNameValuePair("sinceCreateDate", "2014-05-01T00:00:00.000Z")
        ));
        Assertions.assertEquals(4, jsonNode1.size());

    }

    private JsonNode executeGet(List<NameValuePair> params) throws URISyntaxException, IOException, ParseException {
        URI uri = new URIBuilder(SERVER_URL)
                .addParameters(params)
                .build();
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = httpClient.execute(httpGet);

        Assertions.assertEquals(HttpStatus.SC_OK, response.getCode());

        String responseStr = EntityUtils.toString(response.getEntity());
        return objectMapper.readTree(responseStr);
    }
}
