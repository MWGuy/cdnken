package io.mwguy.cdnken.daemon.service;

import io.mwguy.cdnken.daemon.component.StoragePathResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<?> upload(String name, InputStream inputStream) throws IOException {
        var path = pathResolver.resolve(name).toAbsolutePath();
        if (Files.exists(path)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Files.createDirectories(path.getParent());
        Files.write(path, inputStream.readAllBytes());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public ResponseEntity<?> delete(String name) throws IOException {
        return Files.deleteIfExists(pathResolver.resolve(name))
                ? ResponseEntity.status(HttpStatus.OK).build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    public ResponseEntity<?> move(String name, String destination) throws IOException {
        if (destination.startsWith("/")) {
            destination = destination.substring(1);
        }

        var destinationPath = pathResolver.resolve(destination).toAbsolutePath();
        var sourcePath = pathResolver.resolve(name).toAbsolutePath();

        if (destinationPath.equals(sourcePath)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (!Files.exists(sourcePath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (Files.exists(destinationPath)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Files.createDirectories(destinationPath.getParent());
        Files.move(sourcePath, destinationPath);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
