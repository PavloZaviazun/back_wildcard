package com.wildcard.back.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class LibWord implements Serializable {

    @Id
    private int libId;
    @Id
    private int wordId;
}
