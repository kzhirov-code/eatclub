package com.kzhirov.eatclub.availability.ds;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import com.kzhirov.eatclub.availability.model.dto.RestaurantsInfoDto;
import tools.jackson.databind.ObjectMapper;

@Repository
public class RestaurantsFileBaseDS implements RestaurantsDS {

    private static final String DATA_FILE_PATH = "challengedata.json";
    private final RestaurantsInfoDto restaurantsInfo;   

    public RestaurantsFileBaseDS(ObjectMapper objectMapper) {
        try (InputStream inputStream = new ClassPathResource(DATA_FILE_PATH).getInputStream()) {
            this.restaurantsInfo = objectMapper.readValue(inputStream, RestaurantsInfoDto.class);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to load " + DATA_FILE_PATH, exception);
        }
    }


    @Override
    public RestaurantsInfoDto getRestaurants() {
        return restaurantsInfo;
    }
    
}
