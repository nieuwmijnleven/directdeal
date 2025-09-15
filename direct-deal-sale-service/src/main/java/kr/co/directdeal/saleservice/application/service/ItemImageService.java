package kr.co.directdeal.saleservice.application.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

import kr.co.directdeal.saleservice.port.inbound.ItemImageUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.saleservice.async.AsyncImageSaveRunner;
import kr.co.directdeal.saleservice.domain.object.ImageUploadStatus;
import kr.co.directdeal.saleservice.exception.ItemImageException;
import kr.co.directdeal.saleservice.application.service.dto.ImageUploadStatusDTO;
import kr.co.directdeal.saleservice.application.service.dto.ItemImageDTO;
import kr.co.directdeal.saleservice.port.outbound.ImageUploadStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class that implements the ItemImageUseCase interface.
 * Responsible for managing item image files including reading, saving, and checking upload status.
 *
 * @author Cheol Jeon
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ItemImageService implements ItemImageUseCase {

    /**
     * Base directory path for storing image files.
     */
    public static final String IMAGE_REPOSITORY_PATH = "resources/images";

    /**
     * Path object for the image repository directory.
     */
    public static final Path IMAGE_REPOSITORY = Paths.get(IMAGE_REPOSITORY_PATH);

    private final AsyncImageSaveRunner asyncSaveImageRunner;

    private final ImageUploadStatusRepository imageUploadStatusRepository;

    private final Mapper<ImageUploadStatus, ImageUploadStatusDTO> mapper;

    /**
     * Reads the image file bytes by given filename.
     *
     * @param filename the name of the image file to read
     * @return byte array of the image file contents
     * @throws ItemImageException if the image file cannot be read
     */
    @Override
    public byte[] readImage(String filename) {
        try {
            return Files.readAllBytes(Paths.get(IMAGE_REPOSITORY_PATH, filename));
        } catch(Exception e) {
            throw ItemImageException.builder()
                    .messageKey("saleservice.exception.itemimageservice.readimage.fail.message")
                    .messageArgs(new String[]{ filename })
                    .build();
        }
    }

    /**
     * Saves multiple image files asynchronously and returns an ItemImageDTO with upload status information.
     * Generates unique filenames for each uploaded image.
     * Creates the image repository directory if it does not exist.
     *
     * @param files the list of MultipartFile images to save
     * @return an ItemImageDTO containing the check ID, check URL, and list of saved image filenames
     * @throws ItemImageException if any image cannot be loaded or directory cannot be created
     */
    @Override
    public ItemImageDTO saveImages(List<MultipartFile> files) {
        // if (files.isEmpty())
        //     throw ItemImageException.builder()
        //                 .messageKey("saleservice.exception.itemimageservice.saveimage.fail.message")
        //                 .messageArgs(new String[]{})
        //                 .build();

        // Allocate normalized image names and map each to its byte data
        Map<String, byte[]> images = new HashMap<>();
        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            String ext = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
            String filename = UUID.randomUUID() + "." + ext;

            try {
                images.put(filename, file.getBytes());
            } catch (IOException ioe) {
                throw ItemImageException.builder()
                        .messageKey("saleservice.exception.itemimageservice.saveimages.cannotloadImage.message")
                        .messageArgs(new String[]{ file.getOriginalFilename() })
                        .build();
            }
        }

        // Generate a unique check ID and URL to monitor upload status
        String checkId = UUID.randomUUID().toString();
        String checkURL = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/check-upload-status")
                .path("/{id}")
                .buildAndExpand(checkId)
                .toUriString();

        // Create the image repository directory if it does not exist
        if (Files.notExists(IMAGE_REPOSITORY)) {
            try {
                Files.createDirectories(IMAGE_REPOSITORY);
            } catch (IOException e) {
                throw ItemImageException.builder()
                        .messageKey("saleservice.exception.itemimageservice.saveimages.cannotcreatedirectory.message")
                        .messageArgs(new String[]{ IMAGE_REPOSITORY.toAbsolutePath().toString() })
                        .build();
            }
        }

        // Execute asynchronous image saving process
        asyncSaveImageRunner.execute(images, checkId);

        return ItemImageDTO.builder()
                .checkId(checkId)
                .checkURL(checkURL)
                .images(images.keySet().stream().toList())
                .build();
    }

    /**
     * Checks the upload status of images by a given check ID.
     *
     * @param id the unique check ID for the image upload
     * @return ImageUploadStatusDTO representing the status of the image upload
     * @throws ItemImageException if the upload status for the given ID is not found
     */
    @Transactional(readOnly = true)
    @Override
    public ImageUploadStatusDTO checkUploadStatus(String id) {
        return imageUploadStatusRepository
                .findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> ItemImageException.builder()
                        .messageKey("saleservice.exception.itemimageservice.checkuploadstatus.notfound.message")
                        .messageArgs(new String[]{ id })
                        .build());
    }
}
