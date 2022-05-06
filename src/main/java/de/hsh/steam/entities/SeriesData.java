package de.hsh.steam.entities;

import java.io.Serializable;
import java.util.ArrayList;

public class SeriesData implements Serializable {
    private static final long serialVersionUID = 1;

    private String title;
    private int numberOfSeasons;
    private Genre genre;
    private Streamingprovider streamedBy;
    private ArrayList<String> seenByName = new ArrayList<String>();


    public SeriesData(Series s){
        this.title = s.getTitle();
        this.numberOfSeasons = s.getNumberOfSeasons();
        this.genre = s.getGenre();
        this.streamedBy = s.getStreamedBy();
        for(User u : s.getSeenBy()){
            seenByName.add(u.getUsername());
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(int numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Streamingprovider getStreamedBy() {
        return streamedBy;
    }

    public void setStreamedBy(Streamingprovider streamedBy) {
        this.streamedBy = streamedBy;
    }

    public void setSeenByName(ArrayList<String> seenByName) {
        this.seenByName = seenByName;
    }

    public ArrayList<String> getSeenByName() {
        return seenByName;
    }
}

