package com.api.nextspring.util;

import com.api.nextspring.dto.DeveloperDto;
import com.api.nextspring.entity.DeveloperEntity;
import com.github.javafaker.Faker;

public class ObjectCreationUtils {

    private final static Faker faker = new Faker();

    public static DeveloperEntity getDeveloperEntity() {
        return DeveloperEntity
                .builder()
                .name(faker.company().name())
                .description(faker.lorem().paragraph())
                .build();
    }

    public static DeveloperDto getDeveloperDto() {
        return DeveloperDto
                .builder()
                .name(faker.company().name())
                .description(faker.lorem().paragraph())
                .build();
    }

}
