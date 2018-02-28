package com.customframework.core;

import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created on 23.02.2018
 *
 * @author Roman Hayda
 */
public class FileFinder extends SimpleFileVisitor<Path> {

    private PathMatcher matcher;

    private Set<Class> candidates = new LinkedHashSet<>();

    public Set<Class> getCandidates() {
        return candidates;
    }

    public FileFinder(String pattern) {
        try {
            matcher = FileSystems.getDefault().getPathMatcher(pattern);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid pattern; did you forget to prefix \"glob:\" or \"regex:\"?");
//            System.exit(1);
        }

    }

    public FileVisitResult visitFile(Path path, BasicFileAttributes fileAttributes) {
        try {
            find(path);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return FileVisitResult.CONTINUE;
    }

    private void find(Path path) throws ClassNotFoundException {
        Path name = path.getFileName();
        if (matcher.matches(name)) {
            System.out.println("Matching file:" + path.getFileName());
            candidates.add(Class.forName(name.toString()));
        }
    }

    public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes fileAttributes) {
        try {
            find(path);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return FileVisitResult.CONTINUE;
    }

}
