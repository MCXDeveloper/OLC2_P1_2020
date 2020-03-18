package com.estaticas;

import java.io.File;
import java.util.Random;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class Helper {

    static final Random rand = new java.util.Random();
    static final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";

    private static Boolean FileExists(String path) {
        File archivo = new File(path);
        return archivo.exists();
    }

    public static String[] ObtenerComponentesPath(String path) {
        if(FileExists(path)) {
            String[] str = new String[4];
            str[0] = FilenameUtils.getFullPath(path);
            str[1] = FilenameUtils.getBaseName(path);
            str[2] = FilenameUtils.getExtension(path);
            str[3] = str[1] + "." + str[2];
            return str;
        }
        return null;
    }

    public static String[] ObtenerPathSinUltimoDirectorio(String path) {
        Path p = Paths.get(path);
        System.out.println(p.getParent());
        return new String[] { p.getParent().toString(), p.getFileName().toString() };
    }

    public static String ObtenerContenidoArchivo(String path) {
        try {
            return FileUtils.readFileToString(new File(path));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String getRandomName() {
        StringBuilder builder = new StringBuilder();
        while(builder.toString().length() == 0) {
            int length = rand.nextInt(5);
            for(int i = 0; i < length; i++) {
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            }
        }
        return builder.toString();
    }

    public static boolean isAbsolutePath(String path) {
        File archivo = new File(path);
        return archivo.isAbsolute();
    }

    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }

}