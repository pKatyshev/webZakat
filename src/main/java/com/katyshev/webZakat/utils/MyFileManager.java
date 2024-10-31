package com.katyshev.webZakat.utils;


import com.katyshev.webZakat.exceptions.FileNotFoundException;
import com.katyshev.webZakat.exceptions.WrongFileException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
    private final String separator = File.separator;

    public static List<File> getFileList(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        return Arrays.asList(files);
    }

    public String getUnikoQueryDirectory() {
        return getDirectory("query");
    }

    public String getPriceDirectory() {
        return getDirectory("price");
    }

    public String getOutputDirectory() {
        return getDirectory("output");
    }

    public String getUnikoQueryStorageDirectory() {
        return getDirectory("query" + separator + "storage");
    }

    private String getDirectory(String path) {
        Path directory = Path.of(mainPath + separator + path);
        if (!Files.exists(directory)) {
            try {
                Files.createDirectory(directory);
                log.info(String.format("create \"%s\" directory", path));
            } catch (IOException e) {
                log.warning(String.format("Can not create \"%s\" directory", path));
                throw new RuntimeException(e);
            }
        }
        return directory.toString();
    }

    public String getUnikoQueryFile() {
        File directory = new File(getUnikoQueryDirectory());
        Optional<File> firstFile = Arrays.stream(Objects.requireNonNull(directory.listFiles())).findFirst();

        if (firstFile.isEmpty()) {
            log.warning("Uniko query file does not exist");
            throw new FileNotFoundException("no files found in the directory: " + directory);
        }

        return firstFile.get().toString();
    }

    public String getNewOutputFile() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy_HH-mm-ss");
        String outputFileName = "order_" + LocalDateTime.now().format(formatter) + ".dbf";
        Path path = Path.of(getOutputDirectory() + separator + outputFileName);

        try {
            Files.createFile(path);
            log.info(String.format("Output file \"%s\" was created", path));
        } catch (IOException e) {
            log.warning(String.format("Output file \"%s\" cannot be created", path));
            throw new RuntimeException(e);
        }

        return path.toString();
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

    public void moveUnikoQueryToStorage(String absoluteFilePath) {
        Path src = Path.of(absoluteFilePath);
        Path dest = Path.of(getUnikoQueryStorageDirectory() + separator + src.getFileName());

        try {
            Files.move(src, dest, StandardCopyOption.REPLACE_EXISTING);
            log.info(String.format("File was moved from=%s to=%s", src, dest));
        } catch (IOException e) {
            log.warning(String.format("Can not move file from=%s to=%s", src, dest));
            throw new RuntimeException(e);
        }
    }

    public void writeUserQueryFile(MultipartFile file) {
        String name = file.getOriginalFilename();

        if (file.isEmpty() || name == null) {
            log.warning("user-query-file is empty");
            throw new WrongFileException("No file");
        } else if (!name.toLowerCase(Locale.ROOT).endsWith(".dbf")) {
            log.warning("user-query-file has wrong file format");
            throw new WrongFileException("Wrong file format");
        }

        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(getUnikoQueryDirectory() + File.separator + name)))) {
            byte[] bytes = file.getBytes();
            bos.write(bytes);
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("user-query-file was wrote successfully");
    }
}
