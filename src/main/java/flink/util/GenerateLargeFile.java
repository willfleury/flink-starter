package flink.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class GenerateLargeFile {

    public static void main(String[] args) throws Exception {
        byte[] line = "booooo a really long line is what i want..\n".getBytes();

        try (BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(new File("/tmp/large.txt")))) {
            int i = 0;

            while (i++ < 1_000_000)
                bos.write(line);
        }
    }
}
