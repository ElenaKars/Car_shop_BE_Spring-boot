package de.aittr.g_52_shop.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import de.aittr.g_52_shop.service.interfaces.FileService;
import de.aittr.g_52_shop.service.interfaces.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private final AmazonS3 client;
    private final ProductService productService;

    public FileServiceImpl(AmazonS3 client, ProductService productService) {
        this.client = client;
        this.productService = productService;
    }

    @Override
    public String upload(MultipartFile file, String productTitle) {
        try {
            String uniqueName = generateUniqueFileName(file);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());

            PutObjectRequest request = new PutObjectRequest(
                    "shop-bucket", uniqueName, file.getInputStream(), metadata
            ).withCannedAcl(CannedAccessControlList.PublicRead);

            //send the request
            client.putObject(request);

            String url = client.getUrl("shop-bucket", uniqueName).toString();

            productService.attachImage(url, productTitle);

            return url;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String generateUniqueFileName(MultipartFile file){
        String sourceFileName = file.getOriginalFilename();
        int dotIndex = sourceFileName.lastIndexOf(".");
        String fileName = sourceFileName.substring(0,dotIndex);
        String extension = sourceFileName.substring(dotIndex);

        return String.format("%s - %s%s", fileName, UUID.randomUUID(), extension);
    }
}
