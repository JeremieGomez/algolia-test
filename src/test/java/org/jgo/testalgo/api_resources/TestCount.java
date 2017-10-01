package org.jgo.testalgo.api_resources;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.jgo.testalgo.App;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import java.util.Map;

import static org.junit.Assert.*;

public class TestCount extends JerseyTest {


    @Override
    protected Application configure() {
        return new ResourceConfig(Count.class);
    }

    @BeforeClass
    public static void createTree() {
        App.buildQueriesTree(ClassLoader.getSystemResource("sample.tsv").getPath());
    }

    @Test
    public void testCount() {
        String expectedResponse = "{count=10}";
        Map<String,Integer> response = target("count/2015-08-01 00:04").request().get(Map.class);
        assertEquals(expectedResponse, response.toString());
    }

    @Test
    public void testCountWrongTimestamp() {
        Response response = target("count/2015-08-01 00:4").request().get(Response.class);
        assertEquals(400, response.getStatus());
    }

}
