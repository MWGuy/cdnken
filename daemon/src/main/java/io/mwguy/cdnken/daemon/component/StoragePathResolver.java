package io.mwguy.cdnken.daemon.component;

import java.nio.file.Path;

public interface StoragePathResolver {
    Path resolve(String name);
}
