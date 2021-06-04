package kr.co.directdeal.saleservice.adapter.inbound;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kr.co.directdeal.saleservice.dto.ImageUploadStatusDTO;
import kr.co.directdeal.saleservice.dto.ItemImageDTO;
import kr.co.directdeal.saleservice.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/image")
public class ItemImageController {

    private final ImageService imageService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ItemImageDTO saveImages(@RequestParam("files") List<MultipartFile> files) {
        return imageService.saveImages(files);
    }
    
    @GetMapping(value = "/{filename:.+}", produces = {MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public byte[] getImage(@PathVariable("filename") String filename) {
        return imageService.readImage(filename);
    }

    @GetMapping("/check-upload-status/{id}")
    public ImageUploadStatusDTO checkUploadStatus(@PathVariable("id") String id) {
        return imageService.checkUploadStatus(id);
    }
}
