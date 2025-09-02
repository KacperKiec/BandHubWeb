package io.kk.bandhub.imageservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String save(MultipartFile file, Integer uid);
}
