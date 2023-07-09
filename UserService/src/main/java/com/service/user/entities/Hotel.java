package com.service.user.entities;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Hotel {
    private String id;
    private String name;
    private String location;
    private String about;
}
