package kr.co.directdeal.saleservice.async;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.time.Instant;
import java.util.Map;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kr.co.directdeal.saleservice.domain.object.ImageUploadStatus;
import kr.co.directdeal.saleservice.application.service.ItemImageService;
import kr.co.directdeal.saleservice.port.outbound.ImageUploadStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Async component responsible for saving uploaded images to the file system
 * and updating the upload status accordingly.
 *
 * Uses a Spring async executor named "saleThreadPoolTaskExecutor" for async execution.
 *
 * Updates ImageUploadStatus entity in the repository to track progress.
 *
 * @author Cheol Jeon
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AsyncImageSaveRunner {

    private final ImageUploadStatusRepository imageUploadStatusRepository;

    /**
     * Updates the upload status of the given ImageUploadStatus entity and persists it.
     * Transactional method to ensure DB consistency.
     *
     * @param imageUploadStatus the entity to update
     * @param status the new status to set
     * @return the saved ImageUploadStatus entity
     */
    @Transactional
    private ImageUploadStatus updateImageUploadStatus(ImageUploadStatus imageUploadStatus, ImageUploadStatus.Status status) {
        imageUploadStatus.setStatus(status);
        return imageUploadStatusRepository.save(imageUploadStatus);
    }

    /**
     * Asynchronously saves the given images to the file system under configured directory.
     * Updates upload status to PROCESSING at start, SUCCESS when finished, or FAILURE if any error occurs.
     *
     * @param images a map of filename to byte array data for each image
     * @param checkId the unique ID for this upload batch used to track status
     */
    @Async("saleThreadPoolTaskExecutor")
    public void execute(Map<String, byte[]> images, String checkId) {
        ImageUploadStatus imageUploadStatus = ImageUploadStatus.builder()
                .id(checkId)
                .createdDate(Instant.now())
                .build();
        // set the status to PROCESSING
        updateImageUploadStatus(imageUploadStatus, ImageUploadStatus.Status.PROCESSING);

        int i = 0;
        for (var entry : images.entrySet()) {
            String filename = entry.getKey();
            byte[] data = entry.getValue();

            Path dest = Paths.get(ItemImageService.IMAGE_REPOSITORY_PATH, filename).toAbsolutePath();

            try {
                Files.write(dest, data, StandardOpenOption.CREATE_NEW);

                log.info("saved uploaded image to [{}]", dest.toAbsolutePath());
            } catch (Exception e) {
                log.error("fail to save [{}] => {}", filename, e.getMessage(), e);
                updateImageUploadStatus(imageUploadStatus, ImageUploadStatus.Status.FAILURE);
                return;
            }
            ++i;
        }

        // set the status to SUCCESS
        updateImageUploadStatus(imageUploadStatus, ImageUploadStatus.Status.SUCCESS);
    }
}
