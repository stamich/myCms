package pl.codecity.main.controller.guest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.codecity.main.controller.support.Pagination;
import pl.codecity.main.model.BlogLanguage;
import pl.codecity.main.model.Post;
import pl.codecity.main.request.PostSearchRequest;
import pl.codecity.main.service.PostService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/search")
public class SearchController {

	@Inject
	private PostService postService;

	@RequestMapping
	public String search(
			@RequestParam String keyword,
			@PageableDefault(50) Pageable pageable,
			BlogLanguage blogLanguage,
			Model model,
			HttpServletRequest servletRequest) {
		PostSearchRequest request = new PostSearchRequest(blogLanguage.getLanguage()).withKeyword(keyword);
		Page<Post> posts = postService.getPosts(request, pageable);
		model.addAttribute("keyword", keyword);
		model.addAttribute("posts", posts);
		model.addAttribute("pageable", pageable);
		model.addAttribute("pagination", new Pagination<>(posts, servletRequest));
		return "search";
	}
}
