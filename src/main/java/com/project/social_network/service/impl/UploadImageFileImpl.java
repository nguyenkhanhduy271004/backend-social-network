package com.project.social_network.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.project.social_network.service.interfaces.UploadImageFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadImageFileImpl implements UploadImageFile {

  private final Cloudinary cloudinary;

  @Override
  public String uploadImage(MultipartFile file) throws IOException {
    assert file.getOriginalFilename() != null;
    String originalName = file.getOriginalFilename();
    String extension = getFileExtension(originalName);
    String publicValue = generatePublicValue(originalName);

    log.info("File Name: {}", originalName);
    log.info("File Extension: {}", extension);
    log.info("Public ID: {}", publicValue);

    boolean isVideo = isVideoFile(extension);
    String resourceType = isVideo ? "video" : "image";

    File fileUpload = convert(file);
    log.info("Converted File: {}", fileUpload.getAbsolutePath());

    cloudinary.uploader().upload(fileUpload, ObjectUtils.asMap(
        "public_id", publicValue,
        "resource_type", resourceType
    ));

    cleanDisk(fileUpload);

    return cloudinary.url().resourceType(resourceType).generate(publicValue + "." + extension);
  }

  private File convert(MultipartFile file) throws IOException {
    File convFile = new File(UUID.randomUUID() + "_" + file.getOriginalFilename());
    try (InputStream is = file.getInputStream()) {
      Files.copy(is, convFile.toPath());
    }
    return convFile;
  }

  private void cleanDisk(File file) {
    try {
      Files.deleteIfExists(file.toPath());
    } catch (IOException e) {
      log.error("Error deleting file: {}", file.getAbsolutePath(), e);
    }
  }

  private String generatePublicValue(String originalName) {
    return UUID.randomUUID().toString() + "_" + originalName;
  }

  private String getFileExtension(String filename) {
    int lastIndex = filename.lastIndexOf(".");
    return (lastIndex == -1) ? "" : filename.substring(lastIndex + 1).toLowerCase();
  }

  private boolean isVideoFile(String extension) {
    return extension.matches("mp4|avi|mov|wmv|flv|mkv");
  }
}
