package pl.codecity.main.utility;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public abstract class ExtendedResourceUtils extends ResourceUtils {

//	public static Resource createRelative(Resource resource, String relativePath, ResourceLoader resourceLoader) throws IOException {
//		return resourceLoader.getResource(resource.getURI().toString() + relativePath);
//	}

	public static void write(Resource resource, File file) throws IOException {
		if (resource instanceof WritableResource) {
//			IOUtils.copy(new FileInputStream(file), ((WritableResource) resource).getOutputStream());
			try (InputStream input = new FileInputStream(file); OutputStream output = ((WritableResource) resource).getOutputStream()) {
				IOUtils.copy(input, output);
			}
			return;
		}
		FileUtils.copyFile(file, getFile(resource.getURI()));
	}

	public static void write(Resource resource, MultipartFile file) throws IOException {
		if (resource instanceof WritableResource) {
//			IOUtils.copy(file.getInputStream(), ((WritableResource) resource).getOutputStream());
			try (InputStream input = file.getInputStream(); OutputStream output = ((WritableResource) resource).getOutputStream()) {
				IOUtils.copy(input, output);
			}
			return;
		}
		FileUtils.copyInputStreamToFile(file.getInputStream(), getFile(resource.getURI()));
	}
}
