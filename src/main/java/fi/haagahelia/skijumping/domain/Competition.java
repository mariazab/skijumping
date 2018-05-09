package fi.haagahelia.skijumping.domain;

import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "competition")
public class Competition {

	@Id
	@Column(name = "id")
	private Long id;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "hill_id")
	private Hill hill;
	
	@Column(name = "date")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private Calendar date;
	
	@Column(name ="type")
	private String type;

	@OneToMany (cascade = CascadeType.ALL,mappedBy = "competition")
	private List<Result2018> results2018;
	
	public Competition() {
	}

	public Competition(Long id, Hill hill, Calendar date, String type) {
		super();
		this.id = id;
		this.hill = hill;
		this.date = date;
		this.type = type;
	}

	public Competition(Long id, Hill hill) {
		super();
		this.id = id;
		this.hill = hill;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Hill getHill() {
		return hill;
	}

	public void setHill(Hill hill) {
		this.hill = hill;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
