package com.aguai.utils;

import org.springframework.stereotype.Component;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class FileSystemHandler {

    private final String outputRoot = "generate_project/"; // Saari files is directory mein banengi

    public void writeCodeToFile(String filePath, String fileContent) {
        try {
            File file = new File(outputRoot + filePath);
            // Agar directories (folders) nahi bani hain, toh pehle unhe banao
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }

            // Clean Markdown code blocks wrap logic if AI sent them
            String cleanCode = fileContent.replaceAll("```[a-zA-Z]*\\n", "").replaceAll("```", "");

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(cleanCode);
            }
            System.out.println("[FileSystem]: File successfully written to disk -> " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("[FileSystem Error]: Failed to write file: " + e.getMessage());
        }
    }
}