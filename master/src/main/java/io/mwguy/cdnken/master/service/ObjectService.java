package io.mwguy.cdnken.master.service;

import io.mwguy.cdnken.master.entity.DaemonServer;
import io.mwguy.cdnken.master.repository.DaemonServerRepository;
import io.mwguy.cdnken.master.repository.ObjectRepository;
import io.mwguy.cdnken.shared.model.ServerInformationResponse;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;

@Service
public class ObjectService {
    private final DaemonServerRepository daemonServerRepository;
    private final ObjectRepository objectRepository;
    private final RestTemplate restTemplate;

    public ObjectService(DaemonServerRepository daemonServerRepository, ObjectRepository objectRepository, RestTemplate restTemplate) {
        this.daemonServerRepository = daemonServerRepository;
        this.objectRepository = objectRepository;
        this.restTemplate = restTemplate;
    }

    public void uploadObject(String name, MultipartFile file) throws IOException {
        var daemonServer = findAvailablePrimaryDaemonServer(file.getSize()).orElse(null);
        if (daemonServer == null) {
            throw new IllegalStateException("Service unavailable");
        }

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBearerAuth(daemonServer.getAccessToken());

        var body = new LinkedMultiValueMap<String, Object>();
        body.add("file", file.getResource());

        var requestEntity = new HttpEntity<>(body, headers);
        var url = String.format("%s/object/%s", daemonServer.getEndpoint(), name);
        var response = restTemplate.postForEntity(url, requestEntity, Void.TYPE);

        if (response.getStatusCode().isError()) {
            throw new IOException(String.format("Unable to upload object to demon server '%s'", daemonServer.getId()));
        }

        var object = new io.mwguy.cdnken.master.entity.Object();
        object.setName(name);
        object.setSize(file.getSize());
        object.setServers(Collections.singletonList(daemonServer));
        objectRepository.save(object);

        // TODO: upload to mirror servers
    }

    protected Optional<DaemonServer> findAvailablePrimaryDaemonServer(long size) throws IOException {
        var servers = daemonServerRepository.findAllEnabledPrimaryServers();
        if (servers.isEmpty()) {
            throw new IllegalStateException("No servers available");
        }

        var serversWithAvailableStorage = new LinkedList<Pair<Long, DaemonServer>>();
        for (var server : servers) {
            long totalObjectsSize = objectRepository.totalSizeForServer(server);
            long capacity = getServerCapacity(server);

            if (totalObjectsSize + size >= capacity) {
                continue;
            }

            serversWithAvailableStorage.add(Pair.of(capacity - totalObjectsSize, server));
        }

        return serversWithAvailableStorage.stream()
                .min(this::compareServers)
                .map(Pair::getSecond);
    }

    protected long getServerCapacity(DaemonServer daemonServer) throws IOException {
        var headers = new HttpHeaders();
        headers.setBearerAuth(daemonServer.getAccessToken());

        var requestEntity = new HttpEntity<>(null, headers);
        var user = String.format("%s/server/info", daemonServer.getEndpoint());
        var response = restTemplate.exchange(user, HttpMethod.GET, requestEntity, ServerInformationResponse.class);
        if (response.getStatusCode().isError()) {
            throw new IOException("Unable to get server capacity");
        }

        return Objects.requireNonNull(response.getBody()).getCapacity();
    }

    protected int compareServers(Pair<Long, DaemonServer> one, Pair<Long, DaemonServer> two) {
        return one.getFirst().compareTo(two.getFirst());
    }
}
