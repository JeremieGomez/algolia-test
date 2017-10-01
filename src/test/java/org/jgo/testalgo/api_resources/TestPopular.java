package org.jgo.testalgo.api_resources;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.jgo.testalgo.App;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TestPopular extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(Popular.class);
    }

    @BeforeClass
    public static void createTree() {
        App.buildQueriesTree(ClassLoader.getSystemResource("sample.tsv").getPath());
    }

    @Test
    public void testPopular() {

        String expectedResponse = "{queries=[{dup=4}, " +
                "{%22http%3A%2F%2Fwww.npr.org%2F2015%2F07%2F15%2F423203008%2Fcartel-author=3}, " +
                "{https%3A%2F%2Fphoenix.craigslist.org%2Fsearch%2Ffoa%3Fs%3D400=2}]}";

        Map<String, List<Map.Entry<String, Integer>>> response =
                target("popular/2015-08-01 00:04").queryParam("size", 3).request().get(Map.class);

        assertEquals(expectedResponse, response.toString());
    }

    @Test
    public void testPopularWrongTimestamp() {
        Response response = target("popular/2015-08-01 00:4").queryParam("size", 3).request().get(Response.class);
        assertEquals(400, response.getStatus());
    }
}