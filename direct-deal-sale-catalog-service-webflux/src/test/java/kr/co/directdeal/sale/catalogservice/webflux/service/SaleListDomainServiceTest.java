package kr.co.directdeal.sale.catalogservice.webflux.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.co.directdeal.sale.catalogservice.webflux.config.prop.LiftUpProperties;
import kr.co.directdeal.sale.catalogservice.webflux.domain.SaleList;
import kr.co.directdeal.sale.catalogservice.webflux.exception.SaleListException;

@ExtendWith(MockitoExtension.class)
public class SaleListDomainServiceTest {

    private SaleListDomainService saleListDomainService;

    private LiftUpProperties liftUpProperties = new LiftUpProperties();

    @BeforeEach
    public void init() {
        this.saleListDomainService = new SaleListDomainService(liftUpProperties);
    }
    
    @Test
    public void CanLiftUp_ValidCreatedDate_ReturnTrue() {
        //given
        String id = "1";
        Instant createdDate = Instant.now();
        SaleList saleList = SaleList.builder()
                                        .id(id)
                                        .createdDate(createdDate.minus(3, ChronoUnit.DAYS))
                                        .build();    

        liftUpProperties.setLiftUpIntervalDays(3);

        //when
        boolean result = saleListDomainService.canLiftUp(saleList);
        
        //then
        assertThat(result, equalTo(true));
    }

    @Test
    public void CanLiftUp_InvalidCreatedDate_ReturnFalse() {
        //given
        String id = "1";
        Instant createdDate = Instant.now();
        SaleList saleList = SaleList.builder()
                                        .id(id)
                                        .createdDate(createdDate.minus(2, ChronoUnit.DAYS))
                                        .build();    
        
        liftUpProperties.setLiftUpIntervalDays(3);

        //when
        boolean result = saleListDomainService.canLiftUp(saleList);
        
        //then
        assertThat(result, equalTo(false));
    }

    @Test
    public void LiftUp_ValidCreatedDate_Success() {
        //given
        String id = "1";
        Instant createdDate = Instant.now();
        SaleList saleList = SaleList.builder()
                                        .id(id)
                                        .createdDate(createdDate.minus(7, ChronoUnit.DAYS))
                                        .build();    
        saleList = spy(saleList);

        liftUpProperties.setLiftUpIntervalDays(3);

        //when
        saleListDomainService.liftUp(saleList);
        
        //then
        verify(saleList).setCreatedDate(any(Instant.class));
    }

    @Test
    public void LiftUp_InvalidCreatedDate_ThrowSaleListException() {
        //given
        String id = "1";
        Instant createdDate = Instant.now();
        SaleList saleList = SaleList.builder()
                                        .id(id)
                                        .createdDate(createdDate.minus(1, ChronoUnit.DAYS))
                                        .build();    

        liftUpProperties.setLiftUpIntervalDays(3);

        //when and then
        assertThrows(SaleListException.class, () -> {
            saleListDomainService.liftUp(saleList);
        });
    }
}
