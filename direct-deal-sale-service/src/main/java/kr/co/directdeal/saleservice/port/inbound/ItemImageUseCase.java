package kr.co.directdeal.saleservice.port.inbound;

import kr.co.directdeal.saleservice.application.service.dto.ImageUploadStatusDTO;
import kr.co.directdeal.saleservice.application.service.dto.ItemImageDTO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Use case interface for managing item images.
 *
 * Provides methods to read images, save multiple images, and check the status of image uploads.
 *
 * @author Cheol Jeon
 */
public interface ItemImageUseCase {

    /**
     * Reads the image data by filename.
     *
     * @param filename the name of the image file to read
     * @return byte array of the image data
     */
    byte[] readImage(String filename);

    /**
     * Saves multiple image files.
     *
     * @param files list of image files to save
     * @return the DTO containing details about saved images
     */
    ItemImageDTO saveImages(List<MultipartFile> files);

    /**
     * Checks the upload status of images by an upload ID.
     *
     * @param id the upload status identifier
     * @return the DTO containing the status of the image upload
     */
    ImageUploadStatusDTO checkUploadStatus(String id);
}
