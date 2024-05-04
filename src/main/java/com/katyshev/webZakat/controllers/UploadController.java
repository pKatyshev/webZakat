package com.katyshev.webZakat.controllers;

import com.katyshev.webZakat.exceptions.FileNotFoundException;
import com.katyshev.webZakat.exceptions.WrongFileException;
import com.katyshev.webZakat.services.UnikoLecItemService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Locale;

@Controller
public class UploadController {

    @Value("${user_input_Directory}")
    private String path;
    private final UnikoLecItemService unikoLecItemService;

    public UploadController(UnikoLecItemService unikoLecItemService) {
        this.unikoLecItemService = unikoLecItemService;
    }


    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        String name = file.getOriginalFilename();

        if (file.isEmpty()) {
            throw new WrongFileException("No file");
        } else if (!name.toLowerCase(Locale.ROOT).endsWith(".dbf")) {
            throw new WrongFileException("Wrong file format");
        }

        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(path + "\\" + name)))){
            byte[] bytes = file.getBytes();
            bos.write(bytes);
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        unikoLecItemService.importNewUnikoQuery();

        return "redirect:/";
    }

    @ExceptionHandler
    private String handle(Model model, WrongFileException e) {
        model.addAttribute("message", e.getMessage());
        return "error";
    }
}
