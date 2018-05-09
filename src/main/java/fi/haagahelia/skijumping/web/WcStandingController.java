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

import fi.haagahelia.skijumping.domain.AthleteRepository;
import fi.haagahelia.skijumping.domain.User;
import fi.haagahelia.skijumping.domain.UserRepository;
import fi.haagahelia.skijumping.domain.WcStanding2018;
import fi.haagahelia.skijumping.domain.WcStanding2018Repository;

@Controller
public class WcStandingController {

	@Autowired
	WcStanding2018Repository repository;

	@Autowired
	AthleteRepository athleteRepository;

	@Autowired
	private UserRepository userRepository;

	// Showing all standings
	@RequestMapping("/standings")
	public String showStandings(Model model) {
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByUsername(loggedInUser.getName());
		String name = user.getName();
		model.addAttribute("name", name);

		model.addAttribute("standings", repository.findAllByOrderByPointsDesc());
		return "standings";
	}

	// Saving standing
	@RequestMapping("/saveStanding")
	public String saveStanding(WcStanding2018 standing) {
		repository.save(standing);
		return "redirect:standings";
	}

	// Editing standing
	@RequestMapping("/editStanding/{id}")
	public String editCompetition(@PathVariable("id") Long standingId, Model model) {
		model.addAttribute("standing", repository.findOne(standingId));
		model.addAttribute("athletes", athleteRepository.findAll());
		return "editStanding";
	}

	// Deleting standing
	@RequestMapping("/deleteStanding/{id}")
	public String deleteStanding(@PathVariable("id") Long standingId, Model model, RedirectAttributes redirectAttributes) {
		WcStanding2018 standing = repository.findOne(standingId);
		repository.delete(standing);
		
		//Adding message to show
		redirectAttributes.addFlashAttribute("message", "Deleted!");
		return "redirect:../standings";
	}
	

	// RESTful service to get standings
	@RequestMapping(value = "/standingsApi", method = RequestMethod.GET)
	public @ResponseBody List<WcStanding2018> standingsListRest() {
		return (List<WcStanding2018>) repository.findAll();
	}
}
