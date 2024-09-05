package com.vubq.joyboystore.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface CloudinaryService {

    Map upload(MultipartFile file);

    Map deleteByPublicId(String id);
}
