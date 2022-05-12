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
        return Response.ok().entity(entity).build();
    }

    /**
     * get Information of one spezific serie identified by serienname
     * @return infos of one serie
     */
    @GET
    @Path("/{Username}/{SerieId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSerie(@PathParam("Username")String username, @PathParam("SerieId")String serieId){
        Series s = SerializedSeriesRepository.getInstance().getSerieWithId(serieId);

        if ( s == null ){
            return Response.status(404).entity("Serie wurde nicht gefunden").build();
        } else {
            return Response.ok().entity(s).build();
        }
    }


    // ############################ Post JSON ############################

    /**
     * Log in
     * @param u User who wanna login
     * @return
     */
    @POST
    @Path("/LogIn")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response logIn(User u) {

        // check the log in data
        boolean correct_logIn = SteamService.getInstance().login(u.getUsername(), u.getPassword());
        if (correct_logIn) {
            return Response.ok().entity(SerializedSeriesRepository.getInstance().getAllSeriesOfUser(u.getUsername())).build();
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
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addSerie(@PathParam("Username") String username, Series s) {
        try {
            // wir müssen noch testen ob die Serie bereits existiert
            Series a = SerializedSeriesRepository.getInstance().addOrModifySeries(s);
            a.putOnWatchListOfUser(username);
            return Response.ok().entity(s.getTitle() + " wurde erstellt :).").build();
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchSerie(SeriesSearch s){
        return Response.ok().entity(SerializedSeriesRepository.getInstance().searchSeries(s.getUsername(), s.getGenre(), s.getProvider(), s.getScore())).build();
    }

    /**
     * get Information of one spezific serie identified by serienname
     * @return infos of one serie
     */
    @POST
    @Path("/{Username}/search/{Serienname}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchSerie(@PathParam("Username")String username, @PathParam("Serienname") String seriesname){
        ArrayList<Series> s = SerializedSeriesRepository.getInstance().getAllSerieWithTitle(seriesname);
        if (s.isEmpty()) return Response.status(404).build();
        for (int i = 0; i < s.size(); i++) {
            if (!s.get(i).isSeenBy(username)) {
                s.remove(i);
                i--;
            }
        }
        return Response.ok().entity(s).build();
    }

    /**
     *
     * @param username
     * @param r
     * @return
     */
    @POST
    @Path("{Username}/rating")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response rate(@PathParam("Username") String username, Rating r) {
        if (username.equals(r.getRatingUser())) {
            User u = SerializedSeriesRepository.getInstance().getUserObject(username);
            u.rate(r.getRatedSeries(), r.getScore(), r.getRemark());
            return Response.ok().entity("rate wurde erstellt").build();
        } else {
            return Response.status(400).entity("Das war dumm " + username + " ## r.User: " + r.getRatingUser() + " ## Serie: " + r.getRatedSeries()).build(); // User und rating user sind nicht identisch hier muss noch ein fehler code rausgesucht werden.
        }

    }


    /**
     * register a new User
     * @param user username and password
     * @return a response with statuscode
     */
    @POST
    @Path("registerUser")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(User user) {
        SerializedSeriesRepository repo = SerializedSeriesRepository.getInstance();
        if (repo.getUserObject(user.getUsername()) == null) {
            repo.registerUser(user);
            return Response.status(201).build();
        } else {
            return Response.status(409).build(); //409 conflict username already exists
        }
    }



    // ############################ Post XML ############################

    /**
     * Log in
     * @param u User who wanna login
     * @return
     */
    @POST
    @Path("/LogIn")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response logIn_XML(User u) {

        // check the log in data
        boolean correct_logIn = SteamService.getInstance().login(u.getUsername(), u.getPassword());
        if (correct_logIn) {
            return Response.ok().entity(SerializedSeriesRepository.getInstance().getAllSeriesOfUser(u.getUsername())).build();
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
    @Consumes(MediaType.APPLICATION_XML)
    public Response addSerie_XML(@PathParam("Username") String username, Series s) {
        try {
            // wir müssen noch testen ob die Serie bereits existiert
            Series a = SerializedSeriesRepository.getInstance().addOrModifySeries(s);
            a.putOnWatchListOfUser(username);
            return Response.ok().entity(s.getTitle() + " wurde erstellt :).").build();
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
    @Consumes(MediaType.APPLICATION_XML)
    //@Produces(MediaType.APPLICATION_XML)
    public Response searchSerie_XML(SeriesSearch s){
        return Response.ok().entity(SerializedSeriesRepository.getInstance().searchSeries(s.getUsername(), s.getGenre(), s.getProvider(), s.getScore())).build();
    }

    /**
     * get Information of one spezific serie identified by serienname
     * @return infos of one serie
     */
    @POST
    @Path("/{Username}/search/{Serienname}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response searchSerie_XML(@PathParam("Username")String username, @PathParam("Serienname") String seriesname){
        ArrayList<Series> s = SerializedSeriesRepository.getInstance().getAllSerieWithTitle(seriesname);
        if (s.isEmpty()) return Response.status(404).build();
        for (int i = 0; i < s.size(); i++) {
            if (!s.get(i).isSeenBy(username)) {
                s.remove(i);
                i--;
            }
        }
        return Response.ok().entity(s).build();
    }

    /**
     *
     * @param username
     * @param r
     * @return
     */
    @POST
    @Path("{Username}/rating")
    @Consumes(MediaType.APPLICATION_XML)
    public Response rate_XML(@PathParam("Username") String username, Rating r) {
        if (username.equals(r.getRatingUser())) {
            User u = SerializedSeriesRepository.getInstance().getUserObject(username);
            u.rate(r.getRatedSeries(), r.getScore(), r.getRemark());
            return Response.ok().entity("rate wurde erstellt").build();
        } else {
            return Response.status(400).entity("Das war dumm " + username + " ## r.User: " + r.getRatingUser() + " ## Serie: " + r.getRatedSeries()).build(); // User und rating user sind nicht identisch hier muss noch ein fehler code rausgesucht werden.
        }

    }

    /**
     * register a new User
     * @param user username and password
     * @return a response with statuscode
     */
    @POST
    @Path("registerUser")
    @Consumes(MediaType.APPLICATION_XML)
    public Response registerUser_XML(User user) {
        SerializedSeriesRepository repo = SerializedSeriesRepository.getInstance();
        if (repo.getUserObject(user.getUsername()) == null) {
            repo.registerUser(user);
            return Response.status(201).build();
        } else {
            return Response.status(409).build(); //409 conflict username already exists
        }
    }
}
