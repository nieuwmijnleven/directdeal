package kr.co.directdeal.saleservice.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesRegex;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.saleservice.async.AsyncImageSaveRunner;
import kr.co.directdeal.saleservice.domain.ImageUploadStatus;
import kr.co.directdeal.saleservice.exception.ItemImageException;
import kr.co.directdeal.saleservice.service.dto.ImageUploadStatusDTO;
import kr.co.directdeal.saleservice.service.dto.ItemImageDTO;
import kr.co.directdeal.saleservice.service.mapper.ImageUploadStatusMapper;
import kr.co.directdeal.saleservice.service.repository.ImageUploadStatusRepository;

@ExtendWith(MockitoExtension.class)
public class ItemImageServiceTest {
    
    public static final String IMAGE_REPOSITORY_PATH = "resources/images";

    @Mock
    private AsyncImageSaveRunner asyncSaveImageRunner;

    @Mock
    private ImageUploadStatusRepository imageUploadStatusRepository;

    private Mapper<ImageUploadStatus, ImageUploadStatusDTO> mapper = new ImageUploadStatusMapper();

    private ItemImageService itemImageService;

    @BeforeEach
    public void init() {
        this.itemImageService = new ItemImageService(
                                        asyncSaveImageRunner,
                                        imageUploadStatusRepository, 
                                        mapper);
    }

    @Test
    public void ReadImage_IOException_ThrowItemImageException() {
        try (MockedStatic<Files> mocked = mockStatic(Files.class)) {
            //given
            mocked.when(() -> Files.readAllBytes(any(Path.class)))
                .thenThrow(new IOException());

            //when and then
            assertThrows(ItemImageException.class, () -> {
                itemImageService.readImage("1.png");
            });
        }
    }

    // @Test
    // public void saveImages_EmptyMultipartFileList_ThrowItemImageException() {
    //     //given
    //     List<MultipartFile> files = Collections.emptyList();

    //     //when and then
    //     assertThrows(ItemImageException.class, () -> {
    //         itemImageService.saveImages(files);
    //     });
    // }

    @Test
    public void saveImages_ValidMultipartFileList_Success() {
        //given
        MultipartFile file1 = mock(MultipartFile.class);
        given(file1.getOriginalFilename())
            .willReturn("1.png");

        MultipartFile file2 = mock(MultipartFile.class);
        given(file2.getOriginalFilename())
            .willReturn("2.png");

        List<MultipartFile> files = List.of(file1, file2);

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        //when
        ItemImageDTO resultDTO = itemImageService.saveImages(files);

        //then
        assertThat(resultDTO.getCheckId(), matchesRegex("[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}"));
        assertThat(resultDTO.getCheckURL(), equalTo("http://localhost/check-upload-status/" + resultDTO.getCheckId()));
        assertThat(resultDTO.getImages().get(0), matchesRegex("[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}\\.png"));
        assertThat(resultDTO.getImages().get(1), matchesRegex("[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}\\.png"));
    }

    @Test
    public void CheckUploadStatus_InvalidId_ThrowItemImageException() {
            //given
            String id = "1";
            given(imageUploadStatusRepository.findById(id))
                .willReturn(Optional.empty());

            //when and then
            assertThrows(ItemImageException.class, () -> {
                itemImageService.checkUploadStatus(id);
            });
    }

    @Test
    public void CheckUploadStatus_ValidId_ThrowItemImageException() {
            //given
            String id = "1";
            ImageUploadStatus entity = ImageUploadStatus.builder()
                                            .id("id")
                                            .status(ImageUploadStatus.Status.PROCESSING)
                                            .createdDate(Instant.now())
                                            .build();

            given(imageUploadStatusRepository.findById(id))
                .willReturn(Optional.of(entity));

            //when
            ImageUploadStatusDTO resultDTO = itemImageService.checkUploadStatus(id);
            
            //then
            assertThat(resultDTO.getId(), equalTo(entity.getId()));
            assertThat(resultDTO.getStatus(), equalTo(entity.getStatus()));
            assertThat(resultDTO.getStatus(), equalTo(entity.getStatus()));
            assertThat(resultDTO.getCreatedDate(), equalTo(entity.getCreatedDate()));
    }
}
