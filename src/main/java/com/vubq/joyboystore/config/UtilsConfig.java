package com.vubq.joyboystore.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class UtilsConfig {

    @Bean
    public Cloudinary getCloudinary() {
        Map config = new HashMap();
        config.put("cloud_name", "vubq");
        config.put("api_key", "213741985827213");
        config.put("api_secret", "b7kMRq121vRX8ZXmSVFFTuVUnZA");
        config.put("secure", true);
        return new Cloudinary(config);
    }
}
