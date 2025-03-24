package de.aittr.g_52_shop.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String upload(MultipartFile file, String productTitle);
}
