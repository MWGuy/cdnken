package io.mwguy.cdnken.daemon.service;

import io.mwguy.cdnken.daemon.component.StoragePathResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@Service
public class StorageService {
    private final StoragePathResolver pathResolver;

    public StorageService(StoragePathResolver pathResolver) {
        this.pathResolver = pathResolver;
    }

    public void save(String name, InputStream inputStream) throws IOException {
        var path = pathResolver.resolve(name).toAbsolutePath();
        if (Files.exists(path)) {
            throw new IOException("Object already exists");
        }

        Files.createDirectories(path.getParent());
        Files.write(path, inputStream.readAllBytes());
    }

    public boolean delete(String name) throws IOException {
        return Files.deleteIfExists(pathResolver.resolve(name));
    }
}
