import up.edu.*;

import java.io.IOException;
import java.io.File;
import java.sql.Timestamp;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Start: " + new Timestamp(new Date().getTime()));
        Scanner sc = new Scanner(System.in);

        try {
            System.out.println("Enter your folder path: ");
            File folder = new File(sc.nextLine());
            FileHandler fileHandler = new FileHandler(folder);
            List<File> filteredFiles = fileHandler.getFilteredFiles();
            Metadata metadata = new Metadata();
            metadata.extractMetadata(filteredFiles);
        } catch (IOException e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}
