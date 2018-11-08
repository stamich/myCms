package pl.codecity.main.controller.admin.customfield;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.codecity.main.controller.support.RestValidationErrorModel;
import pl.codecity.main.exception.DuplicateCodeException;
import pl.codecity.main.exception.EmptyCodeException;
import pl.codecity.main.model.CustomField;
import pl.codecity.main.service.CustomFieldService;
import pl.codecity.main.utility.AuthorizedUser;

import javax.inject.Inject;

@Controller
@RequestMapping("/{language}/customfields/edit")
public class CustomFieldEditController {

	private static Logger logger = LoggerFactory.getLogger(CustomFieldEditController.class);

	@Inject
	private CustomFieldService customFieldService;

	@Inject
	private MessageSourceAccessor messageSourceAccessor;

	@ModelAttribute("fieldTypes")
	public CustomField.FieldType[] setFieldTypes() {
		return CustomField.FieldType.values();
	}

	@ModelAttribute("customField")
	public CustomField setupCustomField(
			@PathVariable String language,
			@RequestParam long id) {
		return customFieldService.getCustomFieldById(id, language);
	}

	@ModelAttribute("query")
	public String query(@RequestParam(required = false) String query) {
		return query;
	}

	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody RestValidationErrorModel bindException(BindException e) {
		logger.debug("BindException", e);
		return RestValidationErrorModel.fromBindingResult(e.getBindingResult(), messageSourceAccessor);
	}

	@RequestMapping(method= RequestMethod.GET)
	public String edit(
			@PathVariable String language,
			@RequestParam long id,
			Model model,
			RedirectAttributes redirectAttributes) {
		CustomField customField = (CustomField) model.asMap().get("customField");
		if (!language.equals(customField.getLanguage())) {
			redirectAttributes.addAttribute("language", language);
			return "redirect:/_admin/{language}/customfields/index";
		}

		CustomFieldEditForm form = CustomFieldEditForm.fromDomainObject(customField);
		model.addAttribute("form", form);

		return "customfield/edit";
	}


	@RequestMapping(method=RequestMethod.POST)
	public String update(
			@PathVariable String language,
			@Validated @ModelAttribute("form") CustomFieldEditForm form,
			BindingResult errors,
			String query,
			AuthorizedUser authorizedUser,
			RedirectAttributes redirectAttributes) {
		if (errors.hasErrors()) {
			return "customField/edit";
		}

		CustomField customField = null;
		try {
			customField = customFieldService.updateCustomField(form.buildCustomFieldUpdateRequest(), authorizedUser);
		}
		catch (EmptyCodeException e) {
			errors.rejectValue("code", "NotNull");
		}
		catch (DuplicateCodeException e) {
			errors.rejectValue("code", "NotDuplicate");
		}
		if (errors.hasErrors()) {
			logger.debug("Errors: {}", errors);
			return "customfield/edit";
		}

		redirectAttributes.addFlashAttribute("savedCustomField", customField);
		redirectAttributes.addAttribute("language", language);
		redirectAttributes.addAttribute("id", customField.getId());
		return "redirect:/_admin/{language}/customfields/index";
	}
}