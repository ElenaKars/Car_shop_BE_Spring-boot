package de.aittr.g_52_shop.controller;

import de.aittr.g_52_shop.exception_handling.Response;
import de.aittr.g_52_shop.service.interfaces.FileService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public Response upload(
            @RequestParam MultipartFile file,
            @RequestParam String productTitle
            ){
       String url = fileService.upload(file, productTitle);
       return new Response("Saved image-url: " + url);
    }
}
