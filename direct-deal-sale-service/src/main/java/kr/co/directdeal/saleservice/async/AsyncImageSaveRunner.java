package kr.co.directdeal.saleservice.async;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import kr.co.directdeal.saleservice.domain.ImageUploadStatus;
import kr.co.directdeal.saleservice.service.ItemImageService;
import kr.co.directdeal.saleservice.service.repository.ImageUploadStatusRepository;
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
    public void execute(List<MultipartFile> files, List<String> images, String checkId) {
        ImageUploadStatus imageUploadStatus = ImageUploadStatus.builder()
                                                .id(checkId)
                                                .createdDate(Instant.now())
                                                .build();           
        //set the status to PROCESSING     
        updateImageUploadStatus(imageUploadStatus, ImageUploadStatus.Status.PROCESSING);

        // MultipartConfigElement
        int i = 0;
        for (MultipartFile file : files) {
            if (!(file instanceof CommonsMultipartFile)) 
                throw new IllegalArgumentException("The referece of MultipartFile is not the type of CommonsMultipartFile");         
            
            CommonsMultipartFile commonFile = (CommonsMultipartFile)file;

            if (!(commonFile.getFileItem() instanceof DiskFileItem)) 
                throw new IllegalArgumentException("The referece of FileItem is not the type of DiskFileItem");
            
            DiskFileItem diskFileItem = (DiskFileItem)commonFile.getFileItem();
            File uploadFile = diskFileItem.getStoreLocation();

            String filename = images.get(i);
            Path dest = Paths.get(ItemImageService.IMAGE_REPOSITORY_PATH, filename).toAbsolutePath();
            try {
                if (diskFileItem.isInMemory()) {
                    Files.write(dest, diskFileItem.get());
                    log.info("write the memory cached image to [{}]", dest);
                } else {
                    Files.move(uploadFile.toPath(), dest);
                    log.info("move [{}] to [{}]", uploadFile, dest);
                }
            } catch(Exception e) {
                //set the status to FAILURE
                log.error("fail to move [{}] to [{}]", uploadFile, dest);
                log.error("image file save error => type:{}, message:{}", e.getClass().getSimpleName(), e.getMessage());
                updateImageUploadStatus(imageUploadStatus, ImageUploadStatus.Status.FAILURE);
                return;
            }

            ++i;
        }

        //set the status to SUCCESS
        updateImageUploadStatus(imageUploadStatus, ImageUploadStatus.Status.SUCCESS);
    }
}
