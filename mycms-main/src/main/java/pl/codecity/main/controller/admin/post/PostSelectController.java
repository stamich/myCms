package pl.codecity.main.controller.admin.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.codecity.main.controller.support.DomainObjectSelect2Model;
import pl.codecity.main.model.Post;
import pl.codecity.main.request.PostSearchRequest;
import pl.codecity.main.service.PostService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PostSelectController {

	@Inject
	private PostService postService;

	@RequestMapping(value="/{language}/posts/select")
	public @ResponseBody List<DomainObjectSelect2Model> select(
			@PathVariable String language,
			@RequestParam(required=false) String keyword) {
		PostSearchRequest request = new PostSearchRequest(language)
				.withStatus(Post.Status.PUBLISHED)
				.withKeyword(keyword);
		Page<Post> posts = postService.getPosts(request, new PageRequest(0, 30));

		List<DomainObjectSelect2Model> results = new ArrayList<>();
		if (posts.hasContent()) {
			for (Post post : posts) {
				DomainObjectSelect2Model model = new DomainObjectSelect2Model(post);
				results.add(model);
			}
		}
		return results;
	}

	@RequestMapping(value="/{language}/posts/select/{id}")
	public @ResponseBody
	DomainObjectSelect2Model select(
			@PathVariable String language,
			@RequestParam Long id,
			HttpServletResponse response)
			throws IOException {
		Post post = postService.getPostById(id, language);
		if (post == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		DomainObjectSelect2Model model = new DomainObjectSelect2Model(post);
		return model;
	}
}
