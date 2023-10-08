package com.api.nextspring.utils;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.nextspring.exceptions.RestApiException;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EntityFileUtils {

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

	public void getPhoto(String filePath, HttpServletResponse response) {
		Path path = Paths.get(filePath);

		if (!Files.exists(path))
			throw new RestApiException(HttpStatus.NOT_FOUND, "File not found!");

		File file = new File(filePath);

		try (InputStream inputStream = file.toURI().toURL().openStream()) {
			Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while downloading the file!");
		}
	}
}
