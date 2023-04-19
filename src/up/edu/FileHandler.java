package up.edu;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FileHandler {
    private File path;

    public FileHandler(File path) throws IOException {
        setPath(path);
    }

    public File getPath() {
        return path;
    }

    public void setPath(File path) throws IOException {
        if (!path.isDirectory()) {
            throw new IOException("Enter a valid folder.");
        }
        this.path = path;
    }

    public List<File> getFilteredFiles() throws IOException {
        List<File> filteredFiles = new ArrayList<>();
        for (File fileEntry : path.listFiles()) {
            if (fileEntry.isDirectory()) {
                FileHandler subFolder = new FileHandler(fileEntry);
                filteredFiles.addAll(subFolder.getFilteredFiles()); // recursively analyze the subfolder
            } else {
                String fileName = fileEntry.getName().toLowerCase();
                boolean matcher = Pattern.matches(".*\\.(mkv|png|jpg|mp4|heic|mov)$", fileName);
                if (matcher) {
                    filteredFiles.add(fileEntry);
                }
            }
        }
        return filteredFiles;
    }
}

