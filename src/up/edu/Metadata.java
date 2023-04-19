package up.edu;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Metadata {

    public void extractMetadata(List<File> fileList) throws IOException {
        Map<String, File> metadataMap = new HashMap<>();
        for (File fileEntry : fileList) {
            // Run exiftool command and capture output
            System.out.println(fileEntry.getName());
            String projectDir = System.getProperty("user.dir");
            String exiftoolPath = projectDir + "/src/up/edu/tools/Image-ExifTool-12/57/exiftool";
            String[] cmd = {"/bin/bash", "-c", exiftoolPath + " -j \"" + fileEntry.getAbsolutePath() + "\""};
            try {
                Process p = Runtime.getRuntime().exec(cmd);
                String output = new String(p.getInputStream().readAllBytes());
                System.out.println(output); // Json printer

                // Extract CreateDate metadata using regex
                String createDate = null;
                Pattern pattern = Pattern.compile("\"CreateDate\": \"(.*?)\"");
                Matcher matcher = pattern.matcher(output);
                if (matcher.find()) {
                    createDate = matcher.group(1);
                }

                if (createDate != null) {
                    if (createDate == null) {
                        createDate = LocalDateTime.now().toString();
                    }
                    metadataMap.put(createDate, fileEntry);
                }
            } catch (IOException e) {
                System.err.println("Error running exiftool command: " + e.getMessage());
            }
        }
        // Sort files by oldest to newest using CreateDate metadata
        sortingFiles(metadataMap);
    }

    public Path sortingFiles(Map<String, File> metadataMap) throws IOException {
        Path folderPath = Paths.get(System.getProperty("user.dir"), "files");
        if (!Files.exists(folderPath)) {
            Files.createDirectory(folderPath);
        }

        int fileCount = 1;
        for (Map.Entry<String, File> entry : metadataMap.entrySet()) {
            File sourceFile = entry.getValue();
            String fileExtension = getFileExtension(sourceFile.getName());
            //String output = new String(Files.readAllBytes(Files.copy(fileEntry.toPath(), folderPath.resolve(fileEntry.getName() + ".copy"))));
            String newFileName = "file" + String.format("%02d", fileCount) + "." + fileExtension;
            try {
                Files.move(sourceFile.toPath(), folderPath.resolve(newFileName));
                fileCount++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(folderPath.toAbsolutePath());
        return folderPath.toAbsolutePath();
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        } else {
            return "";
        }
    }
}



