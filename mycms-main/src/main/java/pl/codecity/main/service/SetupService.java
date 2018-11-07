package pl.codecity.main.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.codecity.main.configuration.MyCmsCacheConfiguration;
import pl.codecity.main.model.Blog;
import pl.codecity.main.model.BlogLanguage;
import pl.codecity.main.model.User;
import pl.codecity.main.repository.BlogRepository;
import pl.codecity.main.repository.UserRepository;
import pl.codecity.main.request.SetupRequest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional(rollbackFor = Exception.class)
public class SetupService {

	@Resource
	private UserRepository userRepository;

	@Resource
	private BlogRepository blogRepository;

	@CacheEvict(value = {MyCmsCacheConfiguration.USER_CACHE, MyCmsCacheConfiguration.BLOG_CACHE}, allEntries = true)
	public User setup(SetupRequest request) {
		LocalDateTime now = LocalDateTime.now();

		User user = new User();
		user.setLoginId(request.getLoginId());

		StandardPasswordEncoder passwordEncoder = new StandardPasswordEncoder();
		user.setLoginPassword(passwordEncoder.encode(request.getLoginPassword()));

		user.getName().setFirstName(request.getName().getFirstName());
		user.getName().setLastName(request.getName().getLastName());
		user.setEmail(request.getEmail());

		user.getRoles().add(User.Role.ADMIN);

		user.setCreatedAt(now);
		user.setUpdatedAt(now);

		user = userRepository.saveAndFlush(user);

		Blog blog = new Blog();
		blog.setCode("default");
		blog.setDefaultLanguage(request.getDefaultLanguage());
		blog.setCreatedAt(now);
		blog.setCreatedBy(user.toString());
		blog.setUpdatedAt(now);
		blog.setUpdatedBy(user.toString());

		BlogLanguage defaultLanguage = new BlogLanguage();
		defaultLanguage.setBlog(blog);
		defaultLanguage.setLanguage(request.getDefaultLanguage());
		defaultLanguage.setTitle(request.getWebsiteTitle());
		defaultLanguage.setCreatedAt(now);
		defaultLanguage.setCreatedBy(user.toString());
		defaultLanguage.setUpdatedAt(now);
		defaultLanguage.setUpdatedBy(user.toString());

		Set<BlogLanguage> blogLanguages = new HashSet<>();
		blogLanguages.add(defaultLanguage);

		for (String language : request.getLanguages()) {
			BlogLanguage blogLanguage = new BlogLanguage();
			blogLanguage.setBlog(blog);
			blogLanguage.setLanguage(language);
			blogLanguage.setTitle(request.getWebsiteTitle());
			blogLanguage.setCreatedAt(now);
			blogLanguage.setCreatedBy(user.toString());
			blogLanguage.setUpdatedAt(now);
			blogLanguage.setUpdatedBy(user.toString());

			blogLanguages.add(blogLanguage);
		}
		blog.setLanguages(blogLanguages);

		blogRepository.saveAndFlush(blog);

		return user;
	}
}
