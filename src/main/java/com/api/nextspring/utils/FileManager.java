package com.api.nextspring.utils;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.nextspring.exceptions.RestApiException;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * This class provides utility methods for handling files related to entities.
 * It has a private method getFilesPath that returns the path of the directory
 * where the files will be saved.
 * 
 * @author Athirson Silva
 */
@Service
@RequiredArgsConstructor
public class FileManager {

	/**
	 * Returns the path of the directory where the files will be saved
	 * 
	 * @return the path of the directory where the files will be saved
	 */
	private String getFilesPath() {
		String workingDirectory = System.getProperty("user.dir");
		String directory = workingDirectory + "/src/main/resources/static/";

		return directory;
	}

	/**
	 * Saves a file to the server and returns the path of the file that was saved
	 * 
	 * @param id   the id of the user that will be used to generate the file name
	 * @param file the file that will be saved to the server
	 * @return the path of the file that was saved
	 */
	public String savePhoto(String id, MultipartFile file) {
		if (file.isEmpty())
			throw new RestApiException(HttpStatus.BAD_REQUEST, "File is empty!");

		String directory = getFilesPath() + "images";
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

	/**
	 * Find the file with the given path and downloads it to the user
	 * 
	 * @param filePath the path of the file that will be downloaded
	 * @param response the response object that will be used to download the file to
	 *                 the user
	 */
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
