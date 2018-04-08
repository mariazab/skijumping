package fi.haagahelia.skijumping.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table (name = "hill")
public class Hill {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "country")
	private String country;
	
	@Column(name = "city")
	private String city;
	
	@Column(name = "hspoint")
	private int hsPoint;
	
	@Column(name = "kpoint")
	private int kPoint;
	
	@Column(name = "buildyear")
	private int buildYear;

	@OneToOne (cascade = CascadeType.ALL,mappedBy = "hill")
	@JoinColumn(name = "hill_record_id")
	private HillRecord hillRecord;
	
	@OneToMany (cascade = CascadeType.ALL,mappedBy = "hill")
	private List<Competition> competitions;
	
	public Hill() {
	}

	public Hill(String name, String country, String city, int hsPoint, int kPoint, int buildYear, HillRecord hillRecord) {
		super();
		this.name = name;
		this.country = country;
		this.city = city;
		this.hsPoint = hsPoint;
		this.kPoint = kPoint;
		this.buildYear = buildYear;
		this.hillRecord = hillRecord;
	}
	
	
	public Hill(String name, String country, String city, int hsPoint, int kPoint, int buildYear) {
		super();
		this.name = name;
		this.country = country;
		this.city = city;
		this.hsPoint = hsPoint;
		this.kPoint = kPoint;
		this.buildYear = buildYear;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getHsPoint() {
		return hsPoint;
	}

	public void setHsPoint(int hsPoint) {
		this.hsPoint = hsPoint;
	}

	public int getkPoint() {
		return kPoint;
	}

	public void setkPoint(int kPoint) {
		this.kPoint = kPoint;
	}

	public int getBuildYear() {
		return buildYear;
	}

	public void setBuildYear(int buildYear) {
		this.buildYear = buildYear;
	}

	public HillRecord getHillRecord() {
		return hillRecord;
	}

	public void setHillRecord(HillRecord hillRecord) {
		this.hillRecord = hillRecord;
	}
	
	
}
