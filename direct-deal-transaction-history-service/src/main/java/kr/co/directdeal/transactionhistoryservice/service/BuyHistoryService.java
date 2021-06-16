package kr.co.directdeal.transactionhistoryservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.transactionhistoryservice.domain.BuyHistory;
import kr.co.directdeal.transactionhistoryservice.exception.BuyHistoryException;
import kr.co.directdeal.transactionhistoryservice.service.dto.BuyHistoryDTO;
import kr.co.directdeal.transactionhistoryservice.service.repository.BuyHistoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BuyHistoryService {
    
    private final BuyHistoryRepository buyHistoryRepository;

    private final Mapper<BuyHistory, BuyHistoryDTO> mapper;

    @Transactional(readOnly = true)
    public List<BuyHistoryDTO> list(String userId) {
        return buyHistoryRepository.findAllByBuyerId(userId).stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
    }

    @Transactional
    public void insert(BuyHistoryDTO dto) {
        boolean checkDuplicate = 
            buyHistoryRepository
                .existsByBuyerIdAndItemIdAndCompletionTime(dto.getBuyerId(), dto.getItemId(), dto.getCompletionTime());
        if (checkDuplicate) {
            throw BuyHistoryException.builder()
                    .messageKey("buyhistoryservice.exception.insert.transaction.duplicate.message")
                    .messageArgs(new String[]{})
                    .build();           
        }

        BuyHistory newBuyHistory = mapper.toEntity(dto);
        buyHistoryRepository.save(newBuyHistory);
    }

    @Transactional
    public void delete(BuyHistoryDTO dto) {
        BuyHistory buyHistory = 
            buyHistoryRepository.findByIdAndBuyerId(dto.getId(), dto.getBuyerId())
                .orElseThrow(() -> BuyHistoryException.builder()
                                        .messageKey("buyhistoryservice.exception.delete.transaction.notfound.message")
                                        .messageArgs(new String[]{ dto.getId().toString(), dto.getBuyerId() })
                                        .build());

            buyHistoryRepository.delete(buyHistory);
    }
}
