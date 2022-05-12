package de.hsh.steam.entities;

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Data Class for search function
 */
@XmlRootElement(name ="seriessearch")
public class SeriesSearch {
    private String username;
    private Genre genre;
    private Streamingprovider provider;
    private Score score;

    public Genre getGenre() {
        return genre;
    }
    public Streamingprovider getProvider() {
        return provider;
    }
    public Score getScore() {
        return score;
    }
    public String getUsername() {
        return username;
    }
    public void setGenre(Genre genre) {
        this.genre = genre;
    }
    public void setProvider(Streamingprovider provider) {
        this.provider = provider;
    }
    public void setScore(Score score) {
        this.score = score;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
