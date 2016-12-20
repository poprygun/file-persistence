package io.pivotal.pal.services;


import io.pivotal.pal.controller.FileController;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LocalFileOperationsTests {

    private FileOperations fileOperations = new LocalFileOperations();

    @Test
    public void shouldSaveFile() throws Exception {
        String fileToSave = createFile();
        assertTrue(new File(fileToSave).exists());
        FileUtils.deleteQuietly(new File(fileToSave));
    }

    @Test
    public void shouldDeleteFile() throws Exception {
        String fileToSave = createFile();
        fileOperations.delete(fileToSave);
        assertFalse(new File(fileToSave).exists());
    }

    private String createFile() throws Exception {
        String createdFileKey = String.valueOf(System.currentTimeMillis());

        String fileToSave = FileController.pathToFileToSave(createdFileKey);
        fileOperations.create(fileToSave, "some dummy content");
        return fileToSave;
    }
}
