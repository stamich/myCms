package pl.codecity.main.controller.admin.comment;

import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import pl.codecity.main.controller.support.ControllerUtils;
import pl.codecity.main.model.Comment;
import pl.codecity.main.service.CommentService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

@Controller
@RequestMapping("/{language}/comments/index")
public class CommentSearchController {

    @Inject
    private CommentService commentService;

    @Inject
    private ConversionService conversionService;

    @ModelAttribute("form")
    public CommentSearchForm setupCommentSearchForm() {
        return new CommentSearchForm();
    }

    @ModelAttribute("query")
    public String query(@RequestParam(required = false) String query) {
        return query;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String search(
            @PathVariable String language,
            @Validated CommentSearchForm form,
            BindingResult result,
            @PageableDefault(value = 50, sort = "date", direction = Sort.Direction.DESC) Pageable pageable,
            Model model,
            HttpServletRequest servletRequest) throws UnsupportedEncodingException {
        Page<Comment> comments = commentService.getComments(form.toCommentSearchRequest(), pageable);

        model.addAttribute("form", form);
        model.addAttribute("comments", comments);
        model.addAttribute("pageable", pageable);
        model.addAttribute("pagination", new pl.codecity.main.controller.support.Pagination<>(comments, servletRequest));

        UriComponents uriComponents = ServletUriComponentsBuilder
                .fromRequest(servletRequest)
                .queryParams(ControllerUtils.convertBeanForQueryParams(form, conversionService))
                .build();
        if (!StringUtils.isEmpty(uriComponents.getQuery())) {
            model.addAttribute("query", URLDecoder.decode(uriComponents.getQuery(), "UTF-8"));
        }

        return "comment/index";
    }

    @RequestMapping(params = "query")
    public String query(
            @PathVariable String language,
            String query,
            Model model,
            SessionStatus sessionStatus,
            RedirectAttributes redirectAttributes) {
        sessionStatus.setComplete();

        for (Map.Entry<String, Object> mapEntry : model.asMap().entrySet()) {
            redirectAttributes.addFlashAttribute(mapEntry.getKey(), mapEntry.getValue());
        }
        String url = UriComponentsBuilder.fromPath("/_admin/{language}/comments/index")
                .query(query)
                .buildAndExpand(language)
                .encode()
                .toUriString();
        return "redirect:" + url;
    }

    @RequestMapping(method = RequestMethod.GET, params = "part=bulk-delete-form")
    public String partBulkDeleteForm(@PathVariable String language) {
        return "comment/index::bulk-delete-form";
    }

    @RequestMapping(method = RequestMethod.GET, params = "part=bulk-approve-form")
    public String partBulkApproveForm(@PathVariable String language) {
        return "comment/index::bulk-approve-form";
    }

    @RequestMapping(method = RequestMethod.GET, params = "part=bulk-unapprove-form")
    public String partBulkUnapproveForm(@PathVariable String language) {
        return "comment/index::bulk-unapprove-form";
    }
}
