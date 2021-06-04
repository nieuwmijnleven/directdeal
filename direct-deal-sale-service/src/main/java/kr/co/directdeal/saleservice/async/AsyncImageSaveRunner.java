package kr.co.directdeal.saleservice.async;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import com.google.common.io.Files;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import kr.co.directdeal.saleservice.domain.ImageUploadStatus;
import kr.co.directdeal.saleservice.service.ImageService;
import kr.co.directdeal.saleservice.service.ImageUploadStatusRepository;
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
    
    public void execute(List<MultipartFile> files, List<String> images, String checkId) {
        ImageUploadStatus imageUploadStatus = ImageUploadStatus.builder()
                                                .id(checkId)
                                                // .status(ImageUploadStatus.Status.PROCESSING)
                                                .createdDate(Instant.now())
                                                .build();           
        //set the status to PROCESSING     
        updateImageUploadStatus(imageUploadStatus, ImageUploadStatus.Status.PROCESSING);

        // MultipartConfigElement

        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            String ext = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
            String filename = UUID.randomUUID().toString() + "." + ext;

            log.debug("filename => " + filename);

            Path dest = Paths.get(ImageService.IMAGE_REPOSITORY_PATH, filename).toAbsolutePath();
            try {
                CommonsMultipartFile commonFile = (CommonsMultipartFile)file;
                log.debug("file.getFileItem().getName() => " + commonFile.getFileItem().getName());
                DiskFileItem diskFileItem = (DiskFileItem)commonFile.getFileItem();
                log.debug("diskFileItem.getStoreLocation() => " + diskFileItem.getStoreLocation());
                File uploadFile = diskFileItem.getStoreLocation();
                Files.move(uploadFile, dest.toFile());
                //file.transferTo(dest);
                // Files.copy(file.getInputStream(), dest);
            } catch(Exception e) {
                //set the status to FAILURE
                log.debug("file save error => " + e.getClass().getSimpleName());
                log.debug("file save error => " + e.getMessage());
                updateImageUploadStatus(imageUploadStatus, ImageUploadStatus.Status.FAILURE);
                return;
            }
        }

        //set the status to SUCCESS
        updateImageUploadStatus(imageUploadStatus, ImageUploadStatus.Status.SUCCESS);
    }
}
