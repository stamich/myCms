package pl.codecity.main.controller.admin.analytics;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.codecity.main.model.Blog;
import pl.codecity.main.model.GoogleAnalytics;
import pl.codecity.main.service.BlogService;

import javax.inject.Inject;

@Controller
@RequestMapping("/{language}/analytics")
public class GoogleAnalyticsDeleteController {

	@Inject
	private BlogService blogService;

	@RequestMapping(method = RequestMethod.DELETE)
	public String delete(
			@PathVariable String language,
			RedirectAttributes redirectAttributes) {
		GoogleAnalytics deletedGoogleAnalytics = blogService.deleteGoogleAnalytics(Blog.DEFAULT_ID);
		redirectAttributes.addFlashAttribute("deletedGoogleAnalytics", deletedGoogleAnalytics);
		return "redirect:/_admin/{language}/analytics";
	}
}
