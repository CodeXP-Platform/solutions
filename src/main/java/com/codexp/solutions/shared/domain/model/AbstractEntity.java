package com.codexp.solutions.shared.domain.model;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

import java.util.Date;

@MappedSuperclass
@Getter
public class AbstractEntity {
    private Date updatedAt;
    private Date createdAt;
}
