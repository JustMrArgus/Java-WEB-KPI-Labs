package com.cosmocats.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    private Long id;
    private String sessionId;
    private List<Long> productIds;
}
