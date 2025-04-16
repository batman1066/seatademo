package org.example.mvc.wrapper;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Servlets {


    public static void transfer(HttpServletResponse response, byte[] bs) throws IOException {
        Streams.transfer(Streams.toInputStream(bs), response.getOutputStream());
    }
}