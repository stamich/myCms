package pl.codecity.main.controller.admin.category;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.RequestContextUtils;
import pl.codecity.main.controller.support.DomainObjectDeletedModel;
import pl.codecity.main.controller.support.DomainObjectSavedModel;
import pl.codecity.main.controller.support.DomainObjectUpdatedModel;
import pl.codecity.main.controller.support.RestValidationErrorModel;
import pl.codecity.main.model.Category;
import pl.codecity.main.service.CategoryService;
import pl.codecity.main.utility.AuthorizedUser;
import pl.codecity.main.utility.CategoryUtils;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
public class CategoryRestController {

	@Inject
	private CategoryService categoryService;

	@Inject
	private CategoryUtils categoryUtils;

	@Inject
	private MessageSourceAccessor messageSourceAccessor;

	private static Logger logger = LoggerFactory.getLogger(CategoryRestController.class);

	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody RestValidationErrorModel bindException(BindException e) {
		logger.debug("BindException", e);
		return RestValidationErrorModel.fromBindingResult(e.getBindingResult(), messageSourceAccessor);
	}

	@RequestMapping(value="/{language}/categories", method= RequestMethod.GET)
	public @ResponseBody CategoryIndexModel index(@PathVariable String language) {
		return new CategoryIndexModel(categoryUtils.getNodes(true));
	}

	@RequestMapping(value="/{language}/categories", method=RequestMethod.POST)
	public @ResponseBody DomainObjectSavedModel save(
			@Valid CategoryCreateForm form,
			BindingResult result,
			AuthorizedUser authorizedUser,
			HttpServletRequest request,
			HttpServletResponse response) throws BindException {
		if (result.hasErrors()) {
			throw new BindException(result);
		}
		Category category = categoryService.createCategory(form.buildCategoryCreateRequest(), authorizedUser);
		FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
		flashMap.put("savedCategory", category);
		RequestContextUtils.getFlashMapManager(request).saveOutputFlashMap(flashMap, request, response);
		return new DomainObjectSavedModel<>(category);
	}

	@RequestMapping(value="/{language}/categories/{id}", method=RequestMethod.POST)
	public @ResponseBody DomainObjectUpdatedModel update(
			@Valid CategoryEditForm form,
			BindingResult result,
			@PathVariable long id,
			AuthorizedUser authorizedUser,
			HttpServletRequest request,
			HttpServletResponse response) throws BindException {
		form.setId(id);
		if (result.hasErrors()) {
			throw new BindException(result);
		}
		Category category = categoryService.updateCategory(form.buildCategoryUpdateRequest(), authorizedUser);
		FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
		flashMap.put("updatedCategory", category);
		RequestContextUtils.getFlashMapManager(request).saveOutputFlashMap(flashMap, request, response);
		return new DomainObjectUpdatedModel<>(category);
	}

	@RequestMapping(value="/{language}/categories/{id}", method= RequestMethod.DELETE)
	public @ResponseBody DomainObjectDeletedModel<Long> delete(
			@PathVariable String language,
			@PathVariable long id,
			AuthorizedUser authorizedUser,
			HttpServletRequest request,
			HttpServletResponse response) throws BindException {
		Category category = categoryService.deleteCategory(id, language);
		FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
		flashMap.put("deletedCategory", category);
		RequestContextUtils.getFlashMapManager(request).saveOutputFlashMap(flashMap, request, response);
		return new DomainObjectDeletedModel<>(category);
	}

	@RequestMapping(value="/{language}/categories", method= RequestMethod.PUT, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CategoryIndexModel sort(@PathVariable String language, @RequestBody List<Map<String, Object>> data) {
		categoryService.updateCategoryHierarchy(data, language);
		return new CategoryIndexModel(categoryUtils.getNodes(true));
	}
}
