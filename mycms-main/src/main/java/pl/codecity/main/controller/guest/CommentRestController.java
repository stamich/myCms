package pl.codecity.main.controller.guest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.codecity.main.controller.support.DomainObjectDeletedModel;
import pl.codecity.main.controller.support.RestValidationErrorModel;
import pl.codecity.main.model.BlogLanguage;
import pl.codecity.main.model.Comment;
import pl.codecity.main.request.CommentCreateRequest;
import pl.codecity.main.request.CommentDeleteRequest;
import pl.codecity.main.request.CommentUpdateRequest;
import pl.codecity.main.service.CommentService;
import pl.codecity.main.utility.AuthorizedUser;

import javax.inject.Inject;

@RestController
@RequestMapping("/comments")
public class CommentRestController {

	@Inject
	private CommentService commentService;
	@Inject
	private MessageSourceAccessor messageSourceAccessor;

	private static Logger logger = LoggerFactory.getLogger(CommentRestController.class);

	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public RestValidationErrorModel bindException(BindException e) {
		logger.debug("BindException", e);
		return RestValidationErrorModel.fromBindingResult(e.getBindingResult(), messageSourceAccessor);
	}

	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public CommentSavedModel create(
			@Validated CommentForm form,
			BindingResult result,
			BlogLanguage blogLanguage,
			AuthorizedUser authorizedUser) throws BindException {
		if (result.hasErrors()) {
			throw new BindException(result);
		}

		CommentCreateRequest request = form.toCommentCreateRequest(blogLanguage, authorizedUser);
		Comment comment = commentService.createComment(request, authorizedUser);
		return new CommentSavedModel(comment);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public CommentSavedModel update(
			@PathVariable long id,
			@Validated CommentForm form,
			BindingResult result,
			AuthorizedUser authorizedUser) throws BindException {
		if (result.hasFieldErrors("content")) {
			throw new BindException(result);
		}
		CommentUpdateRequest request = form.toCommentUpdateRequest(id);
		Comment comment = commentService.updateComment(request, authorizedUser);
		return new CommentSavedModel(comment);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public DomainObjectDeletedModel<Long> delete(
			@PathVariable long id,
			AuthorizedUser authorizedUser) {
		CommentDeleteRequest request = new CommentDeleteRequest();
		request.setId(id);
		Comment comment = commentService.deleteComment(request, authorizedUser);
		return new DomainObjectDeletedModel<>(comment);
	}
}
