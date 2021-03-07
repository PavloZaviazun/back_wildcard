package com.wildcard.back.models;

import lombok.*;

import java.util.ArrayList;

@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Translation {
    private ArrayList<String> ru;
    private ArrayList<String> ua;

}
