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

@Entity
@Table (name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "username", nullable = false, unique = true)
	private String username;
	
	@Column(name = "password", nullable = false)
	private String passwordHash;
	
	@Column(name = "name", nullable = false, unique = true)
	private String name;
	
	@Column(name = "email", nullable = false)
	private String email;
	
	@Column(name = "role", nullable = false, unique = true)
	private String role;
	
	@OneToMany (cascade = CascadeType.ALL,mappedBy = "athlete")
	private List<FavAthlete> favAthlete;
	
	public User() {
		
	}

	public User(String username, String passwordHash, String name, String email, String role) {
		super();
		this.username = username;
		this.passwordHash = passwordHash;
		this.name = name;
		this.email = email;
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	
}
