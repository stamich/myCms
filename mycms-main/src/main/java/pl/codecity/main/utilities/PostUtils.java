package pl.codecity.main.utilities;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import pl.codecity.main.model.Article;
import pl.codecity.main.model.Page;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PostUtils {

	private PageUtils pageUtils;

	public PostUtils(PageUtils pageUtils) {
		this.pageUtils = pageUtils;
	}

	public String link(Article article) {
		UriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentContextPath();
		return path(builder, article, true);
	}

	public String link(Article article, boolean encode) {
		UriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentContextPath();
		return path(builder, article, encode);
	}

	public String link(Page page) {
		UriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentContextPath();
		return path(builder, page, true);
	}

	public String link(Page page, boolean encode) {
		UriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentContextPath();
		return path(builder, page, encode);
	}

	public String path(Article article) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("");
		return path(builder, article, true);
	}

	public String path(Article article, boolean encode) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("");
		return path(builder, article, encode);
	}

	public String path(Page page) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("");
		return path(builder, page, true);
	}

	public String path(Page page, boolean encode) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("");
		return path(builder, page, encode);
	}

	private String path(UriComponentsBuilder builder, Article article, boolean encode) {
		Map<String, Object> params = new HashMap<>();
		builder.path("/{year}/{month}/{day}/{code}");
		params.put("year", String.format("%04d", article.getDate().getYear()));
		params.put("month", String.format("%02d", article.getDate().getMonth().getValue()));
		params.put("day", String.format("%02d", article.getDate().getDayOfMonth()));
		params.put("code", article.getCode());

		UriComponents components = builder.buildAndExpand(params);
		if (encode) {
			components = components.encode();
		}
		return components.toUriString();
	}

	private String path(UriComponentsBuilder builder, Page page, boolean encode) {
		Map<String, Object> params = new HashMap<>();

		List<String> codes = new LinkedList<>();
		Map<Page, String> paths = pageUtils.getPaths(page);
		paths.keySet().stream().map(p -> p.getCode()).forEach(codes::add);

		for (int i = 0; i < codes.size(); i++) {
			String key = "code" + i;
			builder.path("/{" + key + "}");
			params.put(key, codes.get(i));
		}

		UriComponents components = builder.buildAndExpand(params);
		if (encode) {
			components = components.encode();
		}
		return components.toUriString();
	}
}
