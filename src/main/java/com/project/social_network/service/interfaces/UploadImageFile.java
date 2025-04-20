package com.project.social_network.service.interfaces;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface UploadImageFile {
  String uploadImage(MultipartFile file) throws IOException;
  void validateImage(MultipartFile file);
}
