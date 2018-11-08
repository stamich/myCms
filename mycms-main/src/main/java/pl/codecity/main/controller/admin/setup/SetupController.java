package pl.codecity.main.controller.admin.setup;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.codecity.main.controller.support.HttpForbiddenException;
import pl.codecity.main.model.Blog;
import pl.codecity.main.service.BlogService;
import pl.codecity.main.service.SetupService;

import javax.inject.Inject;
import javax.validation.Valid;

@Controller
@RequestMapping("/setup")
public class SetupController {

	@Inject
	private SetupService setupService;
	@Inject
	private BlogService blogService;

	@ModelAttribute("form")
	public SetupForm setupForm() {
		return new SetupForm();
	}

	@RequestMapping(method = RequestMethod.GET)
	public String setup() {
		Blog blog = blogService.getBlogById(Blog.DEFAULT_ID);
		if (blog != null) {
			throw new HttpForbiddenException();
		}
		return "setup";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(
			@Valid @ModelAttribute("form") SetupForm form,
			BindingResult result,
			RedirectAttributes redirectAttributes) {
		Blog blog = blogService.getBlogById(Blog.DEFAULT_ID);
		if (blog != null) {
			throw new HttpForbiddenException();
		}
		if (result.hasErrors()) {
			return "setup";
		}
		setupService.setup(form.buildSetupRequest());
		return "redirect:/_admin/login";
	}
}
