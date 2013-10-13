/**
 * Copyright (c) 2013 ICZ Slovakia a.s.. All Rights Reserved.
 */
package com.icz.interview.webapp.dao;

import java.io.File;
import java.io.FileFilter;

/**
 * @author tibor17 (Tibor Digana)
 * @since 2.0
 * @version 2.0
 */
final class FileUtils {
    private FileUtils() {
    }

    private static final FileFilter DIRECTORIES = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return pathname.isDirectory();
        }
    };

    private static final FileFilter FILES = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return !pathname.isDirectory();
        }
    };

    public static void delete(File fileOrDir) {
        if (fileOrDir.isDirectory()) {
            for (File file : fileOrDir.listFiles(FILES)) {
                file.delete();
            }

            for (File dir : fileOrDir.listFiles(DIRECTORIES)) {
                delete(dir);
            }
        }
        fileOrDir.delete();
    }
}