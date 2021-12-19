package io.mwguy.cdnken.shared.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerInformationResponse {
    private String template;
    private long capacity;
    private boolean mirror;
}
