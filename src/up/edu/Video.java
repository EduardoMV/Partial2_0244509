package up.edu;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Video {

    String projectDir = System.getProperty("user.dir");
    String ffmepgPath = projectDir + "/files";
    private static final String[] ImgExt = {"png", "jpg", "heic"};
    private static final String videoPath = "/path/to/ffmpeg"; // replace with the path to ffmpeg executable

    public void createVideo(Path folderPath) throws IOException, InterruptedException {
        List<File> imageFiles = Files.list(folderPath)
                .filter(Files::isRegularFile)
                //.filter(file -> Arrays.asList(ImgExt).contains(getFileExtension(file.getName())))
                .map(Path::toFile)
                .collect(Collectors.toList());

        if (!imageFiles.isEmpty()) {
            // create a video of 1 second for the newest image file
            File newestImageFile = imageFiles.stream().max(Comparator.comparing(File::lastModified)).get();
            String newVideoFileName = removeExtension(newestImageFile.getName()) + ".mp4";
            Path newVideoFilePath = folderPath.resolve(newVideoFileName);
            ProcessBuilder builder = new ProcessBuilder(
                    videoPath,
                    "-loop", "1",
                    "-t", "1",
                    "-i", newestImageFile.getAbsolutePath(),
                    "-c:v", "libx264",
                    "-pix_fmt", "yuv420p",
                    newVideoFilePath.toString()
            );
            builder.redirectErrorStream(true);
            builder.start().waitFor();

            // delete the oldest image file
            File oldestImageFile = imageFiles.stream().min(Comparator.comparing(File::lastModified)).get();
            Files.deleteIfExists(oldestImageFile.toPath());
        }

        // create a video with all the remaining files
        String allVideosFileName = "all_videos.mp4";
        Path allVideosFilePath = folderPath.resolve(allVideosFileName);
        ProcessBuilder builder = new ProcessBuilder(
                videoPath,
                "-f", "concat",
                "-safe", "0",
                "-i", folderPath.resolve("files.txt").toString(),
                "-c:v", "libx264",
                "-pix_fmt", "yuv420p",
                allVideosFilePath.toString()
        );
        builder.redirectErrorStream(true);
        builder.start().waitFor();
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        } else {
            return "";
        }
    }

    private String removeExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(0, dotIndex);
        } else {
            return fileName;
        }
    }
}

