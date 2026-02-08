package com.example.classcompare.repository;

import com.example.classcompare.entity.DatasetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatasetRepository extends JpaRepository<DatasetEntity, Long> {
    
    List<DatasetEntity> findByUserIdOrderByUploadTimeDesc(String userId);
    
    List<DatasetEntity> findByFileType(String fileType);
}
