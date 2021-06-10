package kr.co.directdeal.saleservice.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.directdeal.saleservice.domain.ImageUploadStatus;

@Repository
public interface ImageUploadStatusRepository extends JpaRepository<ImageUploadStatus, String> {
    
}
