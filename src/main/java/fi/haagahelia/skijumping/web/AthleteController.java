package fi.haagahelia.skijumping.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fi.haagahelia.skijumping.domain.Athlete;
import fi.haagahelia.skijumping.domain.AthleteRepository;
import fi.haagahelia.skijumping.domain.FavAthlete;
import fi.haagahelia.skijumping.domain.FavAthleteRepository;
import fi.haagahelia.skijumping.domain.User;
import fi.haagahelia.skijumping.domain.UserRepository;
import fi.haagahelia.skijumping.domain.WcStanding2018Repository;

@Controller
public class AthleteController {

	@Autowired
	AthleteRepository repository;
	
	@Autowired
	WcStanding2018Repository wcStandingRepository;
	
	@Autowired
	FavAthleteRepository favAthleteRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	// Showing all athletes
	@RequestMapping(value="/athletes", method=RequestMethod.GET)
	public String showAthletes(Model model) {
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
	    User user = userRepository.findByUsername(loggedInUser.getName());
	    String name = user.getName();
	    model.addAttribute("name", name);
		
	    model.addAttribute("favAthletes", favAthleteRepository.findByUserId(user.getId()));
		model.addAttribute("athletes", repository.findAll());
		return "athletes";
	}
	
	// Adding new athlete
	@RequestMapping("/addAthlete")
	public String addAthlete(Model model) {
		model.addAttribute("athlete", new Athlete());
		return "addAthlete";
	}
	
	// Saving new athlete
	@RequestMapping("/saveAthlete")
	public String saveAthlete(Athlete athlete) {
		repository.save(athlete);
		/*createStanding(athlete);*/
		return "redirect:athletes";
	}
	
	// Deleting athlete
	@RequestMapping("/deleteAthlete/{id}")
	public String deleteAthlete(@PathVariable("id") Long athleteId, Model model) {
		repository.delete(athleteId);
		return "redirect:../athletes";
	}
	
	// Edit athlete
	@RequestMapping("/editAthlete/{id}") 
	public String editAthlete(@PathVariable("id") Long athleteId, Model model) {
		model.addAttribute("athlete", repository.findOne(athleteId));
		return "editAthlete";
	}
	
	//Adding athlete to user's favorites
	@RequestMapping("/addToFav/{id}")
	public String addToFav(@PathVariable("id") Long athleteId, Model model) {
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
	    User user = userRepository.findByUsername(loggedInUser.getName());
	    Athlete athlete = repository.findOne(athleteId);
	    FavAthlete favAthlete = new FavAthlete(user, athlete);
	    favAthleteRepository.save(favAthlete);
	    return "redirect:../athletes";
	    
	}
	
	//Listing all favorite athletes
	@RequestMapping("/favorites")
	public String showFav(Model model) {
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
	    User user = userRepository.findByUsername(loggedInUser.getName());
	    List<FavAthlete> favAthletes = favAthleteRepository.findByUserId(user.getId());
	    model.addAttribute("name", user.getName());
	    model.addAttribute("favAthletes", favAthletes);
	    return "favorites";
	}
	
	//Deleting athlete from favorites
	@RequestMapping("/deleteFromFav/{id}")
	public String deleteFromFav(@PathVariable("id") Long athleteId, Model model) {
			Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		    User user = userRepository.findByUsername(loggedInUser.getName());
		    Long userId = user.getId();
		    List<FavAthlete> favAthletes = favAthleteRepository.findByUserId(userId);
		    for(int i = 0; i < favAthletes.size(); i++) {
		    	if(favAthletes.get(i).getAthlete().getId() == athleteId) {
		    		favAthleteRepository.delete(favAthletes.get(i));
		    	}
		    }
		    return "redirect:../athletes";
		    
		}
	
	// RESTful service to get all athletes
	@RequestMapping(value="/athletesApi", method=RequestMethod.GET)
	public @ResponseBody List<Athlete> athleteListRest() {
		return (List<Athlete>) repository.findAll();
	}
	
	//RESTful service to get athlete by id
	@RequestMapping(value="/athletesApi/{id}", method=RequestMethod.GET)
	public @ResponseBody Athlete findAthleteRest(@PathVariable("id") Long athleteId) {
		return repository.findOne(athleteId);
	}
	
	
}
