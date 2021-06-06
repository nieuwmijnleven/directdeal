package kr.co.directdeal.saleservice.service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.saleservice.async.AsyncImageSaveRunner;
import kr.co.directdeal.saleservice.domain.ImageUploadStatus;
import kr.co.directdeal.saleservice.dto.ImageUploadStatusDTO;
import kr.co.directdeal.saleservice.dto.ItemImageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageService {
    
    public static final String IMAGE_REPOSITORY_PATH = "resources/images";

    private final AsyncImageSaveRunner asyncSaveImageRunner;

    private final ImageUploadStatusRepository imageUploadStatusRepository;

    private final Mapper<ImageUploadStatus, ImageUploadStatusDTO> mapper;

    public byte[] readImage(String filename) {
        try {
            return Files.readAllBytes(Paths.get(IMAGE_REPOSITORY_PATH, filename));
        } catch(Exception e) {
            throw new RuntimeException("failed to read the image(" + filename + ").");
        }
    }

    @Transactional(readOnly = true)
    public ImageUploadStatusDTO checkUploadStatus(String id) {
        log.debug("id => " + id);
        ImageUploadStatus imageUploadStatus = imageUploadStatusRepository.findById(id)
                                                    .orElseThrow(IllegalStateException::new);
        return mapper.toDTO(imageUploadStatus);
    }

    public ItemImageDTO saveImages(List<MultipartFile> files) {
        if (files.size() == 0)
            throw new IllegalStateException("Threre is no item images to save.");

        //allocate normalized image names
        List<String> images = new ArrayList<>();
        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            String ext = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
            String filename = UUID.randomUUID().toString() + "." + ext;
            images.add(filename);
        }

        String checkId = UUID.randomUUID().toString();
        String checkURL = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/check-upload-status")
                            .path("/{id}")
                            .buildAndExpand(checkId)
                            .toUriString();

        asyncSaveImageRunner.execute(files, images, checkId);

        return ItemImageDTO.builder()
                    .checkId(checkId)
                    .checkURL(checkURL)
                    .images(images)
                    .build();
    }
}