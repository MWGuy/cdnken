package io.mwguy.cdnken.master.controller;

import io.mwguy.cdnken.master.entity.Object;
import io.mwguy.cdnken.master.repository.DaemonServerRepository;
import io.mwguy.cdnken.master.repository.ObjectRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/object")
public class ObjectController {
    private final DaemonServerRepository daemonServerRepository;
    private final ObjectRepository objectRepository;

    public ObjectController(DaemonServerRepository daemonServerRepository, ObjectRepository objectRepository) {
        this.daemonServerRepository = daemonServerRepository;
        this.objectRepository = objectRepository;
    }

    @GetMapping
    public ResponseEntity<?> resolveObject(@RequestParam(name = "name", required = true) String name) {
        return objectRepository.findByName(name)
                .map(this::makeRedirectEntity)
                .orElse(ResponseEntity.notFound().build());
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
