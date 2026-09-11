package com.marketplace.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Сервис для работы с файлами (картинками товаров).
 */
@Service
@RequiredArgsConstructor
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private Path rootLocation;

    /**
     * Создаёт папку для загрузки файлов при старте приложения.
     */
    @PostConstruct
    public void init() {
        try {
            rootLocation = Paths.get(uploadDir);
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать папку для загрузки файлов", e);
        }
    }

    /**
     * Сохраняет загруженный файл и возвращает его имя.
     * Используется продавцом при загрузке картинки товара.
     *
     * @param file загруженный файл
     * @return уникальное имя файла
     */
    public String saveFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Файл пустой");
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String extension = "";

            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String filename = UUID.randomUUID() + extension;
            Path destination = rootLocation.resolve(filename);

            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Не удалось сохранить файл", e);
        }
    }

    /**
     * Удаляет файл по имени.
     * Используется при удалении товара.
     *
     * @param filename имя файла
     */
    public void deleteFile(String filename) {
        if (filename == null || filename.isEmpty()) {
            return;
        }

        try {
            Path file = rootLocation.resolve(filename);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось удалить файл: " + filename, e);
        }
    }
}