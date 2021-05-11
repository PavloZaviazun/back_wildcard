package com.wildcard.back.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Translation {
    private String translationRu;
    private String translationUa;

    @Override
    public String toString() {
        return "{\"ru\": \"" + translationRu + "\", \"ua\": \"" + translationUa + "\"}";
    }
}
