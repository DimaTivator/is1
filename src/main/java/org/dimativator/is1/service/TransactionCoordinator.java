package org.dimativator.is1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionCoordinator {
    private final MinioService minioService;

    public <T> TransactionResult<T> executeTransaction(MultipartFile file, TransactionOperation<T> operation) {
        String minioFilename = null;
        T result = null;
        List<String> preparedParticipants = new ArrayList<>();

        try {
            log.info("Starting prepare phase");
            
            minioFilename = minioService.prepareUpload(file);
            preparedParticipants.add("minio");
            
            result = operation.prepare();
            preparedParticipants.add("database");

            log.info("Starting commit phase");
            
            minioService.commitUpload(minioFilename);
            operation.commit();

            return new TransactionResult<>(true, result, minioFilename);

        } catch (Exception e) {
            log.error("Transaction failed, starting rollback", e);
            
            // Rollback all prepared operations
            if (preparedParticipants.contains("minio") && minioFilename != null) {
                try {
                    minioService.rollbackUpload(minioFilename);
                } catch (Exception rollbackEx) {
                    log.error("Failed to rollback MinIO upload", rollbackEx);
                }
            }
            
            if (preparedParticipants.contains("database")) {
                try {
                    operation.rollback();
                } catch (Exception rollbackEx) {
                    log.error("Failed to rollback database operation", rollbackEx);
                }
            }
            throw new RuntimeException(e.getMessage());
        }
    }
}

@lombok.Value
class TransactionResult<T> {
    boolean success;
    T result;
    String minioFilename;
}

interface TransactionOperation<T> {
    T prepare() throws Exception;
    void commit() throws Exception;
    void rollback() throws Exception;
} 