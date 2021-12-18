package io.mwguy.cdnken.daemon.controller;

import io.mwguy.cdnken.daemon.service.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/object")
public class ObjectController {
    private final StorageService storageService;

    public ObjectController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/**")
    @PreAuthorize("isFullyAuthenticated()")
    public ResponseEntity<?> saveObject(MultipartFile multipartFile, HttpServletRequest request) {
        try {
            storageService.save(getObjectName(request), multipartFile.getInputStream());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/**")
    @PreAuthorize("isFullyAuthenticated()")
    public ResponseEntity<?> deleteObject(HttpServletRequest request) {
        try {
            return storageService.delete(getObjectName(request))
                    ? ResponseEntity.status(HttpStatus.OK).build()
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    protected String getObjectName(HttpServletRequest request) {
        return request.getRequestURI().replaceFirst("/object/", "").trim();
    }
}
