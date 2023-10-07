package com.api.nextspring.utils;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.UUID;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.nextspring.exceptions.RestApiException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileUtils {

	private String getFilesPath() {
		String workingDirectory = System.getProperty("user.dir");
		String directory = workingDirectory + "/src/main/resources/static/";

		return directory;
	}

	public String savePhoto(UUID id, MultipartFile file) {
		if (file.isEmpty())
			throw new RestApiException(HttpStatus.BAD_REQUEST, "File is empty!");

		String directory = getFilesPath() + "images/games";
		long seconds = Instant.now().getEpochSecond();
		String photoName = id.toString() + String.valueOf(seconds) + ".png";
		Path path = Paths.get(directory + File.separator + photoName);

		try (InputStream inputStream = file.getInputStream()) {
			Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while uploading the file!");
		}

		return path.toString();
	}

	public InputStreamResource getPhoto(String filePath) {
		InputStream inputStream = getClass().getResourceAsStream(filePath);

		return inputStream != null
				? new InputStreamResource(inputStream)
				: new InputStreamResource(getClass().getResourceAsStream("https://api.dicebear.com/avatar.svg"));
	}
}
