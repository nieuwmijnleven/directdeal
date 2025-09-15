package kr.co.directdeal.saleservice.adapter.inbound;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import kr.co.directdeal.common.security.auth.jwt.JwtAccessDeniedHandler;
import kr.co.directdeal.common.security.auth.jwt.JwtAuthenticationEntryPoint;
import kr.co.directdeal.common.security.auth.jwt.TokenProvider;
import kr.co.directdeal.common.security.config.props.JWTProperties;
import kr.co.directdeal.saleservice.async.AsyncImageSaveRunner;
import kr.co.directdeal.saleservice.exception.ItemImageException;
import kr.co.directdeal.saleservice.application.service.ItemImageService;
import kr.co.directdeal.saleservice.application.service.mapper.ImageUploadStatusMapper;
import kr.co.directdeal.saleservice.port.outbound.ImageUploadStatusRepository;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {ItemImageController.class},
    includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, 
        classes = {TokenProvider.class, JWTProperties.class,
                    JwtAuthenticationEntryPoint.class, JwtAccessDeniedHandler.class,
                    ImageUploadStatusRepository.class, ImageUploadStatusMapper.class,
                    AsyncImageSaveRunner.class}))
public class ItemImageControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemImageService itemImageService;

    @MockBean
    private AsyncImageSaveRunner asyncImageSaveRunner;

    @MockBean
    private ImageUploadStatusMapper imageUploadStatusMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void SaveImages_NoUploadFiles_ThrowMissingServletRequestPartException() throws Exception {
        //given
        //when and then
        this.mvc.perform(multipart("/image").with(csrf()))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", is("File Upload Error")))
                    .andExpect(jsonPath("$.message", is("Required part 'files' is not present.")));
    }

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void SaveImages_HasUploadFiles_Success() throws Exception {
        //given
        MockMultipartFile file 
            = new MockMultipartFile(
              "files", 
              "1.png", 
              MediaType.IMAGE_PNG_VALUE, 
              "image binary".getBytes()
            );

        //when and then
        this.mvc.perform(multipart("/image").file(file).with(csrf()))
                    .andDo(print())
                    .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void GetImage_ValidFilename_Success() throws Exception {
        //given
        String filename = "1.gif";
        byte[] imageBinary = new byte[]{0x1,0x2,0x3,0x4,0x5,0x6,0x7};

        given(itemImageService.readImage(filename))
            .willReturn(imageBinary);

        //when and then
        this.mvc.perform(get("/image/" + filename))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.IMAGE_GIF_VALUE))
                    .andExpect(content().bytes(imageBinary));

        verify(itemImageService).readImage(filename);
    }

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void CheckUploadStatus_InvalidId_ThrowItemImageException() throws Exception {
        //given
        String id = "1";
        willThrow(ItemImageException.builder()
                    .messageKey("saleservice.exception.itemimageservice.checkuploadstatus.notfound.message")
                    .messageArgs(new String[]{ id })
                    .build())
            .given(itemImageService).checkUploadStatus(id);

        //when and then
        this.mvc.perform(get("/image/check-upload-status/" + id))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", is("Item Image Service Error")))
                    .andExpect(jsonPath("$.message", is("image(1) is not found")));

        verify(itemImageService).checkUploadStatus(id);
    }
}
