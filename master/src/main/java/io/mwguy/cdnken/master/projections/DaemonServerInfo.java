package io.mwguy.cdnken.master.projections;

import java.util.Date;
import java.util.UUID;

public interface DaemonServerInfo {
    UUID getId();
    Date getCreatedAt();
    String getEndpoint();
    boolean isEnabled();
    boolean isMirror();
}
