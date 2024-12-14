package org.dimativator.is1.service;

import lombok.RequiredArgsConstructor;
import org.dimativator.is1.dto.ImportHistoryDto;
import org.dimativator.is1.model.ImportHistory;
import org.dimativator.is1.repository.ImportHistoryRepository;
import org.dimativator.is1.model.Role;
import org.dimativator.is1.model.User;
import org.springframework.stereotype.Service;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ImportHistoryService {
    
    private final ImportHistoryRepository importHistoryRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordImport(String username, String filename, int rowCount, boolean success) {
        ImportHistory history = new ImportHistory();
        history.setUsername(username);
        history.setFilename(filename);
        history.setRowCount(rowCount);
        history.setSuccess(success);
        history.setImportDate(ZonedDateTime.now());
        importHistoryRepository.save(history);
    }

    public List<ImportHistoryDto> getUserHistory(User user) {
        if (user.getRole() == Role.ADMIN) {
            return importHistoryRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        }
        return importHistoryRepository.findByUsername(user.getLogin())
            .stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    public List<ImportHistoryDto> getAllHistory() {
        return importHistoryRepository.findAll()
            .stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    private ImportHistoryDto toDto(ImportHistory history) {
        ImportHistoryDto dto = new ImportHistoryDto();
        dto.setId(history.getId());
        dto.setUsername(history.getUsername());
        dto.setFilename(history.getFilename());
        dto.setRowCount(history.getRowCount());
        dto.setSuccess(history.getSuccess());
        dto.setImportDate(history.getImportDate());
        return dto;
    }
} 