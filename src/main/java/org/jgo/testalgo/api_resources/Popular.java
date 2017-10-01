package org.jgo.testalgo.api_resources;

import org.jgo.testalgo.App;
import org.jgo.testalgo.timestamp.TimestampInterval;
import org.jgo.testalgo.timestamp.TimestampParseException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/popular/{partialTimestamp}")
public class Popular {


    /**
     * Returns top size popular queries for a partial timestamp
     * Return format is
     * {"queries": [
     *  {"somequery":34 },
     *  {"someotherquery": 23} ]
     * }
     *
     * @param partialTimestamp partial timestamp as a string
     * @param size number of popular queries to return
     * @throws TimestampParseException exception when parsing partial timestamp
     * @return a json response with popular queries
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPopularQueries(
            @PathParam("partialTimestamp") String partialTimestamp,
            @QueryParam("size") Integer size) throws TimestampParseException {

        try {
            TimestampInterval tsi = new TimestampInterval(partialTimestamp);
            Map<String,List<Map.Entry<String, Integer>>> resultsMap = new HashMap<>();

            Map<String, Integer> queriesMap = App.getQueriesTree().getPopularQueries(tsi, size);
            resultsMap.put("queries", new ArrayList<>(queriesMap.entrySet()));

            return Response.status(Response.Status.OK).entity(resultsMap).build();
        }
        catch (TimestampParseException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getLocalizedMessage()).build();
        }
    }
}
