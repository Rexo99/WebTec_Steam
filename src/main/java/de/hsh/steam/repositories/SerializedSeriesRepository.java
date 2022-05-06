package de.hsh.steam.repositories;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;

import de.hsh.steam.entities.Genre;
import de.hsh.steam.entities.Series;
import de.hsh.steam.entities.Streamingprovider;
import de.hsh.steam.entities.User;

/**
 * Class SerializedSeriesRepository
 */
public class SerializedSeriesRepository extends SeriesRepository {

	public SerializedSeriesRepository(){
		User u = new User("marvin", "123");
		User l = new User("Luca", "1234");

		Series s1 = new Series("Dinotopia", 2, Genre.Action, Streamingprovider.AmazonPrime);
		Series s2 = new Series("Dinotopia reloaded",2,Genre.Documentary, Streamingprovider.Netflix);

		registerUser(u);
		registerUser(l);

		addOrModifySeries(s1);
		addOrModifySeries(s2);

		s1.putOnWatchListOfUser(u.getUsername());
		s2.putOnWatchListOfUser(u.getUsername());

		s1.putOnWatchListOfUser(l.getUsername());
		s2.putOnWatchListOfUser(l.getUsername());

	}

	private static SerializedSeriesRepository exemplar = null;

	private static String filename = "allSeriesData.ser";
	
	public static SerializedSeriesRepository getInstance() {
		if (exemplar == null) {
			exemplar = new SerializedSeriesRepository();
		}
		return exemplar;
	}

	/**
	 * 
	 */
	public void saveData() {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream("allSeriesData.ser");
			oos = new ObjectOutputStream(fos);
			oos.writeObject(this.allSeries);
			oos.writeObject(this.allUsers);	
			LOG.info("Write to Database " + filename);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				fos.close();
				oos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 */
	public void readData() {
		ObjectInputStream ois = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filename);
			ois = new ObjectInputStream(fis);
			this.allSeries = (ArrayList<Series>) ois.readObject();
			this.allUsers = (ArrayList<User>) ois.readObject();		
		} catch (IOException e) {
			LOG.info("Database does not exist: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			LOG.info("Database does not exist: " + e.getMessage() );
		} finally {
			try {
				if (fis != null)
					fis.close();
				if (ois != null)
					ois.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public boolean clear(){
		File file = new File(filename);
		try {
			return Files.deleteIfExists(file.toPath());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}
