package io.mwguy.cdnken.daemon.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServerInformationResponse {
    private String template;
    private long capacity;
    private boolean mirror;
}
