# HFile
Java library for working with files

```java
import ru.hzerr.collections.list.ArrayHList;

public class Main {
  
    public static void main(String... args) throws Throwable {
        HDirectory downloads = new HDirectory("C:\\Users\\User\\Downloads");
        System.out.println("Size downloads: " + downloads.sizeOf(SizeType.MB) + "mb");
        HFile yandexDisk = downloads.getSubFile("YandexDisk30Setup.exe");
        HDirectory desktop = downloads.getParent().getSubDirectory("Desktop");
        yandexDisk.copyToDirectory(desktop);
        yandexDisk.delete();
        
        HFile readme = downloads.createSubFile("README.md");
        readme.writeLines(ArrayHList.create("This", " is ", "handy!"));
        System.out.println("Readme read: " + readme.readLines(Charset.defaultCharset()));
        
        HDirectory project = new HDirectory("C:\\Users\\User\\IdeaProjects\\Project");
        readme.moveToDirectory(project);
    }
}
```
