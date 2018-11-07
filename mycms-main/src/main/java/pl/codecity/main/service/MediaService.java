package pl.codecity.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.codecity.main.configuration.MyCmsCacheConfiguration;
import pl.codecity.main.configuration.MyCmsProperties;
import pl.codecity.main.model.Media;
import pl.codecity.main.repository.MediaRepository;
import pl.codecity.main.utility.ExtendedResourceUtils;

import java.io.IOException;
import java.util.List;

@Service
@Transactional(rollbackFor=Exception.class)
public class MediaService {

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private MyCmsProperties wallRideProperties;

	@Autowired
	private MediaRepository mediaRepository;

	public Media createMedia(MultipartFile file) {
		Media media = new Media();
		media.setMimeType(file.getContentType());
		media.setOriginalName(file.getOriginalFilename());
		media = mediaRepository.saveAndFlush(media);

//		Blog blog = blogService.getBlogById(Blog.DEFAULT_ID);
		try {
			Resource prefix = resourceLoader.getResource(wallRideProperties.getMediaLocation());
			Resource resource = prefix.createRelative(media.getId());
//			AmazonS3ResourceUtils.writeMultipartFile(file, resource);
			ExtendedResourceUtils.write(resource, file);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}

		return media;
	}

	public List<Media> getAllMedias() {
		return mediaRepository.findAll(new Sort(new Sort.Order(Sort.Direction.DESC, "createdAt")));
	}

	@Cacheable(value = MyCmsCacheConfiguration.MEDIA_CACHE)
	public Media getMedia(String id) {
		return mediaRepository.findOneById(id);
	}
}
