package fi.haagahelia.skijumping.web;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fi.haagahelia.skijumping.domain.Competition;
import fi.haagahelia.skijumping.domain.CompetitionRepository;
import fi.haagahelia.skijumping.domain.User;
import fi.haagahelia.skijumping.domain.UserRepository;


@Controller
public class SkiJumpingController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired 
	CompetitionRepository competitionRepository;
	
	@RequestMapping(value="/dashboard", method=RequestMethod.GET)
	public String skijumping(Model model) {
		//Get the name of the current user and pass it as a model
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
	    User user = userRepository.findByUsername(loggedInUser.getName());
	    String name = user.getName();
	    Competition competition = getNextCompetition();
	    model.addAttribute("competition", competition);
	    model.addAttribute("name", name);
		
		return "dashboard";
    }
	
	public Competition getNextCompetition() {
		Date today = new Date();
		Competition competition = new Competition();
		List<Competition> competitions = (List<Competition>) competitionRepository.findAllByOrderById();
		for(int i = 0; i < competitions.size(); i++) {
			Date competitionDate = competitions.get(i).getDate();
			if(today.before(competitionDate)) {
				competition = competitions.get(i);
				i =  competitions.size();
				}
		}
		return competition;
		
	}

}
