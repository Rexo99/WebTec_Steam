package de.hsh.steam.resources;

import de.hsh.steam.entities.Genre;
import de.hsh.steam.entities.Series;
import de.hsh.steam.entities.Streamingprovider;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("serien")
public class SerienResource {
    @GET
    @Path("/test2")
    public Response get(){
        return Response.ok().entity(new Series("title", 3, Genre.Action, Streamingprovider.AmazonPrime)).build();
    }
}
