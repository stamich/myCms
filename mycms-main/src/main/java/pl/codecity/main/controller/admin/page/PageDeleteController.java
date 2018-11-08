package pl.codecity.main.controller.admin.page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.codecity.main.model.Page;
import pl.codecity.main.service.PageService;

import javax.inject.Inject;
import javax.validation.Valid;

@Controller
@RequestMapping(value="/{language}/pages/delete", method=RequestMethod.POST)
public class PageDeleteController {

	private static Logger logger = LoggerFactory.getLogger(PageDeleteController.class);

	@Inject
	private PageService pageService;

	@ModelAttribute("query")
	public String query(@RequestParam(required = false) String query) {
		return query;
	}

	@RequestMapping
	public String delete(
			@Valid @ModelAttribute("form") PageDeleteForm form,
			BindingResult errors,
			String query,
			RedirectAttributes redirectAttributes) {
//		if (!form.isConfirmed()) {
//			errors.rejectValue("confirmed", "Confirmed");
//		}
		if (errors.hasErrors()) {
			logger.debug("Errors: {}", errors);
			return "page/describe";
		}

		Page page = null;
		try {
			page = pageService.deletePage(form.buildPageDeleteRequest(), errors);
		}
		catch (BindException e) {
			if (errors.hasErrors()) {
				logger.debug("Errors: {}", errors);
				return "page/describe";
			}
			throw new RuntimeException(e);
		}

		redirectAttributes.addFlashAttribute("deletedPage", page);
		redirectAttributes.addAttribute("query", query);
		return "redirect:/_admin/{language}/pages/index";
	}
}
