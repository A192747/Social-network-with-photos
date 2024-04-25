package ru.micro.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class PhotoUploadResponse {
    private String photoName;
    private Date timestamp;
}
