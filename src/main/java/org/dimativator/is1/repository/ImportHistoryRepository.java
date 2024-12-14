package org.dimativator.is1.repository;

import org.dimativator.is1.model.ImportHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ImportHistoryRepository extends JpaRepository<ImportHistory, Long> {
    List<ImportHistory> findByUsername(String username);
} 