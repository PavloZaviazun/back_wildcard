package com.wildcard.back.models;

import lombok.*;

import javax.persistence.Id;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class WordLib implements Serializable {
    @Id
    private int wordId;
    @Id
    private int libId;
}
