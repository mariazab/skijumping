package fi.haagahelia.skijumping.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class SkiJumpingController {
	
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public String skijumping(Model model) {
          return "index";
    }
	

}
