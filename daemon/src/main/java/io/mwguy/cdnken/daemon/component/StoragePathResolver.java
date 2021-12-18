package io.mwguy.cdnken.daemon.component;

import java.io.IOException;
import java.nio.file.Path;

public interface StoragePathResolver {
    Path resolve(String name) throws IOException;
}
