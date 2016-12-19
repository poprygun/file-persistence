package io.pivotal.pal.controller;

import io.pivotal.pal.model.FileContent;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.charset.Charset;

@Controller
public class FileController {
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
        FileUtils.writeStringToFile(new File(pathToFileToSave()), fileContent.getContent(), Charset.defaultCharset());

        fileContent.setFilePath(pathToFileToSave());
        model.addAttribute("fileData", fileContent);
        return "savedfile";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam String filePath, Model model) throws Exception{
        FileUtils.deleteQuietly(new File(filePath));
        return index(model);
    }

    static String pathToFileToSave(){
        String tempDir = System.getProperty("java.io.tmpdir");
        String pathSeparator = System.getProperty("java.io.tmpdir");
        return tempDir.concat(pathSeparator).concat(String.valueOf(System.currentTimeMillis()));
    }
}
