package com.cyan.deliverytracking.domain.model;

import lombok.*;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class ContactPoint {
    private String zipCode;
    private String complement;
    private String street;
    private String number;
    private String name;
    private String phone;
}
