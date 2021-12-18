package io.mwguy.cdnken.master.controller;

import io.mwguy.cdnken.master.projections.DaemonServerInfo;
import io.mwguy.cdnken.master.repository.DaemonServerRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "/daemon")
public class DaemonServerController {
    private final DaemonServerRepository daemonServerRepository;

    public DaemonServerController(DaemonServerRepository daemonServerRepository) {
        this.daemonServerRepository = daemonServerRepository;
    }

    @GetMapping
    public List<DaemonServerInfo> serverInfoList() {
        return daemonServerRepository.findAllProjections();
    }

    @GetMapping("/{id}")
    public Optional<DaemonServerInfo> serverInfo(@PathVariable UUID id) {
        return daemonServerRepository.findProjectionById(id);
    }
}
