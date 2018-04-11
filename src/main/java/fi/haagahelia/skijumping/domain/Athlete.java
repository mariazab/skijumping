package fi.haagahelia.skijumping.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.OneToOne;

@Entity
@Table(name = "athlete")
public class Athlete {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private long id;
	
	@Column(name = "firstname")
	private String firstName;
	
	@Column(name = "lastname")
	private String lastName;
	
	@Column(name = "nationality")
	private String nationality;
	
	@Column(name = "birthyear")
	private int birthYear;
	
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "athlete")
	private List<HillRecord> hillRecord;
	
	@OneToMany (cascade = CascadeType.ALL,mappedBy = "athlete")
	private List<Result2018> results2018;
	
	@OneToOne (cascade = CascadeType.ALL,mappedBy = "athlete")
	private WcStanding2018 wcStanding2018;
		
	public Athlete() {
	}

	public Athlete(String firstName, String lastName, String nationality, int birthYear) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.nationality = nationality;
		this.birthYear = birthYear;
	}

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public int getBirthYear() {
		return birthYear;
	}

	public void setBirthYear(int birthYear) {
		this.birthYear = birthYear;
	}

	public List<HillRecord> getHillRecord() {
		return hillRecord;
	}

	public void setHillRecord(List<HillRecord> hillRecord) {
		this.hillRecord = hillRecord;
	}
	
	
}
