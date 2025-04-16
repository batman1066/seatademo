package org.example.mvc.wrapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Streams {

    public static int transfer(InputStream input, OutputStream output) throws IOException {
        long count = transferLarge(input, output);
        return count > 2147483647L ? -1 : (int) count;
    }

    public static long transferLarge(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[8192];

        long count;
        int n;
        for (count = 0L; (n = input.read(buffer)) != -1; count += (long) n) {
            output.write(buffer, 0, n);
        }

        return count;
    }

    public static InputStream toInputStream(byte[] bs) {
        return new ByteArrayInputStream(bs);
    }


}