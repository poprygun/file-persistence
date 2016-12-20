package io.pivotal.pal.controller;

import io.pivotal.pal.services.FileOperations;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;

import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(FileController.class)
@ActiveProfiles(profiles = "local")
public class FileControllerTests {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @MockBean
    private FileOperations fileOperations;

    private String createdFileKey;


    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        createdFileKey = String.valueOf(System.currentTimeMillis());

        when(fileOperations.listCreatedFile(anyString()))
                .thenReturn("/my/path/to/file");
    }

    @After
    public void cleanup() {
        FileUtils.deleteQuietly(new File(FileController.pathToFileToSave(createdFileKey)));
    }

    @Test
    public void shouldRenderEntryForm() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("fileData", hasProperty("content", nullValue())))
                .andExpect(view().name("filedata"));
    }

    @Test
    public void shouldSaveToAFile() throws Exception {

        this.mockMvc.perform(post("/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("content", "some generated file content")).andExpect(status().isOk())
                .andExpect(model().attribute("fileData", hasProperty("filePath", notNullValue())))
                .andExpect(model().attribute("fileData", hasProperty("content", notNullValue())))
                .andExpect(view().name("savedfile"));
    }
}
