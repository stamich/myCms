package pl.codecity.main.controller.admin.customfield;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/{language}/customfields/create")
public class CustomFieldCreateController {

	private static Logger logger = LoggerFactory.getLogger(CustomFieldCreateController.class);

	@Inject
	private CustomFieldService customfieldService;

	@Inject
	private MessageSourceAccessor messageSourceAccessor;

	@ModelAttribute("fieldTypes")
	public CustomField.FieldType[] setFieldTypes() {
		return CustomField.FieldType.values();
	}

	@ModelAttribute("form")
	public CustomFieldCreateForm customfieldCreateForm() {
		return new CustomFieldCreateForm();
	}

	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody RestValidationErrorModel bindException(BindException e) {
		logger.debug("BindException", e);
		return RestValidationErrorModel.fromBindingResult(e.getBindingResult(), messageSourceAccessor);
	}

	@RequestMapping(method=RequestMethod.GET)
	public String create() {
		return "customfield/create";
	}

	@RequestMapping(method=RequestMethod.POST)
	public String save(
			@PathVariable String language,
			@Validated @ModelAttribute("form") CustomFieldCreateForm form,
			BindingResult errors,
			String query,
			AuthorizedUser authorizedUser,
			RedirectAttributes redirectAttributes)
			throws BindException {
		if (errors.hasErrors()) {
			return "customfield/create";
		}

		CustomField customfield = null;
		try {
			customfield = customfieldService.createCustomField(form.buildCustomFieldCreateRequest(), authorizedUser);
		}
		catch (EmptyCodeException e) {
			errors.rejectValue("code", "NotNull");
		}
		catch (DuplicateCodeException e) {
			errors.rejectValue("code", "NotDuplicate");
		}
		if (errors.hasErrors()) {
			logger.debug("Errors: {}", errors);
			return "customfield/create";
		}
		redirectAttributes.addFlashAttribute("savedCustomField", customfield);
		redirectAttributes.addAttribute("language", language);
		redirectAttributes.addAttribute("id", customfield.getId());
		return "redirect:/_admin/{language}/customfields/index";
	}
}