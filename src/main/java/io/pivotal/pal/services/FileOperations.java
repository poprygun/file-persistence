package io.pivotal.pal.services;


public interface FileOperations {
    void create(String filePath, String content) throws Exception;
    void delete(String filePath);
    String listCreatedFile(String key);
}
