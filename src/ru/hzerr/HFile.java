package ru.hzerr;

import org.apache.commons.io.FileUtils;
import ru.hzerr.exception.ValidationException;
import ru.hzerr.exception.directory.HDirectoryNotFoundException;
import ru.hzerr.exception.file.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;

public class HFile {

    File file;

    public HFile(String pathname) {
        this.file = new File(pathname);
        validate();
    }

    public HFile(String parent, String child) {
        this.file = new File(parent, child);
        validate();
    }

    public HFile(HDirectory parent, String child) {
        this.file = new File(parent.directory, child);
        validate();
    }

    public HFile(URI uri) {
        this.file = new File(uri);
        validate();
    }

    public String getFileName() { return this.file.getName(); }

    public void create() throws HFileIsNotFileException, HFileCreationFailedException, HFileCreateImpossibleException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new HFileIsNotFileException("File " + file + " exists and is not a file. Unable to create file.");
            }
        } else {
            boolean created;
            try {
                created = file.createNewFile();
            } catch (IOException io) { throw new HFileCreationFailedException(io.getMessage()); }
            if (!created) {
                // Double-check that some other thread or process hasn't made
                // the file in the background
                if (file.isDirectory()) {
                    throw new HFileCreateImpossibleException("Unable to create file " + file);
                }
            }
        }
        validate();
    }

    public void delete() throws IOException {
        checkExists(this);
        FileUtils.forceDelete(file);
    }

    public void copyToFile(HFile file) throws IOException {
        checkExists(file, this);
        FileUtils.copyFile(this.file, file.file);
    }

    public void copyToDirectory(HDirectory directory) throws IOException {
        checkExists(directory, this);
        FileUtils.copyToDirectory(file, directory.directory);
    }

    public void moveToFile(HFile file) throws IOException {
        checkExists(file, this);
        FileUtils.moveFile(this.file, file.file);
    }

    public void moveToDirectory(HDirectory directory) throws IOException {
        checkExists(directory, this);
        FileUtils.moveFileToDirectory(file, directory.directory, false);
    }

    public HDirectory getParent() { return new HDirectory(file.getAbsoluteFile().getParent()); }

    public boolean exists() { return file.exists(); }
    public boolean notExists() { return !file.exists(); }

    @Override
    public String toString() { return file.getAbsolutePath(); }

    private void validate() throws ValidationException {
        if (file.isDirectory())
            throw new ValidationException(file + " is a directory");
    }

    private void checkExists(HFile... files) throws HFileNotFoundException {
        Objects.requireNonNull(files, "Files");
        for (HFile file : files) {
            if (file.notExists())
                throw new HFileNotFoundException("File does not exist: " + file);
        }
    }

    private void checkExists(HDirectory... directories) throws HDirectoryNotFoundException {
        Objects.requireNonNull(directories, "Directories");
        for (HDirectory directory : directories) {
            if (directory.notExists())
                throw new HDirectoryNotFoundException("Directory does not exist: " + directory);
        }
    }

    private void checkExists(HDirectory directory, HFile file) throws HFileNotFoundException, HDirectoryNotFoundException {
        checkExists(directory);
        checkExists(file);
    }
}
