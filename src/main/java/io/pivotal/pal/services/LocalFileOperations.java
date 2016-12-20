package io.pivotal.pal.services;


import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.Charset;

@Service
@Profile("local")
public class LocalFileOperations implements FileOperations{
    @Override
    public void create(String filePath, String content) throws Exception {
        FileUtils.writeStringToFile(new File(filePath), content, Charset.defaultCharset());
    }

    @Override
    public void delete(String filePath) {
        FileUtils.deleteQuietly(new File(filePath));
    }

    @Override
    public String listCreatedFile(String key) {
        String tempDir = System.getProperty("java.io.tmpdir");
        return tempDir.concat(key);
    }
}
