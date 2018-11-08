package pl.codecity.main.controller.guest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {
	
	@RequestMapping
	public String form() {
		return "user/login";
	}
	
	@RequestMapping(params="failed")
	public String failed(Model model) {
		model.addAttribute("failed", true);
		return "user/login";
	}
}