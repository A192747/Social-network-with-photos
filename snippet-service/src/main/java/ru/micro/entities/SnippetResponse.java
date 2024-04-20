package ru.micro.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SnippetResponse {
    private String faviconPath;
    private String title;
    private String textPreview;
    private String link;
}
