package pl.codecity.main.controller.admin.media;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import pl.codecity.main.configuration.MyCmsProperties;
import pl.codecity.main.model.Media;
import pl.codecity.main.service.MediaService;

import javax.inject.Inject;

@Controller
@RequestMapping("/{language}/media/create")
public class MediaCreateController {

	@Inject
	private MediaService mediaService;
	@Inject
	private MyCmsProperties myCmsProperties;

	@RequestMapping(method=RequestMethod.POST)
	public @ResponseBody MediaCreatedModel create(@RequestParam MultipartFile file) {
		Media media = mediaService.createMedia(file);
		return new MediaCreatedModel(media, myCmsProperties);
	}
}
