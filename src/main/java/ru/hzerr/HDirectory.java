package ru.hzerr;

import org.apache.commons.io.FileUtils;
import ru.hzerr.collections.HCollectors;
import ru.hzerr.collections.list.ArrayHList;
import ru.hzerr.collections.list.HList;
import ru.hzerr.exception.ValidationException;
import ru.hzerr.exception.directory.HDirectoryCreateImpossibleException;
import ru.hzerr.exception.directory.HDirectoryIsNotDirectoryException;
import ru.hzerr.exception.directory.HDirectoryNotFoundException;
import ru.hzerr.exception.file.HFileCreateImpossibleException;
import ru.hzerr.exception.file.HFileCreationFailedException;
import ru.hzerr.exception.file.HFileIsNotFileException;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class HDirectory {

    File directory;

    public HDirectory(String pathname) {
        this.directory = new File(pathname);
        validate();
    }

    public HDirectory(String parent, String child) {
        this.directory = new File(parent, child);
        validate();
    }

    public HDirectory(URI uri) {
        this.directory = new File(uri);
        validate();
    }

    public HDirectory(HDirectory parent, String child) {
        this.directory = new File(parent.directory, child);
        validate();
    }

    public void create() throws HDirectoryIsNotDirectoryException, HDirectoryCreateImpossibleException {
        if (directory.exists()) {
            if (directory.isFile()) {
                throw new HDirectoryIsNotDirectoryException("File " + directory + " exists and is not a directory. Unable to create directory.");
            }
        } else if (!directory.mkdirs()) {
            // Double-check that some other thread or process hasn't made
            // the directory in the background
            if (directory.isFile()) {
                throw new HDirectoryCreateImpossibleException("Unable to create directory " + directory);
            }
        }
    }

    public String getDirectoryName() { return this.directory.getName(); }

    public HDirectory createSubDirectory(String dirName) throws HDirectoryIsNotDirectoryException, HDirectoryCreateImpossibleException {
        HDirectory subDirectory =  new HDirectory(this, dirName);
        subDirectory.create();
        return subDirectory;
    }

    public HFile createSubFile(String fileName) throws HFileIsNotFileException, HFileCreationFailedException, HFileCreateImpossibleException {
        HFile subFile = new HFile(this, fileName);
        subFile.create();
        return subFile;
    }

    public HDirectory getSubDirectory(String dirName) { return new HDirectory(this, dirName); }
    public HFile getSubFile(String fileName) { return new HFile(this, fileName); }

    public HList<HFile> getFiles() {
        File[] files = this.directory.listFiles();
        if (files != null) {
            HList<HFile> subFiles = new ArrayHList<>();
            for (File file: files) {
                if (file.isFile()) {
                    subFiles.add(new HFile(file.getAbsolutePath()));
                }
            }
            return subFiles;
        } else return null;
    }

    public HList<HDirectory> getDirectories() {
        File[] files = this.directory.listFiles();
        if (files != null) {
            HList<HDirectory> subFiles = new ArrayHList<>();
            for (File file: files) {
                if (file.isDirectory()) {
                    subFiles.add(new HDirectory(file.getAbsolutePath()));
                }
            }
            return subFiles;
        } else return null;
    }

    public HList<FSObject> getFiles(boolean recursive) throws IOException {
        if (recursive) {
            return walk().map(FSObject::new).collect(HCollectors.toHList());
        } else
            return ArrayHList.create(directory.listFiles()).map(FSObject::new);
    }

    public boolean exists() { return this.directory.exists(); }
    public boolean notExists() { return !this.directory.exists(); }

    public HDirectory getParent() { return new HDirectory(directory.getAbsoluteFile().getParent()); }

    public void cleanAll() throws IOException { FileUtils.cleanDirectory(directory); }
    public void clean(String name) throws IOException { FileUtils.forceDelete(new File(directory, name)); }
    public void delete() throws IOException { FileUtils.deleteDirectory(directory); }

    public void copyToDirectory(HDirectory directory) throws IOException {
        checkExists(this, directory);
        FileUtils.copyDirectoryToDirectory(this.directory, directory.directory);
    }

    public void copyContentToDirectory(HDirectory directory) throws IOException {
        checkExists(this, directory);
        FileUtils.copyDirectory(this.directory, directory.directory);
    }

    public void moveToDirectory(HDirectory directory) throws IOException {
        checkExists(this, directory);
        FileUtils.moveDirectoryToDirectory(this.directory, directory.directory, false);
    }

    public void moveContentToDirectory(HDirectory directory) throws IOException {
        checkExists(this, directory);
        FileUtils.moveDirectory(this.directory, directory.directory);
    }

    public double sizeOf(SizeType type) {
        BigDecimal size = new BigDecimal(FileUtils.sizeOfDirectoryAsBigInteger(directory));
        return SizeType.BYTE.to(type, size).setScale(1, RoundingMode.DOWN).doubleValue();
    }

    @Override
    public String toString() { return directory.getAbsolutePath(); }

    private void validate() throws ValidationException {
        if (directory.isFile())
            throw new ValidationException(directory + " is a file");
    }

    private void checkExists(HDirectory... directories) throws HDirectoryNotFoundException {
        Objects.requireNonNull(directories, "Directories");
        for (HDirectory directory : directories) {
            if (directory.notExists())
                throw new HDirectoryNotFoundException("Directory does not exist: " + directory);
        }
    }

    private HStream<Path> walk() throws IOException {
        return HStream.of(Files.walk(directory.toPath()));
    }
}
