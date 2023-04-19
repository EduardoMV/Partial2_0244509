package up.edu;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class MultiFiles {

    private Path path;
    private LocalDateTime CreateDate;
    private String Location;

    public MultiFiles(Path path, LocalDateTime createDate, String location) {
        setPath(path);
        setCreateDate(createDate);
        setLocation(location);
        Map<Path, String> filesMap = new HashMap<>();
        for(int i = 0; i <= filesMap.size(); i++){
            filesMap.put(getPath(), String.valueOf(i));
        }
    }
    

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public LocalDateTime getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        CreateDate = createDate;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }
}
