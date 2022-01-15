package io.mwguy.cdnken.daemon.controller;

import io.mwguy.cdnken.daemon.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/object")
public class ObjectController {
    private final StorageService storageService;

    public ObjectController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/**")
    @PreAuthorize("isFullyAuthenticated()")
    public ResponseEntity<?> uploadOrMoveObject(
            @RequestBody(required = false) MultipartFile file,
            @RequestParam(required = false, name = "destination") String destination,
            HttpServletRequest request
    ) throws IOException {
        if ((file == null && destination == null) || (file != null && destination != null)) {
            return ResponseEntity.badRequest().build();
        }

        var objectName = getObjectName(request);
        if (file != null) {
            return storageService.upload(objectName, file.getInputStream());
        } else {
            return storageService.move(objectName, destination);
        }
    }

    @DeleteMapping("/**")
    @PreAuthorize("isFullyAuthenticated()")
    public ResponseEntity<?> deleteObject(HttpServletRequest request) throws IOException {
        return storageService.delete(getObjectName(request));
    }

    protected String getObjectName(HttpServletRequest request) {
        return request.getRequestURI().replaceFirst("/object/", "").trim();
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleIOException(IOException exception) {
        log.warn("Handled IO exception with message: {}", exception.getMessage(), exception);
        return ResponseEntity.internalServerError().build();
    }
}
