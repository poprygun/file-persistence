package io.pivotal.pal.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class FileContent {
    private String content;
    private String filePath;
}
