/*
Programm Ausführen:

1. mvn package (compiled das Projekt neu)
2. mvn cargo:redeploy (hier wissen wir nicht genau was es macht, der Befehl muss aber ausgeführt werden.)
 */

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
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

import java.util.ArrayList;

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
    @Path("/{Username}")
    public Response getHome(@PathParam("Username")String username){
        return Response.ok().entity(SerializedSeriesRepository.getInstance().getAllSeriesOfUser(username)).build();
    }


    /**
     * create a new serie
     * @return the new serie
     */
/*
    @POST
    @Path("/{Username}/create_Series")
    public Response addSerie(@PathParam("Username")String username, Genre genre, int numOfSeasons, Streamingprovider sp, String title){
        try{
            Series a = SerializedSeriesRepository.getInstance().addOrModifySeries(new Series(title, numOfSeasons, genre, sp));
            a.putOnWatchListOfUser(username);
            return Response.ok().build();
        } catch (Exception e){
            return Response.status(409).build(); // hier muss noch ein andere Fehlercode rein
        }
    }
    */




    @GET
    @Path("/{Username}/create_Series?genre={genre}&numOfSeasons={numOfS}&sp={sp}&title={title}")
    public Response addSerie(@PathParam("Username")String username, @PathParam("genre")Genre genre, @PathParam("numOfS")int numS,@PathParam("sp")Streamingprovider spr, @PathParam("title")String title){
        try{
            //Serie wird erstellt.
            Series a = SerializedSeriesRepository.getInstance().addOrModifySeries(new Series(title,numS, genre,spr));

            // Serie wird einem User zugeordnet.
            a.putOnWatchListOfUser(username);

            // keine ahnung was hier gemacht wird
            /*
            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
            uriBuilder.path(s.getTitle());
            return Response.created(uriBuilder.build()).entity(a).build();
             */
            return Response.ok().build();
        } catch (Exception e){
            return Response.status(409).build();
        }
    }



    /**
     * get Information of one spezific serie identified by serienname
     * @return infos of one serie
     */
    @GET
    @Path("/{Username}/{Seriesname}")
    public Response getSerie(@PathParam("Username")String username, @PathParam("Seriesname")String seriesname){
        ArrayList<Series> s = SerializedSeriesRepository.getInstance().getAllSerieWithTitle(seriesname);
        if(s.isEmpty()) return Response.status(404).build();
        for (int i = 0 ; i < s.size(); i++){
            if(!s.get(i).isSeenBy(username)){
                s.remove(i);
                i--;
            }
        }
        return Response.ok().entity(s).build();
    }

}
