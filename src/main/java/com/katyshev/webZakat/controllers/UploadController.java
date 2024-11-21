package com.katyshev.webZakat.controllers;

import com.katyshev.webZakat.exceptions.WrongFileException;
import com.katyshev.webZakat.services.UnikoLecItemService;
import com.katyshev.webZakat.utils.FileManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadController {

    private final UnikoLecItemService unikoLecItemService;
    private final FileManager fileManager;

    public UploadController(UnikoLecItemService unikoLecItemService, FileManager fileManager) {
        this.unikoLecItemService = unikoLecItemService;
        this.fileManager = fileManager;
    }


    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        fileManager.writeUserQueryFile(file);
        unikoLecItemService.importNewUnikoQuery();
        return "redirect:/";
    }

    @ExceptionHandler
    private String handle(Model model, WrongFileException e) {
        model.addAttribute("message", e.getMessage());
        return "error";
    }
}
