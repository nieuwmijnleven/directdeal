package kr.co.directdeal.chattingservice.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

import java.time.Instant;
import java.util.Collections;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
                                        .id("1")
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
                                        .id("1")
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

            assertThat(resultDTO.getId(), equalTo("1"));
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

        //when and then
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
        chattingRoom.setId("1");
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

            assertThat(resultDTO.getId(), equalTo("1"));
            assertThat(resultDTO.getItemId(), equalTo(dto.getItemId()));
            assertThat(resultDTO.getSellerId(), equalTo(dto.getSellerId()));
            assertThat(resultDTO.getCustomerId(), equalTo(dto.getCustomerId()));
            assertThat(resultDTO.getMessages().isEmpty(), equalTo(true));
            assertThat(resultDTO.getCreatedDate(), equalTo(createdDate));

            assertEquals("seller@directdeal.co.kr", SecurityUtils.getCurrentUserLogin());
        }
    }


    /*@Transactional
    @Transactional
    public ChattingRoomDTO createChattingRoom(ChattingRoomDTO dto) {
        //check if the chatting room has already created
        checkAlreadyCreated(dto);

        //check if valid talkers
        ChattingRoom newChattingRoom = chattingRoomMapper.toEntity(dto);
        checkValidTalker(newChattingRoom);  
 
        newChattingRoom.setCreatedDate(Instant.now());
        newChattingRoom = chattingRepository.save(newChattingRoom);
        return chattingRoomMapper.toDTO(newChattingRoom);
    }

    @Transactional
    public void sendMessage(ChattingMessageDTO dto) {
        ChattingRoom chattingRoom = findChattingRoomById(dto.getChattingRoomId());
        checkValidTalker(chattingRoom);

        dto.setCreatedDate(Instant.now());
        chattingRoom.addMessage(chattingMessageMapper.toEntity(dto));                                    
    }

    @Transactional
    public List<ChattingMessageDTO> fetchUnreadMessage(ChattingMessageDTO dto) {
        ChattingRoom chattingRoom = findChattingRoomById(dto.getChattingRoomId());
        checkValidTalker(chattingRoom);

        return chattingRoom.getMessages()
                    .stream()
                    .filter(ChattingMessage::isNotSent)
                    .filter(message -> Objects.equals(message.getTalkerId(), dto.getTalkerId()))
                    .map(message -> {
                            message.setSent(true);
                            return message;
                        })
                    .map(chattingMessageMapper::toDTO)
                    .collect(Collectors.toList());                                   
    }

    @Transactional
    public List<ChattingMessageDTO> fetchMessagesFrom(String chattingRoomId, int skip) {
        ChattingRoom chattingRoom = findChattingRoomById(chattingRoomId);
        checkValidTalker(chattingRoom);

        return chattingRoom.getMessages()
                    .stream()
                    .skip(skip)
                    .map(message -> {
                            message.setSent(true);
                            return message;
                        })
                    .map(chattingMessageMapper::toDTO)
                    .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<String> getCustomerIdList(String itemId) {
        String sellerId = SecurityUtils.getCurrentUserLogin();
        log.debug("ChattingRoomService.getCustomerIdList(), sellerId => {}", sellerId);
        return chattingRoomRepository
                    .findBySellerIdAndItemId(sellerId, itemId)
                    .stream()
                    .map(ChattingRoom::getCustomerId)
                    .collect(Collectors.toList());                             
    }

    private ChattingRoom findChattingRoom(ChattingRoomDTO dto) {
        return chattingRoomRepository
                    .findByItemIdAndSellerIdAndCustomerId(dto.getItemId(), dto.getSellerId(), dto.getCustomerId())
                    .orElseThrow(() -> ChattingException.builder()
                                            .messageKey("chattingroomservice.exception.findchattingroom.chattingroom.notfound.message")
                                            .messageArgs(new String[]{ dto.getItemId(), dto.getSellerId(), dto.getCustomerId() })
                                            .build());
    }

    private ChattingRoom findChattingRoomById(String id) {
        return chattingRoomRepository
                    .findById(id)
                    .orElseThrow(() -> ChattingException.builder()
                                            .messageKey("chattingroomservice.exception.sendchattingmessage.chattingroom.notfound.message")
                                            .messageArgs(new String[]{id})
                                            .build());
    }

    private void checkValidTalker(ChattingRoom chattingRoom) {
        String userId = SecurityUtils.getCurrentUserLogin();
        log.debug("ChattingRoomService.checkValidTalker(), userId => {}", userId);
        if (!Objects.equals(userId, chattingRoom.getSellerId()) 
            && !Objects.equals(userId, chattingRoom.getCustomerId()))
            throw ChattingException.builder()
                    .messageKey("chattingroomservice.exception.findchattingroom.chattingroom.invalidchattinguser.message")
                    .messageArgs(new String[]{})
                    .build();
    }

    private void changeMessageSentStatus(ChattingRoom chattingRoom) {
        chattingRoom.getMessages()
            .stream()
            .filter(ChattingMessage::isNotSent)
            .forEach(message -> message.setSent(true));  
    }

    private void checkAlreadyCreated(ChattingRoomDTO dto) {
        boolean hasAlreadyCreated = 
            chattingRoomRepository
                .existsByItemIdAndSellerIdAndCustomerId(dto.getItemId(), dto.getSellerId(), dto.getCustomerId());

        if (hasAlreadyCreated)
            throw ChattingException.builder()
                    .messageKey("chattingroomservice.exception.createchattingroom.chattingroom.hasalreadycreated.message")
                    .messageArgs(new String[]{})
                    .build();
    }*/
}
