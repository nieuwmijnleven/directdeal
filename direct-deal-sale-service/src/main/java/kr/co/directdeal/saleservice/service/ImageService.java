package kr.co.directdeal.saleservice.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import kr.co.directdeal.saleservice.async.AsyncImageSaveRunner;
import kr.co.directdeal.saleservice.dto.ItemImageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageService {
    
    public static final String IMAGE_REPOSITORY_PATH = "resources/images";

    private final AsyncImageSaveRunner asyncSaveImageRunner;

    public byte[] readImage(String filename) {
        try {
            return Files.readAllBytes(Paths.get(IMAGE_REPOSITORY_PATH, filename));
        } catch(Exception e) {
            throw new RuntimeException("failed to read the image(" + filename + ").");
        }
    }

    public ItemImageDTO saveImages(List<MultipartFile> files) {
        if (files.size() == 0)
            throw new IllegalStateException("Threre is no item images to save.");

        log.debug("files.size() => " + files.size());
        log.debug("files[0].name => " + files.get(0).getOriginalFilename());

        //normalize image names
        List<String> images = new ArrayList<>();
        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            String ext = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
            String filename = UUID.randomUUID().toString() + "." + ext;
            images.add(filename);

            // Path dest = Paths.get(ImageService.IMAGE_REPOSITORY_PATH, filename).toAbsolutePath();
            // try {
            //     file.transferTo(dest);
            //     // Files.copy(file.getInputStream(), dest);
            // } catch(Exception e) {
            //     throw new RuntimeException(e);
            // }
        }

        String checkId = UUID.randomUUID().toString();
        String checkURL = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/check-upload-state")
                            .path("/{id}")
                            .buildAndExpand(checkId)
                            .toUriString();

        log.debug("checkId => " + checkId);
        log.debug("checkURL => " + checkURL);

        new Thread(new Runnable(){
            public void run() {
                asyncSaveImageRunner.execute(files, images, checkId);
            }
        }).start();

        return ItemImageDTO.builder()
                    .checkId(checkId)
                    .checkURL(checkURL)
                    .images(images)
                    .build();
    }
}
