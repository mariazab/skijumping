package fi.haagahelia.skijumping.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table (name = "wc_points")
public class WcPoint {

	@Id
	@Column(name = "position")
	private int position;
	
	@Column(name = "points")
	private int points;
	
	@OneToMany (cascade = CascadeType.ALL,mappedBy = "wcPoint")
	private List<Result2018> results2018;

	public WcPoint() {
		
	}
	
	public WcPoint(int position, int points) {
		super();
		this.position = position;
		this.points = points;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
	
}
