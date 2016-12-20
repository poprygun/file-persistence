package io.pivotal.pal.controller;

import io.pivotal.pal.model.FileContent;
import io.pivotal.pal.services.FileOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@Controller
public class FileController {

    @Autowired
    private FileOperations fileOperations;

    @RequestMapping("/hithere")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "hithere";
    }

    @GetMapping("/")
    public String fileData(Model model) {
        return index(model);
    }

    private String index(Model model) {
        FileContent fileContent = new FileContent();
        model.addAttribute("fileData", fileContent);
        return "filedata";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute FileContent fileContent, Model model) throws Exception{
        String createdFileKey = String.valueOf(System.currentTimeMillis());

        fileOperations.create(pathToFileToSave(createdFileKey), fileContent.getContent());

        fileContent.setFilePath(fileOperations.listCreatedFile(createdFileKey));
        model.addAttribute("fileData", fileContent);
        return "savedfile";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam String filePath, Model model) throws Exception{
        fileOperations.delete(filePath);
        return index(model);
    }

    public static String pathToFileToSave(String key){
        String tempDir = System.getProperty("java.io.tmpdir");
        if (!tempDir.endsWith("/")) tempDir = tempDir.concat(File.separator);
        return tempDir.concat(key);
    }
}
