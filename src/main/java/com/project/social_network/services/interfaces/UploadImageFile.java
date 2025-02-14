package com.project.social_network.services.interfaces;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface UploadImageFile {
  String uploadImage(MultipartFile file) throws IOException;
}
