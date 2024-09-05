package com.vubq.joyboystore.services.impl;

import com.cloudinary.Cloudinary;
import com.vubq.joyboystore.services.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public Map upload(MultipartFile file) {
        try {
            Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            return data;
        } catch (IOException io) {
            throw new RuntimeException("Image upload fail");
        }
    }

    @Override
    public Map deleteByPublicId(String id) {
        try {
            Map data = this.cloudinary.uploader().destroy(id, Map.of());
            return data;
        } catch (Exception io) {
            throw new RuntimeException("Delete fail");
        }
    }
}

