package service;

import javafx.scene.image.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

public class FileService {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1);
    
    // Generate a unique ID for documents
    public int generateUniqueId() {
        return ID_GENERATOR.getAndIncrement();
    }
    
    // Get file extension
    public String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
    
    // Check if the file is an image
    public boolean isImage(File file) {
        String extension = getFileExtension(file.getName()).toLowerCase();
        return extension.equals("jpg") || extension.equals("jpeg") || 
               extension.equals("png") || extension.equals("gif") || 
               extension.equals("bmp");
    }
    
    // Check if the file is a PDF
    public boolean isPdf(File file) {
        return getFileExtension(file.getName()).toLowerCase().equals("pdf");
    }
    
    // Load image from file
    public Image loadImage(Path path) {
        try {
            return new Image(path.toUri().toString());
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
            return null;
        }
    }
    
    // Get MIME type of file
    public String getMimeType(Path path) {
        try {
            return Files.probeContentType(path);
        } catch (IOException e) {
            System.err.println("Error determining file type: " + e.getMessage());
            return "application/octet-stream";
        }
    }
}