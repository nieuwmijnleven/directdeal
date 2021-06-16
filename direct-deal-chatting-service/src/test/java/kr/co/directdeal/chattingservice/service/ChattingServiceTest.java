package kr.co.directdeal.chattingservice.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.co.directdeal.chattingservice.domain.ChattingMessage;
import kr.co.directdeal.chattingservice.domain.ChattingRoom;
import kr.co.directdeal.chattingservice.exception.ChattingException;
import kr.co.directdeal.chattingservice.service.dto.ChattingMessageDTO;
import kr.co.directdeal.chattingservice.service.dto.ChattingRoomDTO;
import kr.co.directdeal.chattingservice.service.mapper.ChattingMessageMapper;
import kr.co.directdeal.chattingservice.service.mapper.ChattingRoomMapper;
import kr.co.directdeal.chattingservice.service.repository.ChattingRepository;
import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.common.security.util.SecurityUtils;

@ExtendWith(MockitoExtension.class)
public class ChattingServiceTest {
    
    @Mock
    private ChattingRepository chattingRepository;

    private ChattingService chattingService;

    private Mapper<ChattingMessage, ChattingMessageDTO> chattingMessageMapper = new ChattingMessageMapper();

    private Mapper<ChattingRoom, ChattingRoomDTO> chattingRoomMapper = new ChattingRoomMapper(chattingMessageMapper);

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void init() {
        this.chattingService = new ChattingService(
                                        chattingRepository, 
                                        chattingRoomMapper, 
                                        chattingMessageMapper);
    }

    @Test
    public void GetChattingRoomByDTO_NonExistingChattingRoom_ThrowChattingException() throws Exception {
        //given
        String srcJSON = "{\"itemId\":\"d4c02e9b-1497-48df-885e-6ba6426e73d2\", \"sellerId\":\"seller@directdeal.co.kr\", \"customerId\":\"customer@directdeal.co.kr\"}"; 
        ChattingRoomDTO dto = objectMapper.readValue(srcJSON, ChattingRoomDTO.class);

        given(chattingRepository
                    .findByItemIdAndSellerIdAndCustomerId(dto.getItemId(), dto.getSellerId(), dto.getCustomerId()))
            .willReturn(Optional.empty());

        //when and then
        assertThrows(ChattingException.class, () -> {
            chattingService.getChattingRoom(dto);
        });
    }

    @Test
    public void GetChattingRoomByDTO_InvalidTalker_ThrowChattingException() throws Exception {
        //given
        String srcJSON = "{\"itemId\":\"d4c02e9b-1497-48df-885e-6ba6426e73d2\", \"sellerId\":\"seller@directdeal.co.kr\", \"customerId\":\"customer@directdeal.co.kr\"}"; 
        ChattingRoomDTO dto = objectMapper.readValue(srcJSON, ChattingRoomDTO.class);

        given(chattingRepository
                    .findByItemIdAndSellerIdAndCustomerId(dto.getItemId(), dto.getSellerId(), dto.getCustomerId()))
            .willReturn(Optional.of(ChattingRoom.builder()
                                        .id(1L)
                                        .itemId(dto.getItemId())
                                        .sellerId(dto.getSellerId())
                                        .customerId(dto.getCustomerId())
                                        .createdDate(Instant.now())
                                        .build()));

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getCurrentUserLogin)
                .thenReturn("invalid-talker@directdeal.co.kr");

            //when and then
            assertThrows(ChattingException.class, () -> {
                chattingService.getChattingRoom(dto);
            });
        }
    }

    @Test
    public void GetChattingRoomByDTO_ValidChattingRoomAndValidTalker_Success() throws Exception {
        //given
        String srcJSON = "{\"itemId\":\"d4c02e9b-1497-48df-885e-6ba6426e73d2\", \"sellerId\":\"seller@directdeal.co.kr\", \"customerId\":\"customer@directdeal.co.kr\"}"; 
        ChattingRoomDTO dto = objectMapper.readValue(srcJSON, ChattingRoomDTO.class);

        Instant createdDate = Instant.now();

        given(chattingRepository
                    .findByItemIdAndSellerIdAndCustomerId(dto.getItemId(), dto.getSellerId(), dto.getCustomerId()))
            .willReturn(Optional.of(ChattingRoom.builder()
                                        .id(1L)
                                        .itemId(dto.getItemId())
                                        .sellerId(dto.getSellerId())
                                        .customerId(dto.getCustomerId())
                                        .messages(Collections.emptyList())
                                        .createdDate(createdDate)
                                        .build()));

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getCurrentUserLogin)
                .thenReturn("seller@directdeal.co.kr");

            //when
            ChattingRoomDTO resultDTO = chattingService.getChattingRoom(dto);
            
            //then
            verify(chattingRepository)
                    .findByItemIdAndSellerIdAndCustomerId(dto.getItemId(), dto.getSellerId(), dto.getCustomerId());

            assertThat(resultDTO.getId(), equalTo(1L));
            assertThat(resultDTO.getItemId(), equalTo(dto.getItemId()));
            assertThat(resultDTO.getSellerId(), equalTo(dto.getSellerId()));
            assertThat(resultDTO.getCustomerId(), equalTo(dto.getCustomerId()));
            assertThat(resultDTO.getMessages().isEmpty(), equalTo(true));
            assertThat(resultDTO.getCreatedDate(), equalTo(createdDate));

            assertEquals("seller@directdeal.co.kr", SecurityUtils.getCurrentUserLogin());
        }
    }

    @Test
    public void CreateChattingRoom_AlreadyCreated_ThrowChattingException() throws Exception {
        //given
        String srcJSON = "{\"itemId\":\"d4c02e9b-1497-48df-885e-6ba6426e73d2\", \"sellerId\":\"seller@directdeal.co.kr\", \"customerId\":\"customer@directdeal.co.kr\"}"; 
        ChattingRoomDTO dto = objectMapper.readValue(srcJSON, ChattingRoomDTO.class);

        given(chattingRepository
                    .existsByItemIdAndSellerIdAndCustomerId(dto.getItemId(), dto.getSellerId(), dto.getCustomerId()))
            .willReturn(true);

        //when and then
        assertThrows(ChattingException.class, () -> {
            chattingService.createChattingRoom(dto);
        });
    }

    @Test
    public void CreateChattingRoom_InvalidTalker_ThrowChattingException() throws Exception {
        //given
        String srcJSON = "{\"itemId\":\"d4c02e9b-1497-48df-885e-6ba6426e73d2\", \"sellerId\":\"seller@directdeal.co.kr\", \"customerId\":\"customer@directdeal.co.kr\"}"; 
        ChattingRoomDTO dto = objectMapper.readValue(srcJSON, ChattingRoomDTO.class);

        given(chattingRepository
                    .existsByItemIdAndSellerIdAndCustomerId(dto.getItemId(), dto.getSellerId(), dto.getCustomerId()))
            .willReturn(false);

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getCurrentUserLogin)
                .thenReturn("invalid-talker@directdeal.co.kr");

            //when and then
            assertThrows(ChattingException.class, () -> {
                chattingService.createChattingRoom(dto);
            });
        }
    }

    @Test
    public void CreateChattingRoom_NonAlreadyCreatedAndValidTalker_Success() throws Exception {
        //given
        String srcJSON = "{\"itemId\":\"d4c02e9b-1497-48df-885e-6ba6426e73d2\", \"sellerId\":\"seller@directdeal.co.kr\", \"customerId\":\"customer@directdeal.co.kr\"}"; 
        ChattingRoomDTO dto = objectMapper.readValue(srcJSON, ChattingRoomDTO.class);

        Instant createdDate = Instant.now();

        given(chattingRepository
                    .existsByItemIdAndSellerIdAndCustomerId(dto.getItemId(), dto.getSellerId(), dto.getCustomerId()))
            .willReturn(false);

        ChattingRoom chattingRoom = chattingRoomMapper.toEntity(dto);
        chattingRoom.setId(1L);
        chattingRoom.setCreatedDate(createdDate);

        given(chattingRepository.save(any(ChattingRoom.class)))
            .willReturn(chattingRoom);

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getCurrentUserLogin)
                .thenReturn("seller@directdeal.co.kr");

            //when
            ChattingRoomDTO resultDTO = chattingService.createChattingRoom(dto);

            //then
            verify(chattingRepository)
                    .existsByItemIdAndSellerIdAndCustomerId(dto.getItemId(), dto.getSellerId(), dto.getCustomerId());
            verify(chattingRepository).save(any(ChattingRoom.class));

            assertThat(resultDTO.getId(), equalTo(1L));
            assertThat(resultDTO.getItemId(), equalTo(dto.getItemId()));
            assertThat(resultDTO.getSellerId(), equalTo(dto.getSellerId()));
            assertThat(resultDTO.getCustomerId(), equalTo(dto.getCustomerId()));
            assertThat(resultDTO.getMessages().isEmpty(), equalTo(true));
            assertThat(resultDTO.getCreatedDate(), equalTo(createdDate));

            assertEquals("seller@directdeal.co.kr", SecurityUtils.getCurrentUserLogin());
        }
    }

    @Test
    public void SendMessage_InvalidChattingRoomId_ThrowChattingException() throws Exception {
        //given
        String srcJSON = "{\"chattingRoomId\":\"1\", \"talkerId\":\"customer@directdeal.co.kr\", \"text\":\"문의 드립니다.\"}"; 
        ChattingMessageDTO dto = objectMapper.readValue(srcJSON, ChattingMessageDTO.class);

        given(chattingRepository.findById(dto.getChattingRoomId()))
            .willReturn(Optional.empty());

        //when and then
        assertThrows(ChattingException.class, () -> {
            chattingService.sendMessage(dto);
        });
    }

    @Test
    public void SendMessage_InvalidTalker_ThrowChattingException() throws Exception {
        //given
        String srcJSON = "{\"chattingRoomId\":\"1\", \"talkerId\":\"customer@directdeal.co.kr\", \"text\":\"문의 드립니다.\"}"; 
        ChattingMessageDTO dto = objectMapper.readValue(srcJSON, ChattingMessageDTO.class);

        given(chattingRepository.findById(dto.getChattingRoomId()))
            .willReturn(Optional.of(mock(ChattingRoom.class)));

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getCurrentUserLogin)
                .thenReturn("invalid-talker@directdeal.co.kr");

            //when and then
            assertThrows(ChattingException.class, () -> {
                chattingService.sendMessage(dto);
            });
        }
    }

    @Test
    public void SendMessage_ValidChattingRoomIdAndTalker_Success() throws Exception {
        //given
        String srcJSON = "{\"chattingRoomId\":\"1\", \"talkerId\":\"customer@directdeal.co.kr\", \"text\":\"문의 드립니다.\"}"; 
        ChattingMessageDTO dto = objectMapper.readValue(srcJSON, ChattingMessageDTO.class);

        ChattingRoom chattingRoom = mock(ChattingRoom.class);
        given(chattingRoom.getSellerId())
            .willReturn("seller@directdeal.co.kr");
        // given(chattingRoom.getCustomerId())
        //     .willReturn("customer@directdeal.co.kr");

        given(chattingRepository.findById(dto.getChattingRoomId()))
            .willReturn(Optional.of(chattingRoom));

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getCurrentUserLogin)
                .thenReturn("seller@directdeal.co.kr");

            //when
            chattingService.sendMessage(dto);

            //then
            verify(chattingRoom).addMessage(chattingMessageMapper.toEntity(dto));
        }
    }

    @Test
    public void FetchUnreadMessage_InvalidChattingRoomId_ThrowChattingException() throws Exception {
        //given
        String srcJSON = "{\"chattingRoomId\":\"1\", \"talkerId\":\"customer@directdeal.co.kr\"}"; 
        ChattingMessageDTO dto = objectMapper.readValue(srcJSON, ChattingMessageDTO.class);

        given(chattingRepository.findById(dto.getChattingRoomId()))
            .willReturn(Optional.empty());

        //when and then
        assertThrows(ChattingException.class, () -> {
            chattingService.fetchUnreadMessage(dto);
        });
    }

    @Test
    public void FetchUnreadMessage_InvalidTalker_ThrowChattingException() throws Exception {
        //given
        String srcJSON = "{\"chattingRoomId\":\"1\", \"talkerId\":\"customer@directdeal.co.kr\"}"; 
        ChattingMessageDTO dto = objectMapper.readValue(srcJSON, ChattingMessageDTO.class);

        given(chattingRepository.findById(dto.getChattingRoomId()))
            .willReturn(Optional.of(mock(ChattingRoom.class)));

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getCurrentUserLogin)
                .thenReturn("invalid-talker@directdeal.co.kr");

            //when and then
            assertThrows(ChattingException.class, () -> {
                chattingService.fetchUnreadMessage(dto);
            });
        }
    }

    @Test
    public void FetchUnreadMessage_ValidChattingRoomIdAndTalker_ReturnUnreadMessages() throws Exception {
        //given
        String srcJSON = "{\"chattingRoomId\":\"1\", \"talkerId\":\"customer@directdeal.co.kr\"}"; 
        ChattingMessageDTO dto = objectMapper.readValue(srcJSON, ChattingMessageDTO.class);

        ChattingRoom chattingRoom = mock(ChattingRoom.class);
        given(chattingRoom.getSellerId())
            .willReturn("seller@directdeal.co.kr");
        // given(chattingRoom.getCustomerId())
        //     .willReturn("customer@directdeal.co.kr");
        given(chattingRoom.getMessages())
            .willReturn(Collections.singletonList(ChattingMessage.builder()
                                                    .talkerId("customer@directdeal.co.kr")
                                                    .text("문의 드립니다.")
                                                    .sent(false)
                                                    .build()));

        given(chattingRepository.findById(dto.getChattingRoomId()))
            .willReturn(Optional.of(chattingRoom));

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getCurrentUserLogin)
                .thenReturn("seller@directdeal.co.kr");

            //when
            List<ChattingMessageDTO> resultList = chattingService.fetchUnreadMessage(dto);

            //then
            assertThat(resultList.isEmpty(), equalTo(false));
            assertThat(resultList.get(0).getTalkerId(), equalTo("customer@directdeal.co.kr"));
            assertThat(resultList.get(0).getText(), equalTo("문의 드립니다."));
            assertThat(resultList.get(0).isSent(), equalTo(true));
        }
    }

    @Test
    public void FetchUnreadMessage_ValidChattingRoomIdAndTalker_ReturnEmptyMessages() throws Exception {
        //given
        String srcJSON = "{\"chattingRoomId\":\"1\", \"talkerId\":\"customer@directdeal.co.kr\"}"; 
        ChattingMessageDTO dto = objectMapper.readValue(srcJSON, ChattingMessageDTO.class);

        ChattingRoom chattingRoom = mock(ChattingRoom.class);
        given(chattingRoom.getSellerId())
            .willReturn("seller@directdeal.co.kr");
        // given(chattingRoom.getCustomerId())
        //     .willReturn("customer@directdeal.co.kr");
        given(chattingRoom.getMessages())
            .willReturn(Collections.emptyList());

        given(chattingRepository.findById(dto.getChattingRoomId()))
            .willReturn(Optional.of(chattingRoom));

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getCurrentUserLogin)
                .thenReturn("seller@directdeal.co.kr");

            //when
            List<ChattingMessageDTO> resultList = chattingService.fetchUnreadMessage(dto);

            //then
            assertThat(resultList.isEmpty(), equalTo(true));
        }
    }

    @Test
    public void FetchMessageFrom_InvalidChattingRoomId_ThrowChattingException() throws Exception {
        //given
        Long chattingRoomId = 1L;
        int skip = 1;

        given(chattingRepository.findById(chattingRoomId))
            .willReturn(Optional.empty());

        //when and then
        assertThrows(ChattingException.class, () -> {
            chattingService.fetchMessagesFrom(chattingRoomId, skip);
        });
    }

    @Test
    public void FetchMessageFrom_InvalidTalker_ThrowChattingException() throws Exception {
        //given
        Long chattingRoomId = 1L;
        int skip = 1;

        given(chattingRepository.findById(chattingRoomId))
            .willReturn(Optional.of(mock(ChattingRoom.class)));

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getCurrentUserLogin)
                .thenReturn("invalid-talker@directdeal.co.kr");

            //when and then
            assertThrows(ChattingException.class, () -> {
                chattingService.fetchMessagesFrom(chattingRoomId, skip);
            });
        }
    }

    @Test
    public void FetchMessageFrom_ValidChattingRoomIdAndTalker_ReturnUnreadMessages() throws Exception {
        //given
        Long chattingRoomId = 1L;
        int skip = 0;

        ChattingRoom chattingRoom = mock(ChattingRoom.class);
        given(chattingRoom.getSellerId())
            .willReturn("seller@directdeal.co.kr");
        // given(chattingRoom.getCustomerId())
        //     .willReturn("customer@directdeal.co.kr");
        given(chattingRoom.getMessages())
            .willReturn(Collections.singletonList(ChattingMessage.builder()
                                                    .talkerId("customer@directdeal.co.kr")
                                                    .text("문의 드립니다.")
                                                    .sent(false)
                                                    .build()));

        given(chattingRepository.findById(chattingRoomId))
            .willReturn(Optional.of(chattingRoom));

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getCurrentUserLogin)
                .thenReturn("seller@directdeal.co.kr");

            //when
            List<ChattingMessageDTO> resultList = chattingService.fetchMessagesFrom(chattingRoomId, skip);

            //then
            assertThat(resultList.isEmpty(), equalTo(false));
            assertThat(resultList.get(0).getTalkerId(), equalTo("customer@directdeal.co.kr"));
            assertThat(resultList.get(0).getText(), equalTo("문의 드립니다."));
            assertThat(resultList.get(0).isSent(), equalTo(true));
        }
    }

    @Test
    public void FetchMessageFrom_ValidChattingRoomIdAndTalker_ReturnEmptyMessage() throws Exception {
        //given
        Long chattingRoomId = 1L;
        int skip = 1;

        ChattingRoom chattingRoom = mock(ChattingRoom.class);
        given(chattingRoom.getSellerId())
            .willReturn("seller@directdeal.co.kr");
        // given(chattingRoom.getCustomerId())
        //     .willReturn("customer@directdeal.co.kr");
        given(chattingRoom.getMessages())
            .willReturn(Collections.singletonList(ChattingMessage.builder()
                                                    .talkerId("customer@directdeal.co.kr")
                                                    .text("문의 드립니다.")
                                                    .sent(false)
                                                    .build()));

        given(chattingRepository.findById(chattingRoomId))
            .willReturn(Optional.of(chattingRoom));

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getCurrentUserLogin)
                .thenReturn("seller@directdeal.co.kr");

            //when
            List<ChattingMessageDTO> resultList = chattingService.fetchMessagesFrom(chattingRoomId, skip);

            //then
            assertThat(resultList.isEmpty(), equalTo(true));
        }
    }
}
