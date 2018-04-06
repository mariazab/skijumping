package fi.haagahelia.skijumping.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "hill_record")
public class HillRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private long id;
	
	@OneToOne
	@JsonIgnore
	@JoinColumn(name = "hill_id")
	private Hill hill;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "athlete_id")
	private Athlete athlete;
	
	@Column(name = "length")
	private double length;
	
	@Column(name = "year")
	private int year;
	

	public HillRecord() {
		super();
	}

	public HillRecord(Hill hill, Athlete athlete, double length, int year) {
		super();
		this.hill = hill;
		this.athlete = athlete;
		this.length = length;
		this.year = year;
	}
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Hill getHill() {
		return hill;
	}

	public void setHill(Hill hill) {
		this.hill = hill;
	}

	public Athlete getAthlete() {
		return athlete;
	}

	public void setAthlete(Athlete athlete) {
		this.athlete = athlete;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
}
