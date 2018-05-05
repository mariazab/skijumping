package fi.haagahelia.skijumping;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import fi.haagahelia.skijumping.domain.User;
import fi.haagahelia.skijumping.domain.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

	@Autowired
	private UserRepository repository;
	
	@Test
	public void createNewUser() {
		User user = new User("test", "$2a$10$VRlfA/vqlj1XJPEFUNclAOn84wZNbuKJIY22IXlWZLHlW3w2O0I.2", "test", "test@test.com", "USER");
		repository.save(user);
		assertThat(user.getId()).isNotNull();
	}
	
	@Test
	public void deleteUser() {
		User newUser = new User("test", "$2a$10$VRlfA/vqlj1XJPEFUNclAOn84wZNbuKJIY22IXlWZLHlW3w2O0I.2", "test", "test@test.com", "USER");
		repository.save(newUser);
		
		User user = repository.findByUsername("test");
		repository.delete(user);
		
		List<User> users = (List<User>) repository.findAll();
		assertThat(users.get(users.size() - 1).getUsername()).isNotEqualTo("test");
	}
	
	@Test
	public void findByUsername() {
		User newUser = new User("test", "$2a$10$VRlfA/vqlj1XJPEFUNclAOn84wZNbuKJIY22IXlWZLHlW3w2O0I.2", "test", "test@test.com", "USER");
		repository.save(newUser);
		
		User user = repository.findByUsername("test");
		assertThat(user.getEmail()).isEqualTo("test@test.com");
	}
}
