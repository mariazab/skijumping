package fi.haagahelia.skijumping.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fi.haagahelia.skijumping.domain.Athlete;
import fi.haagahelia.skijumping.domain.AthleteRepository;
import fi.haagahelia.skijumping.domain.Competition;
import fi.haagahelia.skijumping.domain.CompetitionRepository;
import fi.haagahelia.skijumping.domain.FavAthlete;
import fi.haagahelia.skijumping.domain.FavAthleteRepository;
import fi.haagahelia.skijumping.domain.Hill;
import fi.haagahelia.skijumping.domain.HillRecord;
import fi.haagahelia.skijumping.domain.Result2018;
import fi.haagahelia.skijumping.domain.Result2018Repository;
import fi.haagahelia.skijumping.domain.User;
import fi.haagahelia.skijumping.domain.UserRepository;
import fi.haagahelia.skijumping.domain.WcStanding2018;
import fi.haagahelia.skijumping.domain.WcStanding2018Repository;
import fi.haagahelia.skijumping.mail.EmailService;
import fi.haagahelia.skijumping.mail.Mail;

@Controller
public class SkiJumpingController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	CompetitionRepository competitionRepository;

	@Autowired
	AthleteRepository athleteRepository;

	@Autowired
	Result2018Repository resultRepository;

	@Autowired
	WcStanding2018Repository standingsRepository;

	@Autowired
	FavAthleteRepository favAthleteRepository;

	@Autowired
	private EmailService emailService;

	//Redirecting to dashboard
	@RequestMapping("/")
	public String redirect(Model model) {
		return "redirect:dashboard";
	}

	//Showing the data on dashboard
	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public String skijumping(Model model) {
		// Get the name of the current user
		User user = getCurrentUser();
		String name = user.getName();
		model.addAttribute("name", name);

		// Get next competition
		Competition competition = getNextCompetition();
		model.addAttribute("competition", competition);

		// Get the last competition
		Competition lastCompetition = getLastCompetition();
		model.addAttribute("lastCompetition", lastCompetition);

		// Get the top 3 from last competition
		List<Result2018> topThree = getTopThreeCompetition();
		model.addAttribute("topThree", topThree);

		// Get top 3 from standings
		List<WcStanding2018> wcTopThree = getTopThreeStanding();
		model.addAttribute("wcTopThree", wcTopThree);

		// Get results of user's favorites
		List<Result2018> favResults = getResultsFavorite();
		model.addAttribute("favResults", favResults);

		// Standings of user's favorite athletes
		// Get user's favorites
		List<FavAthlete> favAthletes = favAthleteRepository.findByUserId(getCurrentUser().getId());
		// Get all standings
		List<WcStanding2018> standings = standingsRepository.findAllByOrderByPointsDesc();

		List<WcStanding2018> favStandings = new ArrayList<WcStanding2018>();
		List<Integer> favWcPosition = new ArrayList<Integer>();
		for (int i = 0; i < favAthletes.size(); i++) {
			for (int j = 0; j < standings.size(); j++) {
				if (standings.get(j).getAthlete().getId() == favAthletes.get(i).getAthlete().getId()) {
					favStandings.add(standings.get(j));
					favWcPosition.add(j);
				}
			}
		}
		model.addAttribute("favWcPosition", favWcPosition);
		model.addAttribute("favStandings", favStandings);

		// Get number of registered users
		List<User> users = (List<User>) userRepository.findAll();
		int numberOfUsers = users.size();
		model.addAttribute("numberOfUsers", numberOfUsers);

		return "dashboard";
	}

	@RequestMapping("/sendEmail")
	public String sendEmail(RedirectAttributes redirectAttributes) {
		// Get next competition
		Competition competition = getNextCompetition();

		// Get currently logged user and user's name and email
		User user = getCurrentUser();
		String name = user.getName();
		String email = user.getEmail();

		// Save information mentioned in email
		Hill hill = competition.getHill();
		
		DateFormat format = new SimpleDateFormat("HH:mm");
		System.out.println(format.format(competition.getDate().getTime()));

		// Set the basic content of the email
		String content = "Hi " + name + "!\n\nHow's everything going?\n"
				+ "\nHere is the information about next competition:\n" + "\nType: " + competition.getType()
				+ "\nDate: " + competition.getDate().get(Calendar.DAY_OF_MONTH) + "."
				+ competition.getDate().get(Calendar.MONTH) + '.' + competition.getDate().get(Calendar.YEAR)
				+ "\nStarting time: " + format.format(competition.getDate().getTime())
				+ "\nHill: " + hill.getName() + "\nCity: " + hill.getCity()
				+ "\nYear of construction: " + hill.getBuildYear() + "\nK-Point: " + hill.getkPoint() + "m\nHS: "
				+ hill.getHsPoint() + "m";
		

		//If the record is not null, add record info to email
		if (hill.getHillRecord() != null) {
				
			HillRecord record = hill.getHillRecord();
			Athlete athlete = record.getAthlete();

			content += "\nRecord: " + record.getLength() + "m, " + athlete.getFirstName() + " " + athlete.getLastName()
					+ " in " + record.getYear();
		}

		//Add the ending to email
		content += "\n\nMay the wind be in your favour! :)\n"
				+ "\n\n\nThis email has been sent automatically by worldofskijumping@gmail.com";

		Mail mail = new Mail();
		mail.setFrom("worldofskijumping@gmail.com");
		mail.setTo(email);
		mail.setSubject("Information about " + competition.getType() + " competition in "
				+ competition.getHill().getCity() + " HS" + competition.getHill().getHsPoint());
		mail.setContent(content);

		emailService.sendSimpleMessage(mail);

		//Adding message to show
		redirectAttributes.addFlashAttribute("message", "Email sent!");
		return "redirect:dashboard";
	}

	
	// Get and return currently logged user
	public User getCurrentUser() {
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByUsername(loggedInUser.getName());
		return user;
	}

	
	// Get and return next competition
	public Competition getNextCompetition() {
		Calendar today = Calendar.getInstance();
		Competition competition = new Competition();
		List<Competition> competitions = (List<Competition>) competitionRepository.findAllByOrderByDate();
		for (int i = 0; i < competitions.size(); i++) {
			Calendar competitionDate = competitions.get(i).getDate();
			
			//If the date of the competition equals today set it as next competition
			if (today.get(Calendar.DAY_OF_MONTH) == competitionDate.get(Calendar.DAY_OF_MONTH)
					&& today.get(Calendar.MONTH) == competitionDate.get(Calendar.MONTH)
					&& today.get(Calendar.YEAR) == competitionDate.get(Calendar.YEAR)) {
				competition = competitions.get(i);
				
				//Terminating loop
				i = competitions.size();

			//If the today's date is before the competition date, set it as next competition
			} else if (today.before(competitionDate)) {
				competition = competitions.get(i);
				
				//Terminate loop
				i = competitions.size();

			}
		}
		return competition;

	}

	// Get and return last competition
	public Competition getLastCompetition() {
		Calendar today = Calendar.getInstance();
		Competition competition = new Competition();
		List<Competition> competitions = (List<Competition>) competitionRepository.findAllByOrderByDate();
		for (int i = (competitions.size() - 1); i >= 0; i--) {
			Calendar competitionDate = competitions.get(i).getDate();
			
			//If today's date is after the competition date, set it as last competition
			if (today.after(competitionDate)) {
				if (today.get(Calendar.DAY_OF_MONTH) != competitionDate.get(Calendar.DAY_OF_MONTH)) {
					competition = competitions.get(i);
					
					//Terminate loop
					i = 0;
				}

			}
		}
		return competition;
	}

	// Find and return top 3 from last competition
	public List<Result2018> getTopThreeCompetition() {

		// Get last competition
		Competition lastCompetition = getLastCompetition();

		List<Result2018> topThree = new ArrayList<Result2018>();
		if (lastCompetition.getId() != null) {
			long competitionId = lastCompetition.getId();
			List<Result2018> results = resultRepository.findByCompetitionIdOrderByWcPoint(competitionId);

			// Add top 3 results from the competition
			for (int i = 0; i < results.size(); i++) {
				if (results.get(i).getWcPoint().getPosition() < 4) {
					topThree.add(results.get(i));
				}
			}
		}

		return topThree;
	}

	// Find and return results of user's favorites
	public List<Result2018> getResultsFavorite() {

		// Get last competition
		Competition lastCompetition = getLastCompetition();

		// Get user's favorites
		List<FavAthlete> favAthletes = favAthleteRepository.findByUserId(getCurrentUser().getId());
		List<Result2018> favResults = new ArrayList<Result2018>();

		if (lastCompetition.getId() != null) {
			// Get the results of lastCompetition
			long competitionId = lastCompetition.getId();
			List<Result2018> results = resultRepository.findByCompetitionIdOrderByWcPoint(competitionId);

			// Find the results of user's favorite athletes and add it to the List
			for (int i = 0; i < favAthletes.size(); i++) {
				for (int j = 0; j < results.size(); j++) {
					if (results.get(j).getAthlete().getId() == favAthletes.get(i).getAthlete().getId()) {
						favResults.add(results.get(j));
					}
				}
			}
		}

		return favResults;
	}

	// Find and return top 3 standings
	public List<WcStanding2018> getTopThreeStanding() {

		// Get top 3 from standings
		List<WcStanding2018> standings = standingsRepository.findAllByOrderByPointsDesc();
		List<WcStanding2018> wcTopThree = new ArrayList<WcStanding2018>();

		// If there is more than 3 standings, add top 3
		if (standings.size() > 3) {
			for (int i = 0; i < 3; i++) {
				wcTopThree.add(standings.get(i));
			}
		}
		// Else if there is less than 3 standings, add all of them
		else {
			for (int i = 0; i < standings.size(); i++) {
				wcTopThree.add(standings.get(i));
			}
		}

		return wcTopThree;
	}

}
