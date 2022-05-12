package de.hsh.steam.entities;

import java.io.Serializable;
import java.util.UUID;

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Class Rating
 */
@XmlRootElement(name = "rating")
public class Rating implements Serializable{

	private static final long serialVersionUID = -7806234457596021877L;
	
	private Score score;
	private String remark;
	private String ratingUser;
	private String ratedSeries;
	
	/**
	 * Constructor
	 * @param score
	 * @param remark
	 * @param ratingUser
	 * @param ratedSeries
	 */
	public Rating(Score score, String remark, String ratingUser, String ratedSeries) {
		super();
		this.score = score;
		this.remark = remark;
		this.ratingUser = ratingUser;
		this.ratedSeries = ratedSeries;
	}

	public Rating(){}
	
	/** 
	 * @return Score
	 */
	public Score getScore() {
		return score;
	}

	
	/** 
	 * @param score
	 */
	public void setScore(Score score) {
		this.score = score;
	}

	
	/** 
	 * @return String
	 */
	public String getRemark() {
		return remark;
	}

	
	/** 
	 * @param remark
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	
	/** 
	 * @return User
	 */
	public String getRatingUser() {
		return ratingUser;
	}

	
	/** 
	 * @param ofUser
	 */
	public void setRatingUser(String ofUser) {
		this.ratingUser = ofUser;
	}

	
	/** 
	 * @return Series
	 */
	public String getRatedSeries() {
		return ratedSeries;
	}

	/** 
	 * @param ofSeries
	 */
	public void setRatedSeries(String ofSeries) {
		this.ratedSeries = ofSeries;
	}


	@Override
	public String toString() {
		return "{" +
			" score='" + getScore() + "'" +
			", remark='" + getRemark() + "'" +
			", ratingUser='" + getRatingUser() + "'" +
			", ratedSeries='" + getRatedSeries() + "'" +
			"}";
	}

}
