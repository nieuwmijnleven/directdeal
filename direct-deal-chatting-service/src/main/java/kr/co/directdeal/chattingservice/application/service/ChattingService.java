package kr.co.directdeal.chattingservice.application.service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import kr.co.directdeal.chattingservice.port.inbound.ChattingUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.directdeal.chattingservice.domain.object.ChattingMessage;
import kr.co.directdeal.chattingservice.domain.object.ChattingRoom;
import kr.co.directdeal.chattingservice.exception.ChattingException;
import kr.co.directdeal.chattingservice.exception.ChattingRoomAlreadyCreatedException;
import kr.co.directdeal.chattingservice.application.service.dto.ChattingMessageDTO;
import kr.co.directdeal.chattingservice.application.service.dto.ChattingRoomDTO;
import kr.co.directdeal.chattingservice.port.outbound.ChattingRepository;
import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.common.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class that implements chatting functionalities such as managing
 * chatting rooms and messages.
 *
 * This service handles creating rooms, sending messages, fetching messages,
 * and validating users in chatting rooms.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChattingService implements ChattingUseCase {

    private final ChattingRepository chattingRepository;
    private final Mapper<ChattingRoom, ChattingRoomDTO> chattingRoomMapper;
    private final Mapper<ChattingMessage, ChattingMessageDTO> chattingMessageMapper;

    /**
     * Retrieves a list of chatting rooms for the specified user.
     *
     * @param userId the ID of the user (seller or customer)
     * @return list of ChattingRoomDTO associated with the user
     */
    @Transactional(readOnly = true)
    @Override
    public List<ChattingRoomDTO> getChattingRoomList(String userId) {
        return chattingRepository.findBySellerIdOrCustomerId(userId, userId)
                .stream()
                .map(chattingRoomMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a chatting room by its identifying details.
     * Marks all unread messages in the room as sent.
     *
     * @param dto ChattingRoomDTO containing identifying details (itemId, sellerId, customerId)
     * @return the corresponding ChattingRoomDTO with messages updated
     */
    @Transactional
    @Override
    public ChattingRoomDTO getChattingRoom(ChattingRoomDTO dto) {
        ChattingRoom chattingRoom = findChattingRoom(dto);
        checkValidTalker(chattingRoom);
        changeMessageSentStatus(chattingRoom);
        return chattingRoomMapper.toDTO(chattingRoom);
    }

    /**
     * Retrieves a chatting room by its ID.
     * Marks all unread messages in the room as sent.
     *
     * @param id the chatting room ID
     * @return the corresponding ChattingRoomDTO with messages updated
     */
    @Transactional
    @Override
    public ChattingRoomDTO getChattingRoom(Long id) {
        ChattingRoom chattingRoom = findChattingRoomById(id);
        checkValidTalker(chattingRoom);
        changeMessageSentStatus(chattingRoom);
        return chattingRoomMapper.toDTO(chattingRoom);
    }

    /**
     * Creates a new chatting room if it doesn't already exist.
     * Validates that the current user is a valid participant (seller or customer).
     *
     * @param dto the ChattingRoomDTO with new room details
     * @return the created ChattingRoomDTO
     * @throws ChattingRoomAlreadyCreatedException if the room already exists
     * @throws ChattingException if the current user is not a valid talker
     */
    @Transactional
    @Override
    public ChattingRoomDTO createChattingRoom(ChattingRoomDTO dto) {
        checkAlreadyCreated(dto);

        ChattingRoom newChattingRoom = chattingRoomMapper.toEntity(dto);
        checkValidTalker(newChattingRoom);

        newChattingRoom.setCreatedDate(Instant.now());
        newChattingRoom = chattingRepository.save(newChattingRoom);
        return chattingRoomMapper.toDTO(newChattingRoom);
    }

    /**
     * Sends a new chatting message within a chatting room.
     * Sets the message's creation timestamp and adds it to the room.
     *
     * @param dto the ChattingMessageDTO containing message details
     * @throws ChattingException if the current user is not a valid talker or room not found
     */
    @Transactional
    @Override
    public void sendMessage(ChattingMessageDTO dto) {
        ChattingRoom chattingRoom = findChattingRoomById(dto.getChattingRoomId());
        checkValidTalker(chattingRoom);

        dto.setCreatedDate(Instant.now());
        chattingRoom.addMessage(chattingMessageMapper.toEntity(dto));
    }

    /**
     * Fetches unread messages for the specified talker in a chatting room,
     * marks them as sent, and returns them.
     *
     * @param dto the ChattingMessageDTO containing room ID and talker ID
     * @return list of unread ChattingMessageDTOs for the talker
     */
    @Transactional
    @Override
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

    /**
     * Fetches messages from a chatting room starting after a certain number of skipped messages,
     * marks them as sent, and returns them.
     *
     * @param chattingRoomId the ID of the chatting room
     * @param skip the number of messages to skip (for pagination)
     * @return list of ChattingMessageDTO starting from the skipped offset
     */
    @Transactional
    @Override
    public List<ChattingMessageDTO> fetchMessagesFrom(Long chattingRoomId, int skip) {
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

    /**
     * Returns a list of customer IDs who have chatting rooms with the currently logged-in seller for a given item.
     *
     * @param itemId the ID of the item
     * @return list of customer IDs
     */
    @Transactional(readOnly = true)
    @Override
    public List<String> getCustomerIdList(String itemId) {
        String sellerId = SecurityUtils.getCurrentUserLogin();
        log.debug("ChattingRoomService.getCustomerIdList(), sellerId => {}", sellerId);
        return chattingRepository
                .findBySellerIdAndItemId(sellerId, itemId)
                .stream()
                .map(ChattingRoom::getCustomerId)
                .collect(Collectors.toList());
    }

    // --- Private helper methods ---

    private ChattingRoom findChattingRoom(ChattingRoomDTO dto) {
        return chattingRepository
                .findByItemIdAndSellerIdAndCustomerId(dto.getItemId(), dto.getSellerId(), dto.getCustomerId())
                .orElseThrow(() -> ChattingException.builder()
                        .messageKey("chattingroomservice.exception.findchattingroom.chattingroom.notfound.message")
                        .messageArgs(new String[]{ dto.getItemId(), dto.getSellerId(), dto.getCustomerId() })
                        .build());
    }

    private ChattingRoom findChattingRoomById(Long id) {
        return chattingRepository
                .findById(id)
                .orElseThrow(() -> ChattingException.builder()
                        .messageKey("chattingroomservice.exception.sendchattingmessage.chattingroom.notfound.message")
                        .messageArgs(new String[]{id.toString()})
                        .build());
    }

    private void checkValidTalker(ChattingRoom chattingRoom) {
        String userId = SecurityUtils.getCurrentUserLogin();
        log.debug("ChattingRoomService.checkValidTalker(), userId => {}", userId);
        if (!Objects.equals(userId, chattingRoom.getSellerId())
                && !Objects.equals(userId, chattingRoom.getCustomerId())) {
            throw ChattingException.builder()
                    .messageKey("chattingroomservice.exception.findchattingroom.chattingroom.invalidchattinguser.message")
                    .messageArgs(new String[]{})
                    .build();
        }
    }

    private void changeMessageSentStatus(ChattingRoom chattingRoom) {
        chattingRoom.getMessages()
                .stream()
                .filter(ChattingMessage::isNotSent)
                .forEach(message -> message.setSent(true));
    }

    private void checkAlreadyCreated(ChattingRoomDTO dto) {
        boolean hasAlreadyCreated = chattingRepository
                .existsByItemIdAndSellerIdAndCustomerId(dto.getItemId(), dto.getSellerId(), dto.getCustomerId());

        if (hasAlreadyCreated) {
            throw ChattingRoomAlreadyCreatedException.builder()
                    .messageKey("chattingroomservice.exception.createchattingroom.chattingroom.hasalreadycreated.message")
                    .messageArgs(new String[]{})
                    .build();
        }
    }
}
