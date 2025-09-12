package kr.co.directdeal.saleservice.adapter.inbound;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.directdeal.saleservice.application.service.ItemImageService;
import kr.co.directdeal.saleservice.application.service.dto.ImageUploadStatusDTO;
import kr.co.directdeal.saleservice.application.service.dto.ItemImageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller for handling item image upload and retrieval operations.
 * Provides endpoints for saving images, fetching images by filename,
 * and checking the upload status.
 *
 * @author Cheol Jeon
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/image")
@Tag(name = "Item Image API", description = "API for managing item images")
public class ItemImageController {

    private final ItemImageService itemImageService;

    /**
     * Upload multiple image files for an item.
     *
     * @param files List of image files to be uploaded
     * @return ItemImageDTO containing details of saved images
     */
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Upload Item Images", description = "Upload multiple image files for an item")
    public ItemImageDTO saveImages(@RequestParam("files") List<MultipartFile> files) {
        return itemImageService.saveImages(files);
    }

    /**
     * Retrieve an image by its filename.
     * Supports GIF, JPEG, and PNG media types.
     *
     * @param filename The filename of the image to retrieve
     * @return byte array of the image data
     */
    @GetMapping(value = "/{filename:.+}", produces = {
            MediaType.IMAGE_GIF_VALUE,
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE
    })
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get Item Image", description = "Retrieve an image by its filename")
    public byte[] getImage(@PathVariable("filename") String filename) {
        return itemImageService.readImage(filename);
    }

    /**
     * Check the upload status of an image by its ID.
     *
     * @param id The ID associated with the upload task
     * @return ImageUploadStatusDTO containing the upload status details
     */
    @GetMapping("/check-upload-status/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Check Image Upload Status", description = "Check the upload status of an image by ID")
    public ImageUploadStatusDTO checkUploadStatus(@PathVariable("id") String id) {
        return itemImageService.checkUploadStatus(id);
    }
}
