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

@Slf4j
@RequiredArgsConstructor
@Component
public class AsyncImageSaveRunner {

    private final ImageUploadStatusRepository imageUploadStatusRepository;

    @Transactional
    private ImageUploadStatus updateImageUploadStatus(ImageUploadStatus imageUploadStatus, ImageUploadStatus.Status status) {  
        imageUploadStatus.setStatus(status);
        return imageUploadStatusRepository.save(imageUploadStatus);
    }
    
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
                // File destFile = dest.toFile();
                // file.transferTo(destFile);
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