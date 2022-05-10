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

import com.sun.imageio.plugins.common.SimpleRenderedImage;
import de.hsh.steam.entities.*;
import de.hsh.steam.repositories.SerializedSeriesRepository;
import de.hsh.steam.repositories.SeriesRepository;
import de.hsh.steam.services.SteamService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.ArrayList;

/**
 * Template für Response
 * Pesponse.ok().entity( <response> ).build();
 */
@Path("serien")
public class SerienResource {

    @POST
    @Path("/test")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response test(User u) {
        return Response.ok().entity("id: " + u.getId() + " ## Username: " + u.getUsername()).build();
    }

    @POST
    @Path("/LogIn")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logIn(User u) {

        // check the log in data

        boolean correct_logIn = SteamService.getInstance().login(u.getUsername(), u.getPassword());

        if (correct_logIn) {
            return Response.ok().build();
        } else {
            return Response.status(401).build(); // 401 = unauthorisiert
        }
    }

    /**
     * show the home site of on user after log in
     *
     * @param username
     * @return all Seires of the acutal user
     */
    @GET
    @Path("/{Username}")
    public Response getHome(@PathParam("Username") String username) {
        return Response.ok().entity(SerializedSeriesRepository.getInstance().getAllSeriesOfUser(username)).build();
    }


    /**
     * create a new serie
     *
     * @return the new serie
     */
    @POST
    @Path("/{Username}/create_Series")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addSerie(@PathParam("Username") String username, Series s) {
        try {
            Series a = SerializedSeriesRepository.getInstance().addOrModifySeries(s);
            a.putOnWatchListOfUser(username);
            return Response.ok().entity(s.getTitle() + " wurde erstellt :).").build();
        } catch (Exception e) {
            return Response.status(409).build(); // hier muss noch ein andere Fehlercode rein
        }
    }


    /**
     * get Information of one spezific serie identified by serienname
     * @return infos of one serie
     */
    @GET
    @Path("/{Username}/{Seriesname}")
    public Response getSerie(@PathParam("Username") String username, @PathParam("Seriesname") String seriesname) {
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
}
