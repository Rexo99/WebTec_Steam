package de.hsh.steam.repositories;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import de.hsh.steam.entities.*;

// das SeriesDirectory ist eine Abstraktion der Datenbank
// es hält alle Daten vor und bietet Funktionen zum Anlegen, Ändern und Suchen von Objekten 
public abstract class SeriesRepository  {

	// private static final long serialVersionUID = -4283784255267828850L;

	protected final static Logger LOG = Logger.getLogger(SerializedSeriesRepository.class.getName());

	// Liste aller Serien und User
	protected ArrayList<Series> allSeries;
	protected ArrayList<User> allUsers;

	public SeriesRepository(){
		this.allSeries = new ArrayList<Series>();
		this.allUsers = new ArrayList<User>();
	}

	
	/** 
	 * @param
	 */
	// werden in Unterklasse Serializer implementiert
	abstract protected void saveData();
	abstract protected void readData();
	
	
	/** 
	 * @param u
	 */
	public void registerUser(User u) {
		this.allUsers.add(u);
		LOG.info("Register new User " + u.getUsername() + ". Total registered users: " + this.allUsers.size());
	}

	
	/** 
	 * @param username
	 * @return User
	 */
	public User getUserObject(String username) {
		User user = null;
		for (User u : allUsers){
			if (u.getUsername().equals(username) ) {
				user = u;
			}
		}
		return user;
	}
	
	
	/** 
	 * @return ArrayList<User>
	 */
	public ArrayList<User> getAllUsers() {
		return allUsers;
	}
	

	
	/** 
	 * @param s
	 * @return Series
	 */
	// gibt das Series object (das aus dem Directory (falls vorhanden oder das neue Objekt 
	public Series addOrModifySeries(Series s) {
		for (Series old : allSeries) {
			if ( old.getTitle().equals(s.getTitle()) ) {
				// Serie gibt es schon im Directory		
				old.setGenre(s.getGenre());
				old.setNumberOfSeasons(s.getNumberOfSeasons());
				old.setStreamedBy(s.getStreamedBy());
				return old;
			}
		}
		allSeries.add(s);
		return s;
	}

	
	/** 
	 * @return ArrayList<Series>
	 */
	public ArrayList<Series> getAllSeries() {
		return allSeries;
	}

	
	/** 
	 * @param username
	 * @return ArrayList<Series>
	 */
	public ArrayList<SeriesData> getAllSeriesOfUser(String username) {
		ArrayList<SeriesData> allSeriesOfUser = new ArrayList<SeriesData>();
		for (Series s : allSeries) {
			if (s.isSeenBy(username))
				allSeriesOfUser.add(new SeriesData(s));
		}
		return allSeriesOfUser;
	}
	
	
	/** 
	 * @param title
	 * @return ArrayList<Series>
	 */
	public ArrayList<Series> getAllSerieWithTitle(String title) {
		ArrayList<Series> allSeriesWithTitle = new ArrayList<Series>();
		for (Series s : allSeries) {
			if (s.getTitle().contains(title))
				allSeriesWithTitle.add(s);
		}
		return allSeriesWithTitle;
	}

	
	/** 
	 * @param username
	 * @param genre
	 * @param streamedBy
	 * @param score
	 * @return ArrayList<Series>
	 */
	public ArrayList<Series> searchSeries(String username, Genre genre, Streamingprovider streamedBy, Score score) {
		ArrayList<Series> searchResult = new ArrayList<Series>();
		if (username !="")
			for (Series s : allSeries) {
				if (isSearchCriteriaFulfilled(s, username, genre, streamedBy, score)) {
					searchResult.add(s);
				}
		}
		return searchResult;
	}
	
	/** 
	 * @param s
	 * @param username
	 * @param genre
	 * @param streamedBy
	 * @param score
	 * @return Boolean
	 */
	private Boolean isSearchCriteriaFulfilled(Series s, String username, Genre genre, Streamingprovider streamedBy, Score score) {
		if (username != null && !s.isSeenBy(username) )
			return false;
		if (genre != null && s.getGenre() != genre)
			return false;
		if (streamedBy != null && s.getStreamedBy() != streamedBy)
			return false;
		if (score != null) {
			User u = this.getUserObject(username);
			if (u!= null ) {
				Rating r = u.ratingOf(s);
				if (r != null && r.getScore() != score) {
					return false;
				}
			}
		}	
		return true;
	}
	
	
	/** 
	 * @param seriesname
	 * @return Series
	 */
	public Series getSeriesObjectFromName(String seriesname) {
		for (Series s : allSeries){
			if (s.getTitle().equals(seriesname) ) {
				return s;
			}
		}
		return null;
	}


	
	
	
	// ------------ zum Testen: -------------------
	public void dumpRepository() {
		System.out.println();
		System.out.println("######### data dump ############");
		for (User u: this.allUsers)
			System.out.println(u);
		for (Series s: this.allSeries) {
			System.out.println(s);
		}
	}
	
	/** 
	 * @param username
	 */
	public String printAllSeries(String username) {
		User u = this.getUserObject(username);
		String list = "";
		for (Series s: this.allSeries) {
			if (s.isSeenBy(username)){
				list += "Title: " + s.getTitle() + " 	Score: " + u.ratingOf(s).getScore() + "\n";
			}
		}
		return list;
	}


	@Override
	public String toString() {
		return "{" +
			" allSeries='" + getAllSeries() + "'" +
			", allUsers='" + getAllUsers() + "'" +
			"}";
	}
	
	
}













