package kr.co.directdeal.chattingservice.adapter.inbound;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import kr.co.directdeal.chattingservice.exception.ChattingException;
import kr.co.directdeal.chattingservice.application.service.ChattingService;
import kr.co.directdeal.chattingservice.application.service.dto.ChattingMessageDTO;
import kr.co.directdeal.chattingservice.application.service.dto.ChattingRoomDTO;
import kr.co.directdeal.chattingservice.application.service.mapper.ChattingMessageMapper;
import kr.co.directdeal.chattingservice.application.service.mapper.ChattingRoomMapper;
import kr.co.directdeal.chattingservice.port.outbound.ChattingRepository;
import kr.co.directdeal.common.security.auth.jwt.JwtAccessDeniedHandler;
import kr.co.directdeal.common.security.auth.jwt.JwtAuthenticationEntryPoint;
import kr.co.directdeal.common.security.auth.jwt.TokenProvider;
import kr.co.directdeal.common.security.config.props.JWTProperties;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {ChattingController.class},
    includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, 
        classes = {TokenProvider.class, JWTProperties.class,
                    JwtAuthenticationEntryPoint.class, JwtAccessDeniedHandler.class,
                    ChattingRepository.class, ChattingRoomMapper.class, ChattingMessageMapper.class}))
public class ChattingControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ChattingService chattingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void GetChattingRoom_NonExistingChattingRoom_ThrowChattingException() throws Exception {
        //given
        String itemId = "d4c02e9b-1497-48df-885e-6ba6426e73d2";
        String sellerId = "seller@directdeal.co.kr";
        String customerId = "customer@directdeal.co.kr";

        ChattingRoomDTO dto = ChattingRoomDTO.builder()
                                    .itemId(itemId)
                                    .sellerId(sellerId)
                                    .customerId(customerId)
                                    .build();

        given(chattingService.getChattingRoom(dto))
            .willThrow(ChattingException.builder()
                            .messageKey("chattingroomservice.exception.findchattingroom.chattingroom.notfound.message")
                            .messageArgs(new String[]{ dto.getItemId(), dto.getSellerId(), dto.getCustomerId() })
                            .build());

        //when and then
        this.mvc.perform(get(String.format("/chatting/%s/%s/%s", itemId, sellerId, customerId)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", is("Chatting Service Error")))
                    .andExpect(jsonPath("$.message", is("Failed to find the chatting room(itemId = d4c02e9b-1497-48df-885e-6ba6426e73d2, sellerId = seller@directdeal.co.kr, customerId = customer@directdeal.co.kr)")));
    }

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void GetChattingRoom_InvalidTalker_ThrowChattingException() throws Exception {
        //given
        String itemId = "d4c02e9b-1497-48df-885e-6ba6426e73d2";
        String sellerId = "seller@directdeal.co.kr";
        String customerId = "customer@directdeal.co.kr";

        ChattingRoomDTO dto = ChattingRoomDTO.builder()
                                    .itemId(itemId)
                                    .sellerId(sellerId)
                                    .customerId(customerId)
                                    .build();

        given(chattingService.getChattingRoom(dto))
            .willThrow(ChattingException.builder()
                            .messageKey("chattingroomservice.exception.findchattingroom.chattingroom.invalidchattinguser.message")
                            .messageArgs(new String[]{})
                            .build());

        //when and then
        this.mvc.perform(get(String.format("/chatting/%s/%s/%s", itemId, sellerId, customerId)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", is("Chatting Service Error")))
                    .andExpect(jsonPath("$.message", is("An invalid chatting user")));
    }

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void GetChattingRoom_ValidChattingRoomAndValidTalker_Success() throws Exception {
        String itemId = "d4c02e9b-1497-48df-885e-6ba6426e73d2";
        String sellerId = "seller@directdeal.co.kr";
        String customerId = "customer@directdeal.co.kr";

        ChattingRoomDTO dto = ChattingRoomDTO.builder()
                                    .itemId(itemId)
                                    .sellerId(sellerId)
                                    .customerId(customerId)
                                    .build();

        ChattingRoomDTO resultDTO = ChattingRoomDTO.builder()
                                        .id(1L)
                                        .itemId(itemId)
                                        .sellerId(sellerId)
                                        .customerId(customerId)
                                        .messages(List.of(ChattingMessageDTO.builder()
                                                                .id(1L)
                                                                .chattingRoomId(1L)
                                                                .talkerId(sellerId)
                                                                .text("Hi!")
                                                                .createdDate(Instant.now())
                                                                .build()))
                                        .createdDate(Instant.now().minus(1, ChronoUnit.HOURS))
                                        .build();

        given(chattingService.getChattingRoom(dto))
            .willReturn(resultDTO);

        //when and then
        this.mvc.perform(get(String.format("/chatting/%s/%s/%s", itemId, sellerId, customerId)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(resultDTO.getId().intValue())))
                    .andExpect(jsonPath("$.itemId", is(resultDTO.getItemId())))
                    .andExpect(jsonPath("$.sellerId", is(resultDTO.getSellerId())))
                    .andExpect(jsonPath("$.customerId", is(resultDTO.getCustomerId())))
                    .andExpect(jsonPath("$.createdDate", is(resultDTO.getCreatedDate().toString())))
                    .andExpect(jsonPath("$.messages[0].id", is(resultDTO.getMessages().get(0).getId().intValue())))
                    .andExpect(jsonPath("$.messages[0].chattingRoomId", is(resultDTO.getMessages().get(0).getChattingRoomId().intValue())))
                    .andExpect(jsonPath("$.messages[0].talkerId", is(resultDTO.getMessages().get(0).getTalkerId())))
                    .andExpect(jsonPath("$.messages[0].text", is(resultDTO.getMessages().get(0).getText())))
                    .andExpect(jsonPath("$.messages[0].createdDate", is(resultDTO.getMessages().get(0).getCreatedDate().toString())));
    }

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void FetchUnreadMessage_InvalidChattingRoomId_ThrowChattingException() throws Exception {
        //given
        String srcJSON = "{\"chattingRoomId\":\"1\", \"talkerId\":\"customer@directdeal.co.kr\"}"; 
        ChattingMessageDTO dto = objectMapper.readValue(srcJSON, ChattingMessageDTO.class);

        given(chattingService.fetchUnreadMessage(dto))
            .willThrow(ChattingException.builder()
                            .messageKey("chattingroomservice.exception.sendchattingmessage.chattingroom.notfound.message")
                            .messageArgs(new String[]{ dto.getChattingRoomId().toString() })
                            .build());

        //when and then
        this.mvc.perform(get("/chatting/fetch-unread-messages")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(srcJSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", is("Chatting Service Error")))
                    .andExpect(jsonPath("$.message", is("Failed to find the chatting room(id = 1)")));
    }

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void FetchUnreadMessage_InvalidTalker_ThrowChattingException() throws Exception {
        //given
        String srcJSON = "{\"chattingRoomId\":\"1\", \"talkerId\":\"customer@directdeal.co.kr\"}"; 
        ChattingMessageDTO dto = objectMapper.readValue(srcJSON, ChattingMessageDTO.class);

        given(chattingService.fetchUnreadMessage(dto))
            .willThrow(ChattingException.builder()
                            .messageKey("chattingroomservice.exception.findchattingroom.chattingroom.invalidchattinguser.message")
                            .messageArgs(new String[]{})
                            .build());

        //when and then
        this.mvc.perform(get("/chatting/fetch-unread-messages")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(srcJSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", is("Chatting Service Error")))
                    .andExpect(jsonPath("$.message", is("An invalid chatting user")));
    }

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void FetchUnreadMessage_ValidChattingRoomIdAndTalker_ReturnUnreadMessages() throws Exception {
        //given
        String srcJSON = "{\"chattingRoomId\":\"1\", \"talkerId\":\"customer@directdeal.co.kr\"}"; 
        ChattingMessageDTO dto = objectMapper.readValue(srcJSON, ChattingMessageDTO.class);

        ChattingMessageDTO resultDTO = ChattingMessageDTO.builder()
                                            .id(1L)
                                            .chattingRoomId(1L)
                                            .talkerId("customer@directdeal.co.kr")
                                            .text("Hi!")
                                            .createdDate(Instant.now())
                                            .sent(true)
                                            .build();

        given(chattingService.fetchUnreadMessage(dto))
            .willReturn(List.of(resultDTO));

        //when and then
        this.mvc.perform(get("/chatting/fetch-unread-messages")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(srcJSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id", is(resultDTO.getId().intValue())))
            .andExpect(jsonPath("$[0].chattingRoomId", is(resultDTO.getChattingRoomId().intValue())))
            .andExpect(jsonPath("$[0].talkerId", is(resultDTO.getTalkerId())))
            .andExpect(jsonPath("$[0].text", is(resultDTO.getText())))
            .andExpect(jsonPath("$[0].createdDate", is(resultDTO.getCreatedDate().toString())))
            .andExpect(jsonPath("$[0].sent", is(resultDTO.isSent())));

        verify(chattingService).fetchUnreadMessage(dto);
    }

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void FetchMessageFrom_InvalidChattingRoomId_ThrowChattingException() throws Exception {
        //given
        Long chattingRoomId = 1L;
        int skip = 1;

        given(chattingService.fetchMessagesFrom(chattingRoomId, skip))
            .willThrow(ChattingException.builder()
                            .messageKey("chattingroomservice.exception.sendchattingmessage.chattingroom.notfound.message")
                            .messageArgs(new String[]{ chattingRoomId.toString() })
                            .build());

        //when and then
        this.mvc.perform(get("/chatting/" + chattingRoomId + "/fetch-from/" + skip))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", is("Chatting Service Error")))
                    .andExpect(jsonPath("$.message", is("Failed to find the chatting room(id = 1)")));
    }

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void FetchMessageFrom_InvalidTalker_ThrowChattingException() throws Exception {
        //given
        Long chattingRoomId = 1L;
        int skip = 1;

        given(chattingService.fetchMessagesFrom(chattingRoomId, skip))
            .willThrow(ChattingException.builder()
                            .messageKey("chattingroomservice.exception.findchattingroom.chattingroom.invalidchattinguser.message")
                            .messageArgs(new String[]{})
                            .build());

        //when and then
        this.mvc.perform(get("/chatting/" + chattingRoomId + "/fetch-from/" + skip))
                    .andDo(print())
                    .andExpect(jsonPath("$.error", is("Chatting Service Error")))
                    .andExpect(jsonPath("$.message", is("An invalid chatting user")));
    }

    @Test
    @WithMockUser(username = "customer@directdeal.co.kr")
    public void FetchMessageFrom_ValidChattingRoomIdAndTalker_ReturnUnreadMessages() throws Exception {
        //given
        Long chattingRoomId = 1L;
        int skip = 0;

        ChattingMessageDTO resultDTO = ChattingMessageDTO.builder()
                                            .id(1L)
                                            .chattingRoomId(1L)
                                            .talkerId("customer@directdeal.co.kr")
                                            .text("Hi!")
                                            .createdDate(Instant.now().minus(1, ChronoUnit.MINUTES))
                                            .sent(true)
                                            .build();

        given(chattingService.fetchMessagesFrom(chattingRoomId, skip))
            .willReturn(List.of(resultDTO));

        //when and then
        this.mvc.perform(get("/chatting/" + chattingRoomId + "/fetch-from/" + skip))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(resultDTO.getId().intValue())))
                .andExpect(jsonPath("$[0].chattingRoomId", is(resultDTO.getChattingRoomId().intValue())))
                .andExpect(jsonPath("$[0].talkerId", is(resultDTO.getTalkerId())))
                .andExpect(jsonPath("$[0].text", is(resultDTO.getText())))
                .andExpect(jsonPath("$[0].createdDate", is(resultDTO.getCreatedDate().toString())))
                .andExpect(jsonPath("$[0].sent", is(resultDTO.isSent())));

        verify(chattingService).fetchMessagesFrom(chattingRoomId, skip);
    }

    @Test
    @WithMockUser(username = "customer@directdeal.co.kr")
    public void CreateChattingRoom_AlreadyCreated_ThrowChattingException() throws Exception {
        //given
        String srcJSON = "{\"itemId\":\"d4c02e9b-1497-48df-885e-6ba6426e73d2\", \"sellerId\":\"seller@directdeal.co.kr\", \"customerId\":\"customer@directdeal.co.kr\"}"; 
        ChattingRoomDTO dto = objectMapper.readValue(srcJSON, ChattingRoomDTO.class);

        given(chattingService.createChattingRoom(dto))
            .willThrow(ChattingException.builder()
                            .messageKey("chattingroomservice.exception.createchattingroom.chattingroom.hasalreadycreated.message")
                            .messageArgs(new String[]{})
                            .build());

        //when and then
        this.mvc.perform(post("/chatting")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(srcJSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Chatting Service Error")))
                .andExpect(jsonPath("$.message", is("The chatting room(itemId = {0}, sellerId = {1}, customerId = {2}) has already created")));
    }

    @Test
    @WithMockUser(username = "customer@directdeal.co.kr")
    public void CreateChattingRoom_InvalidTalker_ThrowChattingException() throws Exception {
        //given
        String srcJSON = "{\"itemId\":\"d4c02e9b-1497-48df-885e-6ba6426e73d2\", \"sellerId\":\"seller@directdeal.co.kr\", \"customerId\":\"customer@directdeal.co.kr\"}"; 
        ChattingRoomDTO dto = objectMapper.readValue(srcJSON, ChattingRoomDTO.class);

        given(chattingService.createChattingRoom(dto))
            .willThrow(ChattingException.builder()
                            .messageKey("chattingroomservice.exception.findchattingroom.chattingroom.invalidchattinguser.message")
                            .messageArgs(new String[]{})
                            .build());

        //when and then
        this.mvc.perform(post("/chatting")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(srcJSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Chatting Service Error")))
                .andExpect(jsonPath("$.message", is("An invalid chatting user")));
    }

    @Test
    @WithMockUser(username = "customer@directdeal.co.kr")
    public void CreateChattingRoom_NonAlreadyCreatedAndValidTalker_Success() throws Exception {
        //given
        String srcJSON = "{\"itemId\":\"d4c02e9b-1497-48df-885e-6ba6426e73d2\", \"sellerId\":\"seller@directdeal.co.kr\", \"customerId\":\"customer@directdeal.co.kr\"}"; 
        ChattingRoomDTO dto = objectMapper.readValue(srcJSON, ChattingRoomDTO.class);

        ChattingRoomDTO resultDTO = ChattingRoomDTO.builder()
                                        .id(1L)
                                        .itemId(dto.getItemId())
                                        .sellerId(dto.getSellerId())
                                        .customerId(dto.getCustomerId())
                                        .messages(List.of(ChattingMessageDTO.builder()
                                                                .id(1L)
                                                                .chattingRoomId(1L)
                                                                .talkerId(dto.getSellerId())
                                                                .text("Hi!")
                                                                .createdDate(Instant.now())
                                                                .build()))
                                        .createdDate(Instant.now().minus(1, ChronoUnit.HOURS))
                                        .build();

        given(chattingService.createChattingRoom(dto))
            .willReturn(resultDTO);

        //when and then
        this.mvc.perform(post("/chatting")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(srcJSON))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", is(resultDTO.getId().intValue())))
                    .andExpect(jsonPath("$.itemId", is(resultDTO.getItemId())))
                    .andExpect(jsonPath("$.sellerId", is(resultDTO.getSellerId())))
                    .andExpect(jsonPath("$.customerId", is(resultDTO.getCustomerId())))
                    .andExpect(jsonPath("$.createdDate", is(resultDTO.getCreatedDate().toString())))
                    .andExpect(jsonPath("$.messages[0].id", is(resultDTO.getMessages().get(0).getId().intValue())))
                    .andExpect(jsonPath("$.messages[0].chattingRoomId", is(resultDTO.getMessages().get(0).getChattingRoomId().intValue())))
                    .andExpect(jsonPath("$.messages[0].talkerId", is(resultDTO.getMessages().get(0).getTalkerId())))
                    .andExpect(jsonPath("$.messages[0].text", is(resultDTO.getMessages().get(0).getText())))
                    .andExpect(jsonPath("$.messages[0].createdDate", is(resultDTO.getMessages().get(0).getCreatedDate().toString())));
    }

    @Test
    @WithMockUser(username = "customer@directdeal.co.kr")
    public void SendMessage_InvalidChattingRoomId_ThrowChattingException() throws Exception {
        //given
        String srcJSON = "{\"chattingRoomId\":\"1\", \"talkerId\":\"customer@directdeal.co.kr\", \"text\":\"Hi!\"}"; 
        ChattingMessageDTO dto = objectMapper.readValue(srcJSON, ChattingMessageDTO.class);

        willThrow(ChattingException.builder()
                        .messageKey("chattingroomservice.exception.sendchattingmessage.chattingroom.notfound.message")
                        .messageArgs(new String[]{ dto.getChattingRoomId().toString() })
                        .build())
            .given(chattingService).sendMessage(dto);
        
        //when and then
        this.mvc.perform(put("/chatting")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(srcJSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", is("Chatting Service Error")))
                    .andExpect(jsonPath("$.message", is("Failed to find the chatting room(id = 1)")));        
    }

    @Test
    @WithMockUser(username = "customer@directdeal.co.kr")
    public void SendMessage_InvalidTalker_ThrowChattingException() throws Exception {
        //given
        String srcJSON = "{\"chattingRoomId\":\"1\", \"talkerId\":\"customer@directdeal.co.kr\", \"text\":\"Hi!\"}"; 
        ChattingMessageDTO dto = objectMapper.readValue(srcJSON, ChattingMessageDTO.class);

        willThrow(ChattingException.builder()
                        .messageKey("chattingroomservice.exception.findchattingroom.chattingroom.invalidchattinguser.message")
                        .messageArgs(new String[]{})
                        .build())
            .given(chattingService).sendMessage(dto);
        
        //when and then
        this.mvc.perform(put("/chatting")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(srcJSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", is("Chatting Service Error")))
                    .andExpect(jsonPath("$.message", is("An invalid chatting user")));  
    }

    @Test
    @WithMockUser(username = "customer@directdeal.co.kr")
    public void SendMessage_ValidChattingRoomIdAndTalker_Success() throws Exception {
        //given
        String srcJSON = "{\"chattingRoomId\":\"1\", \"talkerId\":\"customer@directdeal.co.kr\", \"text\":\"Hi!\"}"; 
        ChattingMessageDTO dto = objectMapper.readValue(srcJSON, ChattingMessageDTO.class);

        //when and then
        this.mvc.perform(put("/chatting")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(srcJSON))
                    .andDo(print())
                    .andExpect(status().isCreated());
        
        verify(chattingService).sendMessage(dto);
    }

    @Test
    @WithMockUser(username = "customer@directdeal.co.kr")
    public void CustomerList_ValidItemId_ReturnCustomerList() throws Exception {
        //given
        String itemId = "0d49649c-ee95-4a6a-ad92-369bde5ad8b7";
        given(chattingService.getCustomerIdList(itemId))
            .willReturn(List.of("customer1@directdeal.co.kr", "customer2@directdeal.co.kr"));

        //when and then
        this.mvc.perform(get("/chatting/" + itemId + "/customer-list"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0]", is("customer1@directdeal.co.kr")))
                    .andExpect(jsonPath("$[1]", is("customer2@directdeal.co.kr")));
        
        verify(chattingService).getCustomerIdList(itemId);
    }
}
