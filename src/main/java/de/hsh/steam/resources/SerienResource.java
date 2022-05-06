package de.hsh.steam.resources;

import com.sun.imageio.plugins.common.SimpleRenderedImage;
import de.hsh.steam.entities.Genre;
import de.hsh.steam.entities.Series;
import de.hsh.steam.entities.Streamingprovider;
import de.hsh.steam.repositories.SerializedSeriesRepository;
import de.hsh.steam.repositories.SeriesRepository;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

/**
 * Template für Response
 * Pesponse.ok().entity( <response> ).build();
 */
@Path("serien")
public class SerienResource {


    // This is just a Test application
    @GET
    @Path("/test2")
    public Response getTestSerie(){
        return Response.ok().entity(new Series("title", 3, Genre.Action, Streamingprovider.AmazonPrime)).build();
    }


    @GET
    @Path("/test3")
    public Response getAllSeries(){
        return Response.ok().entity(new Series("title", 4, Genre.Action, Streamingprovider.AmazonPrime)).build();
        // return Response.ok().entity(SerializedSeriesRepository.getInstance().getAllSeries()).build();
    }


    /**
     * show the home site of on user after log in
     * @param username
     * @return all Seires of the acutal user
     */
    @GET
    @Path("/{Username}/home")
    public Response getHome(@PathParam("Username")String username){
        return Response.ok().entity(SerializedSeriesRepository.getInstance().getAllSeriesOfUser(username)).build();
    }


    /**
     * create a new serie
     * @param title of the new serie
     * @param numOfSeasons of the new serie
     * @param genre of the new serie
     * @param sp Streamingprovider of the new serie
     * @return the new serie
     */
    @POST
    @Path("/{Username}/create_Series")
    public void addSerie(@PathParam("Username")String username, String title, int numOfSeasons, Genre genre, Streamingprovider sp){
        try{
            Series serie = new Series(title, numOfSeasons, genre, sp);
            SerializedSeriesRepository.getInstance().addOrModifySeries(serie);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


    /**
     * get Information of one spezific serie identified by serienname
     * @param serienname
     * @return infos of one serie
     */
    @GET
    @Path("/{Username}/create_Series")
    public Response getSerie(String serienname){
        return Response.ok().entity(SerializedSeriesRepository.getInstance().getSeriesObjectFromName(serienname)).build();
    }
}
