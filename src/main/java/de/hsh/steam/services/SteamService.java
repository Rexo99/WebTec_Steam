package de.hsh.steam.services;

import java.util.ArrayList;
import java.util.logging.Logger;

import de.hsh.steam.entities.*;
import de.hsh.steam.repositories.SerializedSeriesRepository;

/**
 * Class SteamService
 */
public class SteamService {

	private final static Logger LOG = Logger.getLogger(SteamService.class.getName());

	SerializedSeriesRepository repository = SerializedSeriesRepository.getInstance();

	private static SteamService exemplar = null;
	
	/** 
	 * @return SteamService
	 */
	public static SteamService getInstance() {
		if (exemplar == null) {
			exemplar = new SteamService();
		}
		return exemplar; 
	}


	/** 
	 * @param username
	 * @param pwd
	 * @return Boolean
	 */
	// gibt false zurück, falls es username schon gibt
	public Boolean newUser(String username, String pwd){
		repository.readData();
		if (repository.getUserObject(username) == null) {
			User u = new User(username, pwd);
			repository.registerUser(u);
			repository.saveData();
			return true;
		} else {
			return false;
		}
	}
	
	
	/** 
	 * @param username
	 * @param pwd
	 * @return Boolean
	 */
	public Boolean login(String username, String pwd){
		repository.readData();
		User u = repository.getUserObject(username);
		if (u!= null)
			return u.getPassword().equals(pwd);
		return false;
	}
	
	
	/** 
	 * @param title
	 * @param numberOfSeasons
	 * @param genre
	 * @param streamedBy
	 * @param username
	 * @param score
	 * @param remark
	 */
	public void addOrModifySeries(String title, int numberOfSeasons, Genre genre, Streamingprovider streamedBy, String username, Score score, String remark) {
		repository.readData();
		Series s = new Series(title, numberOfSeasons, genre, streamedBy);
		Series series = repository.addOrModifySeries(s);
		User u = repository.getUserObject(username);
		if (u != null) {
			series.putOnWatchListOfUser(u.getUsername());
			u.rate(series, score, remark);
		}
		LOG.info("Created Series " + series.getTitle() + " for user " + u.getUsername());
		repository.saveData();
	}
	
	
	/** 
	 * @param seriesname
	 * @param username
	 * @return Rating
	 */
	public Rating getRating(String seriesname, String username) {
		User u = repository.getUserObject(username);
		Series s = repository.getSeriesObjectFromName(seriesname);
		return u.ratingOf(s);
	}
	
	
	/** 
	 * @return ArrayList<Series>
	 */
	public ArrayList<Series> getAllSeries() {
		return repository.getAllSeries();
	}
	
	
	/** 
	 * @param title
	 * @return ArrayList<Series>
	 */
	public ArrayList<Series> getAllSeriesWithTitle(String title) {
		return repository.getAllSerieWithTitle(title);
	}
	
	
	/** 
	 * @param username
	 * @return ArrayList<Series>
	 */
	public ArrayList<Series> getAllSeriesOfUser(String username) {
		return repository.getAllSeriesOfUser(username);
	}
	
	
	/** 
	 * @param username
	 * @param genre
	 * @param streamingService
	 * @param score
	 * @return ArrayList<Series>
	 */
	// UND-Verknüpfung aller Suchkriterien
	// ein Suchkriterium, dass nicht berücksichtigt werden soll muss auf null gesetzt werden
	// wichtig: der Username muss gesetzt sein, wenn man nach scores sucht
	public ArrayList<Series> search(String username, Genre genre, Streamingprovider streamingService, Score score) {
		return repository.searchSeries(username, genre, streamingService, score);
	}

	/**
	 * Method to clear all Data
	 * @return
	 */
	public boolean clear(){
		return repository.clear();
	}

}
