package io.mwguy.cdnken.master.controller;

import io.mwguy.cdnken.master.entity.Object;
import io.mwguy.cdnken.master.repository.DaemonServerRepository;
import io.mwguy.cdnken.master.repository.ObjectRepository;
import io.mwguy.cdnken.master.service.ObjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping(value = "/object")
public class ObjectController {
    private final DaemonServerRepository daemonServerRepository;
    private final ObjectRepository objectRepository;
    private final ObjectService objectService;

    public ObjectController(DaemonServerRepository daemonServerRepository, ObjectRepository objectRepository, ObjectService objectService) {
        this.daemonServerRepository = daemonServerRepository;
        this.objectRepository = objectRepository;
        this.objectService = objectService;
    }

    @GetMapping("/**")
    public ResponseEntity<?> resolveObject(HttpServletRequest request) {
        return objectRepository.findByName(getObjectName(request))
                .map(this::makeRedirectEntity)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/**")
    public ResponseEntity<?> uploadObject(@RequestBody MultipartFile file, HttpServletRequest request) {
        try {
            objectService.uploadObject(getObjectName(request), file);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IOException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    protected String getObjectName(HttpServletRequest request) {
        return request.getRequestURI().replaceFirst("/object/", "").trim();
    }

    protected ResponseEntity<?> makeRedirectEntity(Object object) {
        var servers = daemonServerRepository.findAllByObject(object);
        if (servers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }

        var headers = new HttpHeaders();
        headers.add("Location", servers.get(0).computeUrl(object)); // TODO: make server choosing
        return new ResponseEntity<String>(headers, HttpStatus.FOUND);
    }
}
