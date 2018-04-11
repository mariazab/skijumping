package fi.haagahelia.skijumping.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table (name = "results2018")
public class Result2018 {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private long id;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "competition_id")
	private Competition competition;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "athlete_id")
	private Athlete athlete;
	
	@Column (name = "jump1")
	private double jump1;
	
	@Column (name = "jump2")
	private Double jump2;
	
	@Column (name = "points")
	private double points;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "position")
	private WcPoint wcPoint;

	public Result2018() {
	}

	public Result2018(Competition competition, Athlete athlete, double jump1, Double jump2, double points,
			WcPoint wcPoint) {
		super();
		this.competition = competition;
		this.athlete = athlete;
		this.jump1 = jump1;
		this.jump2 = jump2;
		this.points = points;
		this.wcPoint = wcPoint;
	}

	public Result2018(Competition competition, Athlete athlete, double jump1, double points) {
		super();
		this.competition = competition;
		this.athlete = athlete;
		this.jump1 = jump1;
		this.points = points;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Competition getCompetition() {
		return competition;
	}

	public void setCompetition(Competition competition) {
		this.competition = competition;
	}

	public Athlete getAthlete() {
		return athlete;
	}

	public void setAthlete(Athlete athlete) {
		this.athlete = athlete;
	}

	public double getJump1() {
		return jump1;
	}

	public void setJump1(double jump1) {
		this.jump1 = jump1;
	}

	public Double getJump2() {
		return jump2;
	}

	public void setJump2(Double jump2) {
		this.jump2 = jump2;
	}

	public double getPoints() {
		return points;
	}

	public void setPoints(double points) {
		this.points = points;
	}

	public WcPoint getWcPoint() {
		return wcPoint;
	}

	public void setWcPoint(WcPoint wcPoint) {
		this.wcPoint = wcPoint;
	}
	
}
