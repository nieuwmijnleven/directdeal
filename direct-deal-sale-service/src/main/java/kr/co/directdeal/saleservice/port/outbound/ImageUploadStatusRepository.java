package kr.co.directdeal.saleservice.port.outbound;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.directdeal.saleservice.domain.object.ImageUploadStatus;

@Repository
public interface ImageUploadStatusRepository extends JpaRepository<ImageUploadStatus, String> {
    
}
