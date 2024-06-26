package com.katyshev.webZakat.utils;


import com.katyshev.webZakat.exceptions.FileNotFoundException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Log
@Component
public class MyFileManager {
    @Value("${io_Directory}")
    private String mainPath;

    public static List<File> getFileList(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        return Arrays.asList(files);
    }

    public String getUnikoQueryPath() {
        return getUnikoFileFromDirectory(mainPath + "\\query");
    }

    public String getPriceDirectory() {
        return mainPath + "\\price";
    }

    private String getUnikoFileFromDirectory(String path) {
        File directory = new File(path);
        Optional<File> firstFile = Arrays.stream(Objects.requireNonNull(directory.listFiles())).findFirst();

        if (firstFile.isEmpty()) {
            log.warning("Uniko query file does not exist");
            throw new FileNotFoundException("no files found in the directory: " + directory);
        }

        return firstFile.get().toString();
    }

    public String getNewOutputFile() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy_HH-mm-ss");
        String path = mainPath + "\\output\\order_" + LocalDateTime.now().format(formatter) + ".dbf";

        File directory = new File(mainPath, "output");
        if (!directory.exists()) {
            directory.mkdir();
        }

        File file = new File(path);
        try {
            file.createNewFile();
        } catch (IOException e) {
            log.warning("Output file cannot be created");
            throw new RuntimeException(e);
        }

        log.info(String.format("Output file \"%s\" was created", path));
        return file.toString();
    }

    public static List<Path> getFileList(Path path) {
        List<Path> paths = new ArrayList<>();
        class MyFileVisitor extends SimpleFileVisitor<Path> {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                paths.add(file);
                return FileVisitResult.CONTINUE;
            }
        }
        try {
            Files.walkFileTree(path, new MyFileVisitor());
        } catch (IOException e) {
            log.warning(String.format("Error reading files from directory: %s", path.toString()));
        }
        return paths;
    }

    public void moveToStorage(String absoluteFilePath) {
        Path src = Path.of(absoluteFilePath);
        Path dest = Path.of(src.getParent() + "\\storage\\" + src.getFileName());

        try {
            Files.move(src, dest,  StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
