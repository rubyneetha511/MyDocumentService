package com.neetha.restws.service;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.neetha.restws.exception.FileException;
import com.neetha.restws.exception.MyFileNotFoundException;
import com.neetha.restws.property.FileProperties;

@Service
public class FileService {
	private final Path fileLocation;

	@Autowired
	public FileService(FileProperties fileStorageProperties) {
		this.fileLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileLocation);
		} catch (Exception ex) {
			throw new FileException("Could not create the directory where the uploaded files will be stored.", ex);
		}
	}

	public String storeFile(String prefixId, MultipartFile file) {
		// Normalize file name
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try {
			// Check if the file's name contains invalid characters
			if (fileName.contains("..")) {
				throw new FileException("Sorry! Filename contains invalid path sequence " + fileName);
			}

			fileName = prefixId + "_" + fileName;
			// Copy file to the target location (Replacing existing file with the same name)
			Path targetLocation = this.fileLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			return fileName;
		} catch (IOException ex) {
			throw new FileException("Could not store file " + fileName + ". Please try again!", ex);
		}
	}

	public boolean deleteFile(String prefixId) {

		File[] files = findFiles(prefixId);
		boolean flag = false;
		
		if (files != null) {

			for (File file : files) {
				try {
					Files.deleteIfExists(file.toPath());
					flag = true;
				} catch (IOException ex) {
					flag = false;
				}
			}

		} 
		return flag;

	}

	public Resource loadFileAsResource(String prefixId) {
		try {

			File[] files = findFiles(prefixId);

			if (files == null || files.length == 0) {
				throw new MyFileNotFoundException("File not found with ID " + prefixId);
			}
			Resource resource = new UrlResource(files[0].toURI());
			if (resource.exists()) {
				return resource;
			} else {
				throw new MyFileNotFoundException("File not found with ID " + prefixId);
			}
		} catch (MalformedURLException ex) {
			throw new MyFileNotFoundException("File not found with ID" + prefixId, ex);
		}

	}

	private File[] findFiles(String prefixId) {

		File dir = this.fileLocation.toFile();

		File[] matches = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.startsWith(prefixId);
			}
		});

		return matches;

	}
}
