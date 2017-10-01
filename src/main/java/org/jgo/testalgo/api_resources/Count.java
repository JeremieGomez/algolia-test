package org.jgo.testalgo.api_resources;

import org.jgo.testalgo.App;
import org.jgo.testalgo.timestamp.TimestampInterval;
import org.jgo.testalgo.timestamp.TimestampParseException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/count/{partialTimestamp}")
public class Count {

    /**
     * Returns a json {"count":xx}, xx being the number of unique queries in the app queries tree
     * @param partialTimestamp partial timestamp on which to count the queries
     * @return a json {"count":xx}
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response count(@PathParam("partialTimestamp") String partialTimestamp) {
        try {
            TimestampInterval tsi= new TimestampInterval(partialTimestamp);
            Map<String, Integer> resultMap = new HashMap<>();
            resultMap.put("count", App.getQueriesTree().countQueries(tsi));
            return Response.status(Response.Status.OK).entity(resultMap).build();
        }
        catch (TimestampParseException e){
            return Response.status(Response.Status.BAD_REQUEST).entity("Error: " + e.getLocalizedMessage()).build();
        }
    }
}
