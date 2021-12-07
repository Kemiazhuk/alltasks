package com.efimchick.ifmo.io.filetree;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FileTreeImpl implements FileTree {
    private final List<String> res = new ArrayList<>();
    private final static String FILE_DATA_OUTPUT = "%s %s bytes";

    @Override
    public Optional<String> tree(Path path) {
        if ((path == null) || ((!path.toFile().isDirectory()) && (!path.toFile().isFile()))) {
            return Optional.empty();
        }
        if (path.toFile().isFile()) {
            return Optional.of(String.format(FILE_DATA_OUTPUT, path.toFile().getName(), path.toFile().length()));
        }
        res.add(String.format(FILE_DATA_OUTPUT, path.toFile().getName(), folderSize(path.toFile())));
        readFoldersAndFilesByPath(path, 0, new ArrayList<>());
        return Optional.of(String.join("\n", res));
    }

    private void readFoldersAndFilesByPath(Path path, int counterForFolders, List<Boolean> levelFolder) {
        File[] allFiles = path.toFile().listFiles();
        long lastFolder = Arrays.stream(allFiles).filter(File::isDirectory).count();
        long containsFiles = Arrays.stream(allFiles).filter(File::isFile).count();
        long counterForFiles = 0;
        sortFolders(allFiles);
        for (File element : allFiles) {
            if (element.isDirectory()) {
                counterForFolders++;
                levelFolder.add((lastFolder != counterForFolders) || (containsFiles != 0));
                createFolderString(element, levelFolder);
                readFoldersAndFilesByPath(element.toPath(), 0, levelFolder);
                levelFolder.remove(levelFolder.size() - 1);
            } else {
                counterForFiles++;
                createFileString(element, levelFolder, counterForFiles, containsFiles);
            }
        }
    }

    private void sortFolders(File[] files) {
        Arrays.sort(files != null ? files : new File[0], (first, second) -> {
            if (first.isDirectory() && second.isFile()) {
                return -1;
            }
            if (first.isFile() && second.isDirectory()) {
                return 1;
            }
            StringBuilder firstFileName = new StringBuilder(first.getName());
            StringBuilder secondFileName = new StringBuilder(second.getName());
            return firstFileName.toString().compareToIgnoreCase(secondFileName.toString());
        });
    }

    private void createFolderString(File file, List<Boolean> levelFolder) {
        StringBuilder temp = new StringBuilder();
        if (levelFolder.size() > 1) {
            for (int i = 0; i < levelFolder.size() - 1; i++) {
                if (levelFolder.get(i)) {
                    temp.append("│  ");
                } else {
                    temp.append("   ");
                }
            }
        }
        if (levelFolder.get(levelFolder.size() - 1)) {
            temp.append("├─ ");
        } else {
            temp.append("└─ ");
        }
        temp.append(String.format(FILE_DATA_OUTPUT, file.getName(), folderSize(file)));
        res.add(temp.toString());
    }

    private void createFileString(File file, List<Boolean> levelFolder, long counterForFiles, long containsFiles) {
        StringBuilder temp = new StringBuilder();
        for (Boolean level : levelFolder) {
            if (level) {
                temp.append("│  ");
            } else {
                temp.append("   ");
            }
        }
        if (counterForFiles == containsFiles) {
            temp.append("└─ ");
        } else {
            temp.append("├─ ");
        }
        temp.append(String.format(FILE_DATA_OUTPUT, file.getName(), file.length()));
        res.add(temp.toString());
    }

    private long folderSize(File folder) {
        long length = 0;
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.isFile()) {
                length += file.length();
            } else {
                length += folderSize(file);
            }
        }
        return length;
    }
}
