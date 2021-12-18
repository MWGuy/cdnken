package io.mwguy.cdnken.daemon.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadObjectRequestBody {
    @NonNull
    private String name;

    @NonNull
    private MultipartFile file;
}
