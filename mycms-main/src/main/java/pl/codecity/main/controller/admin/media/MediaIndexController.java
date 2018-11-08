package pl.codecity.main.controller.admin.media;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.codecity.main.configuration.MyCmsProperties;
import pl.codecity.main.model.Media;
import pl.codecity.main.service.MediaService;

import javax.inject.Inject;
import java.util.List;

@Controller
@RequestMapping("/{language}/media/index")
public class MediaIndexController {

	@Inject
	private MediaService mediaService;
	@Inject
	private MyCmsProperties myCmsProperties;

	@RequestMapping
	public @ResponseBody MediaIndexModel[] index() {
		List<Media> medias = mediaService.getAllMedias();
		MediaIndexModel[] models = new MediaIndexModel[medias.size()];
		for (int i = 0; i < medias.size(); i++) {
			models[i] = new MediaIndexModel(medias.get(i), myCmsProperties);
		}
		return models;
	}
}
