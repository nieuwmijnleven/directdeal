package kr.co.directdeal.saleservice.service.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemImageDTO {
    private String checkId;
    private String checkURL;
    private List<String> images = new ArrayList<>();
}
