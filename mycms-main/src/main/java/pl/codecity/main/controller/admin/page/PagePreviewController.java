package pl.codecity.main.controller.admin.page;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.expression.ThymeleafEvaluationContext;
import pl.codecity.main.controller.admin.article.CustomFieldValueEditForm;
import pl.codecity.main.controller.support.BlogLanguageMethodArgumentResolver;
import pl.codecity.main.controller.support.DefaultModelAttributeInterceptor;
import pl.codecity.main.exception.ServiceException;
import pl.codecity.main.model.*;
import pl.codecity.main.service.BlogService;
import pl.codecity.main.service.CustomFieldService;
import pl.codecity.main.service.MediaService;
import pl.codecity.main.utility.AuthorizedUser;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

@Controller
@RequestMapping("/{language}/pages/preview")
public class PagePreviewController {

	@Inject
	private BlogService blogService;

	@Inject
	private MediaService mediaService;

	@Inject
	private CustomFieldService customFieldService;

	@Inject
	private ServletContext servletContext;

	@RequestMapping
	public void preview(
			@PathVariable String language,
			@Valid @ModelAttribute("form") PagePreviewForm form,
			BindingResult result,
			AuthorizedUser authorizedUser,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Page page = new Page();
		page.setLanguage(language);
		page.setCover(form.getCoverId() != null ? mediaService.getMedia(form.getCoverId()) : null);
		page.setTitle(form.getTitle());
		page.setBody(form.getBody());
		page.setDate(form.getDate() != null ? form.getDate() : LocalDateTime.now());
		List<CustomFieldValue> fieldValues = new ArrayList<>();
		for (CustomFieldValueEditForm valueForm : form.getCustomFieldValues()) {
			CustomFieldValue value = new CustomFieldValue();
			value.setCustomField(customFieldService.getCustomFieldById(valueForm.getCustomFieldId(), language));
			if (valueForm.getFieldType().equals(CustomField.FieldType.CHECKBOX) && !ArrayUtils.isEmpty(valueForm.getTextValues())) {
				value.setTextValue(String.join(",", valueForm.getTextValues()));
			} else {
				value.setTextValue(valueForm.getTextValue());
			}
			value.setStringValue(valueForm.getStringValue());
			value.setNumberValue(valueForm.getNumberValue());
			value.setDateValue(valueForm.getDateValue());
			value.setDatetimeValue(valueForm.getDatetimeValue());
			fieldValues.add(value);
		}
		page.setCustomFieldValues(new TreeSet<>(fieldValues));
		page.setAuthor(authorizedUser);

		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext, "org.springframework.web.servlet.FrameworkServlet.CONTEXT.guestServlet");
		if (context == null) {
			throw new ServiceException("GuestServlet is not ready yet");
		}

		Blog blog = blogService.getBlogById(Blog.DEFAULT_ID);
		BlogLanguage blogLanguage = blog.getLanguage(language);
		request.setAttribute(BlogLanguageMethodArgumentResolver.BLOG_LANGUAGE_ATTRIBUTE, blogLanguage);

		DefaultModelAttributeInterceptor interceptor = context.getBean(DefaultModelAttributeInterceptor.class);
		ModelAndView mv = new ModelAndView("dummy");
		interceptor.postHandle(request, response, this, mv);

		final WebContext ctx = new WebContext(
				request,
				response,
				servletContext,
				LocaleContextHolder.getLocale(),
				mv.getModelMap());
		ctx.setVariable("page", page);

		ThymeleafEvaluationContext evaluationContext = new ThymeleafEvaluationContext(context, null);
		ctx.setVariable(ThymeleafEvaluationContext.THYMELEAF_EVALUATION_CONTEXT_CONTEXT_VARIABLE_NAME, evaluationContext);

		SpringTemplateEngine templateEngine = context.getBean("templateEngine", SpringTemplateEngine.class);
		String html = templateEngine.process("page/describe", ctx);

		response.setContentType("text/html;charset=UTF-8");
		response.setContentLength(html.getBytes("UTF-8").length);
		response.getWriter().write(html);
	}
}
