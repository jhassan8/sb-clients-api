package com.clients.models.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileServiceImpl implements IUploadFileService {

	private final Logger logger = LoggerFactory.getLogger(UploadFileServiceImpl.class);
	private final static String UPLOADS = "uploads";

	@Override
	public Resource load(String name) throws MalformedURLException {

		Path uploadsPath = get(name);
		logger.info(uploadsPath.toString());
		Resource resouce = new UrlResource(uploadsPath.toUri());

		if (!resouce.exists()) {
			uploadsPath = Paths.get("src/main/resources/static/imgs/no-user.png").resolve("no-user.png")
					.toAbsolutePath();
			resouce = new UrlResource(uploadsPath.toUri());
			logger.error("error on load image " + name);
		}

		return resouce;
	}

	@Override
	public String save(MultipartFile file) throws IOException {

		String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename().replace(" ", "");
		Path uploadsPath = get(fileName);

		logger.info(uploadsPath.toString());
		Files.copy(file.getInputStream(), uploadsPath);

		return fileName;
	}

	@Override
	public boolean delete(String name) {
		if (name != null && !name.isEmpty()) {
			Path oldFilePath = Paths.get("uploads").resolve(name).toAbsolutePath();
			File oldFile = oldFilePath.toFile();
			if (oldFile.exists() && oldFile.canRead()) {
				oldFile.delete();
				return true;
			}
		}
		return false;
	}

	@Override
	public Path get(String name) {
		return Paths.get(UPLOADS).resolve(name).toAbsolutePath();
	}

}
