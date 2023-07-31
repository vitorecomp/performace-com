package bench.perf.com.domain.filesystem;

public class FileProprieties {
    String name;
    String folder;

    Integer size;

    public FileProprieties() {
    }

    public FileProprieties(String name, String folder, Integer size) {
        this.name = name;
        this.folder = folder;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
