/*
## Programm Ausführen:

1. mvn package (compiled das Projekt neu)
2. mvn cargo:redeploy (hier wissen wir nicht genau was es macht, der Befehl muss aber ausgeführt werden.)


## Wie erstelle ich eine Post/Put Request:
- Es kann nur ein Parameter aus dem Body ausgelesen werden!
- Sollte es ein Objekt sein, wie Series oder User, dann muss dies als annotation angegeben werden.
        - @Consumes(MediaType.APPLICATION_JSON)


## Ablauf in Postman zum testen von POST oder PUT anfragen:

Einstellungen:
Post Abfrage -> row -> JSON (Postman)

Eingabe:
 {
    "title": "Hier ist Luca",
    "genre": "Action",
    "streamedBy":   "Netflix",
    "numberOfSeasons" : 4
}
 */

package de.hsh.steam.resources;

import de.hsh.steam.entities.*;
import de.hsh.steam.repositories.SerializedSeriesRepository;
import de.hsh.steam.services.SteamService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Template für Response
 * Pesponse.ok().entity( <response> ).build();
 */
@Path("serien")
public class SerienResource {

    // ############################ Get ############################

    /**
     * show the home site of on user after log in
     *
     * @param username
     * @return all Seires of the acutal user
     */
    @GET
    @Path("/{Username}")
    public Response getHome(@PathParam("Username") String username) {
        GenericEntity<List<Series>> entity = new GenericEntity<List<Series>>(SerializedSeriesRepository.getInstance().getAllSeriesOfUser(username)) {};
        return Response.ok().status(200).entity(entity).build();
    }

    /**
     * get Information of one spezific serie identified by serienname
     * @return infos of one serie
     */
    @GET
    @Path("/{Username}/{SeriesName}")
    public Response getSerie(@PathParam("Username")String username, @PathParam("SeriesName")String seriesName){
        Series s = SerializedSeriesRepository.getInstance().getSeriesObjectFromName(seriesName);

        if ( s == null ){
            return Response.status(404).entity("Serie wurde nicht gefunden").build();
        } else {
            return Response.ok().status(200).entity(s).build();
        }
    }


    // ############################ Post ############################

    /**
     * Log in
     * @param u User who wanna login
     * @return
     */
    @POST
    @Path("/LogIn")
    public Response logIn(User u) {

        // check the log in data
        boolean correct_logIn = SteamService.getInstance().login(u.getUsername(), u.getPassword());
        if (correct_logIn) {
            GenericEntity<List<Series>> entity = new GenericEntity<List<Series>>( SerializedSeriesRepository.getInstance().getAllSeriesOfUser(u.getUsername()) ) {};
            return Response.ok().status(200).entity(entity).build();
        } else {
            return Response.status(401).entity("Log In Daten waren falsch").build(); // 401 = unauthorisiert
        }
    }

    /**
     * create a new serie
     * @return the new serie
     */
    @POST
    @Path("/{Username}/create_Series")
    public Response addSerie(@PathParam("Username") String username, Series s) {
        try {
            Series a = SerializedSeriesRepository.getInstance().addOrModifySeries(s);
            a.putOnWatchListOfUser(username);
            return Response.ok().status(201).entity(s.getTitle() + " wurde erstellt.").build();
        } catch (Exception e) {
            return Response.status(409).build();
        }
    }

    /**
     * create a new serie
     * @return the new serie
     */
    @POST
    @Path("/{Username}/modify_Series")
    public Response modifySerie(@PathParam("Username") String username, Series s) {
        try {
            Series a = SerializedSeriesRepository.getInstance().addOrModifySeries(s);
            a.putOnWatchListOfUser(username);
            return Response.ok().status(200).entity(s.getTitle() + " wurde bearbeitet.").build();
        } catch (Exception e) {
            return Response.status(409).build(); // hier muss noch ein andere Fehlercode rein
        }
    }

    /**
     * seach a serie
     * @param s seaching Parameter for a serie
     * @return the seaching results
     */
    @POST
    @Path("/{Username}/search")
    public Response searchSerie(SeriesSearch s){
        GenericEntity<List<Series>> entity = new GenericEntity<List<Series>>( SerializedSeriesRepository.getInstance().searchSeries(s.getUsername(), s.getGenre(), s.getProvider(), s.getScore())) {};
        return Response.ok().status(200).entity( entity ).build();
    }

    /**
     * get Information of one spezific serie identified by serienname
     * @return infos of one serie
     */
    @GET
    @Path("/{Username}/searchByName/{Serienname}")
    public Response searchSerieByName(@PathParam("Username")String username, @PathParam("Serienname") String seriesname){
        ArrayList<Series> s = SerializedSeriesRepository.getInstance().getAllSerieWithTitle(seriesname);
        if (s.isEmpty()) return Response.status(404).build();
        for (int i = 0; i < s.size(); i++) {
            if (!s.get(i).isSeenBy(username)) {
                s.remove(i);
                i--;
            }
        }
        GenericEntity<List<Series>> entity = new GenericEntity<List<Series>>( s ) {};
        return Response.ok().status(200).entity(entity).build();
    }

    /**
     *
     * @param username
     * @param r
     * @return
     */
    @POST
    @Path("{Username}/rating")
    public Response rate(@PathParam("Username") String username, Rating r) {
        if (username.equals(r.getRatingUser())) {
            User u = SerializedSeriesRepository.getInstance().getUserObject(username);
            u.rate(r.getRatedSeries(), r.getScore(), r.getRemark());
            return Response.ok().status(201).entity("rate wurde erstellt").build();
        } else {
            return Response.status(409).entity("Rating konnte nicht erstellt werden").build();
        }
    }

    /**
     * register a new User
     * @param user username and password
     * @return a response with statuscode
     */
    @POST
    @Path("registerUser")
    public Response registerUser(User user) {
        SerializedSeriesRepository repo = SerializedSeriesRepository.getInstance();
        if (repo.getUserObject(user.getUsername()) == null) {
            repo.registerUser(user);
            return Response.status(201).build();
        } else {
            return Response.status(409).build(); //409 conflict username already exists
        }
    }

    /**
     * @param seriesname
     * @return ArrayList mit Rating Opjekten
     */
    @GET
    @Path("/{Username}/{seriesname}/rating")
    public Response getRating(@PathParam("seriesname") String seriesname) {
        SerializedSeriesRepository repo = SerializedSeriesRepository.getInstance();
        ArrayList<Rating> ratings = new ArrayList<>();
        ArrayList<User> allUsers = repo.getAllUsers();
        for (User user : allUsers) {
            Series testSeries = repo.getSeriesObjectFromName(seriesname);
            Rating rating = user.ratingOf(testSeries);
            if (rating != null) {
                ratings.add(rating);
            }
        }
        return Response.ok().status(200).entity(ratings).build();
    }
}
