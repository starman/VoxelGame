package voxelgame.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Utils {
    private Utils() {

    }

    public static String readFile(String filepath) {
        String result;
        try {
            result = new String(Files.readAllBytes(Paths.get(filepath)));
        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Error: Could not open file for shader: '" + filepath + "'";
            throw new RuntimeException("Error: Could not open file for shader: '" + filepath + "'", e);
        }
        return result;
    }
}
