package kr.co.directdeal.saleservice.service;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import kr.co.directdeal.saleservice.service.dto.ItemDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class Base64ImageService {
    
    public static final String IMAGE_REPOSITORY_PATH = "resources/images";

    public byte[] readImage(String filename) {
        try {
            return Files.readAllBytes(Paths.get(IMAGE_REPOSITORY_PATH, filename));
        } catch(Exception e) {
            throw new RuntimeException("failed to read the image(" + filename + ").");
        }
    }

    public void saveImage(ItemDTO itemDTO) {
        if (itemDTO.getImages().size() == 0)
            throw new IllegalStateException("Threre is no item images to save.");

        List<String> imagePaths = saveEncodedImages(itemDTO.getImages());
        itemDTO.setImages(imagePaths);
    }

    private List<String> saveEncodedImages(List<String> encodedImages) {
        List<String> savedImagePaths = new ArrayList<>();

        for (String encodedImage : encodedImages) {
            int base64PartStartIndex = encodedImage.indexOf(";base64,");
            String imageType = encodedImage.substring(11, base64PartStartIndex);
            String base64Image = encodedImage.substring(8 + base64PartStartIndex);
            
            String filename = UUID.randomUUID().toString() + "." + imageType;
            String imagePath = IMAGE_REPOSITORY_PATH + File.separator + filename;
            log.debug("imagePath => " + imagePath);
            saveBase64Image(base64Image, imagePath);

            savedImagePaths.add(filename);
        }

        return savedImagePaths;
    }

    private void saveBase64Image(String base64Image, String imagePath) {
        try {
            byte[] imageBinary = Base64.getDecoder().decode(base64Image);
            BufferedInputStream rasterInputStream = new BufferedInputStream(new ByteArrayInputStream(imageBinary));
            Files.copy(rasterInputStream, Paths.get(imagePath));
        } catch(Exception e) {
            throw new RuntimeException("could not save a base64 image to " + imagePath);
        }
    }

}
