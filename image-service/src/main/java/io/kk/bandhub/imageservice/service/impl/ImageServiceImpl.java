package io.kk.bandhub.imageservice.service.impl;

import io.kk.bandhub.imageservice.service.CloudinaryService;
import io.kk.bandhub.imageservice.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final CloudinaryService cloudinaryService;

    public String save(MultipartFile file, Integer uid, String oldURL) {
        try {
            if (oldURL != null && !oldURL.isEmpty()) {
                var publicId = getResourceCloudinaryId(oldURL);
                cloudinaryService.deleteFile(publicId);
            }

            var filename = uid.toString() + "_" + LocalDateTime.now();

            return cloudinaryService.uploadFile(file, filename);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot save image", e);
        }
    }

    private String getResourceCloudinaryId(String url) {
        var temp = url.split("/");
        return temp[temp.length - 2] + "/" + temp[temp.length - 1];
    }
}
