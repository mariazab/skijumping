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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	@RequestMapping(value = "/athletes", method = RequestMethod.GET)
	public String showAthletes(Model model) {
		
		//Getting the user's name
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByUsername(loggedInUser.getName());
		String name = user.getName();
		model.addAttribute("name", name);

		model.addAttribute("athletes", repository.findAllByOrderByLastName());
		return "athletes";
	}
	
	// Listing all favorite athletes
	@RequestMapping("/favorites")
	public String showFav(Model model) {
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByUsername(loggedInUser.getName());
		List<FavAthlete> favAthletes = favAthleteRepository.findByUserId(user.getId());
		model.addAttribute("name", user.getName());
		model.addAttribute("favAthletes", favAthletes);
		return "favorites";
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
		return "redirect:athletes";
	}
	
	// Adding athlete to user's favorites
	@RequestMapping("/addToFav/{id}")
	public String addToFav(@PathVariable("id") Long athleteId, Model model, RedirectAttributes redirectAttributes) {
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByUsername(loggedInUser.getName());
		Athlete athlete = repository.findOne(athleteId);

		// Checking if the user has already this athlete in favorites
		boolean isAlreadyFav = false;
		List<FavAthlete> favAthletes = favAthleteRepository.findByUserId(user.getId());
		for (int i = 0; i < favAthletes.size(); i++) {
			if (favAthletes.get(i).getAthlete().getId() == athleteId) {
				isAlreadyFav = true;
			}
		}
			
		String message = "Already in your favorites!";

		if (isAlreadyFav == false) {
			FavAthlete newFavAthlete = new FavAthlete(user, athlete);
			favAthleteRepository.save(newFavAthlete);
			message = "Added to favorites!";
		}

		//Adding message to show
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:../athletes";

	}

	// Deleting athlete
	@RequestMapping("/deleteAthlete/{id}")
	public String deleteAthlete(@PathVariable("id") Long athleteId, Model model,
			RedirectAttributes redirectAttributes) {
		repository.delete(athleteId);
		redirectAttributes.addFlashAttribute("message", "Deleted!");
		return "redirect:../athletes";
	}
	
	// Deleting athlete from favorites
	@RequestMapping("/deleteFromFav/{id}")
	public String deleteFromFav(@PathVariable("id") Long athleteId, Model model, RedirectAttributes redirectAttributes) {
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByUsername(loggedInUser.getName());
		Long userId = user.getId();
			
		List<FavAthlete> favAthletes = favAthleteRepository.findByUserId(userId);
			
		//Finding the athlete to delete
		for (int i = 0; i < favAthletes.size(); i++) {
			if (favAthletes.get(i).getAthlete().getId() == athleteId) {
				favAthleteRepository.delete(favAthletes.get(i));
			}
		}
		redirectAttributes.addFlashAttribute("message", "Deleted from favorites!");
		return "redirect:../favorites";

	}

	// Edit athlete
	@RequestMapping("/editAthlete/{id}")
	public String editAthlete(@PathVariable("id") Long athleteId, Model model) {
		model.addAttribute("athlete", repository.findOne(athleteId));
		return "editAthlete";
	}

	
	// RESTful service to get all athletes
	@RequestMapping(value = "/athletesApi", method = RequestMethod.GET)
	public @ResponseBody List<Athlete> athleteListRest() {
		return (List<Athlete>) repository.findAll();
	}

	// RESTful service to get athlete by id
	@RequestMapping(value = "/athletesApi/{id}", method = RequestMethod.GET)
	public @ResponseBody Athlete findAthleteRest(@PathVariable("id") Long athleteId) {
		return repository.findOne(athleteId);
	}

}
