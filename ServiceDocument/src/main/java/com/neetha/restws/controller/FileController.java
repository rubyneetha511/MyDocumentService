package com.neetha.restws.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.neetha.restws.exception.MyFileNotFoundException;
import com.neetha.restws.payload.FileUploadResponse;
import com.neetha.restws.service.FileService;
import com.neetha.restws.utils.ApplicationUtil;

/**
 * This is REST controller class to handle all the document storage operations.
 * 
 * @author neethaprabhu
 *
 */
@RestController
@RequestMapping("storage")
public class FileController {
	private static final Logger logger = LoggerFactory.getLogger(FileController.class);

	@Autowired
	private FileService fileService;

	/**
	 * This is the REST API to upload a file.
	 * 
	 * @param file - File to upload
	 * @return - Returns response
	 * @throws URISyntaxException - Exception
	 */
	@PostMapping("/documents")
	public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile file)
			throws URISyntaxException {

		String docId = ApplicationUtil.getRandomId();

		String fileName = fileService.storeFile(docId, file);

		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/storage/documents/").path(docId)
				.toUriString();

		return ResponseEntity.created(new URI(fileDownloadUri)).body(new FileUploadResponse(file.getOriginalFilename(),
				fileDownloadUri, file.getContentType(), file.getSize()));
	}

	/*
	 * 
	 */
	@GetMapping("/documents/{docId:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String docId, HttpServletRequest request) {
		// Load file as Resource
		Resource resource = fileService.loadFileAsResource(docId);

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			logger.info("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	@PutMapping("/documents/{docId:.+}")
	public ResponseEntity<Resource> updateFile(@PathVariable String docId, @RequestParam("file") MultipartFile file,
			HttpServletRequest request) {
		Resource resource = null;
		try {
			// Load file as Resource
			resource = fileService.loadFileAsResource(docId);
		} catch (MyFileNotFoundException e) {
			return ResponseEntity.noContent().build();
		}
		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			logger.info("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		boolean isFileDeleted = fileService.deleteFile(docId);

		String fileName = fileService.storeFile(docId, file);

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	@DeleteMapping("/documents/{docId:.+}")
	public ResponseEntity<Resource> deleteFile(@PathVariable String docId) {

		if (this.fileService.deleteFile(docId)) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.noContent().build();
		}

	}

}
