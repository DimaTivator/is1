package org.dimativator.is1.rest;

import org.dimativator.is1.dto.ImportHistoryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import java.util.List;

public interface ImportHistoryApi {
    @GetMapping("/import-history")
    ResponseEntity<List<ImportHistoryDto>> getImportHistory(@RequestHeader(name = "Authorization") String token);

    @GetMapping("/import-history/all")
    ResponseEntity<List<ImportHistoryDto>> getAllImportHistory(@RequestHeader(name = "Authorization") String token);

    @GetMapping("/import-history/download/{id}")
    ResponseEntity<Resource> downloadImportFile(@PathVariable Long id, 
                                            @RequestHeader(name = "Authorization") String token);
} 