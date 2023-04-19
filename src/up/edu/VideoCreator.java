package up.edu;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class VideoCreator {

    private String ffmpegPath;

    public VideoCreator(String ffmpegPath) {
        this.ffmpegPath = ffmpegPath;
    }

    public void createVideo(List<File> fileList, String outputFilePath) throws IOException, InterruptedException {
        // Sort files by creation date
        List<File> sortedFiles = fileList.stream()
                .sorted(Comparator.comparingLong(File::lastModified))
                .collect(Collectors.toList());

        // Create temporary file list
        List<Path> tempFilePaths = new ArrayList<>();

        // Create temporary images and add them to the list
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        for (File file : sortedFiles) {
            Path tempFilePath = Files.createTempFile("img_" + LocalDateTime.now().format(formatter), ".jpg");
            tempFilePaths.add(tempFilePath);
            String[] cmd = {ffmpegPath, "-i", file.getAbsolutePath(), "-vf", "scale=1920:-1", "-q:v", "2", "-vframes", "1", "-y", tempFilePath.toString()};
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
        }

        // Create list of temporary file paths for ffmpeg input
        String inputListFilePath = Files.createTempFile("input_list_", ".txt").toString();
        Files.write(Paths.get(inputListFilePath), tempFilePaths.stream().map(Path::toString).collect(Collectors.toList()));

        // Create video using ffmpeg
        String[] cmd = {ffmpegPath, "-f", "concat", "-safe", "0", "-i", inputListFilePath, "-c:v", "libx264", "-pix_fmt", "yuv420p", "-movflags", "+faststart", "-y", outputFilePath};
        Process p = Runtime.getRuntime().exec(cmd);
        p.waitFor();

        // Delete temporary files
        for (Path tempFilePath : tempFilePaths) {
            Files.deleteIfExists(tempFilePath);
        }
        Files.deleteIfExists(Paths.get(inputListFilePath));
    }
}