package pl.codecity.main.controller.admin.post;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.codecity.main.controller.support.HttpNotFoundException;
import pl.codecity.main.model.Article;
import pl.codecity.main.model.Page;
import pl.codecity.main.model.Post;
import pl.codecity.main.service.PostService;

import javax.inject.Inject;

@Controller
@RequestMapping(value="/{language}/posts/describe", method= RequestMethod.GET)
public class PostDescribeController {

	@Inject
	private PostService postService;

	@RequestMapping
	public String describe(
			@PathVariable String language,
			@RequestParam long id,
			RedirectAttributes redirectAttributes) {
		Post post = postService.getPostById(id, language);
		if (post == null) {
			throw new HttpNotFoundException();
		}

		redirectAttributes.addAttribute("id", post.getId());
		if (post instanceof Article) {
			return "redirect:/_admin/{language}/articles/describe?id={id}";
		}
		if (post instanceof Page) {
			return "redirect:/_admin/{language}/pages/describe?id={id}";
		}
		throw new HttpNotFoundException();
	}
}
