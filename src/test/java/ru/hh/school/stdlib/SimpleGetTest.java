package ru.hh.school.stdlib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


public class SimpleGetTest extends BaseFunctionalTest {
  @Test
     public void simpleGet() throws IOException {
        Socket s = connect();

        Writer out = new PrintWriter(s.getOutputStream());
        out.append("GET k1\n").flush();
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

        Assert.assertEquals("VALUE", in.readLine());
        Assert.assertEquals("", in.readLine());
        s.close();
    }

    @Test
    public void anotherGet() throws IOException {
        Socket s1 = connect();

        Writer out = new PrintWriter(s1.getOutputStream());
        out.append("PUT k1 qwerty\n").flush();
        BufferedReader in = new BufferedReader(new InputStreamReader(s1.getInputStream()));

        Assert.assertEquals("OK", in.readLine());
        s1.close();

        Socket s2 = connect();

        Writer out2 = new PrintWriter(s2.getOutputStream());
        out2.append("GET k1\n").flush();
        BufferedReader in2 = new BufferedReader(new InputStreamReader(s2.getInputStream()));

        Assert.assertEquals("VALUE", in2.readLine());
        Assert.assertEquals("qwerty", in2.readLine());
       s2.close();
    }

    @Test
    public void sleepTest() throws  IOException {
        Socket s = connect();
        Writer out = new PrintWriter(s.getOutputStream());
        out.append("SET SLEEP 2000\n").flush();
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

        Assert.assertEquals("OK", in.readLine());
        s.close();
    }

    @Test
    public void severalActions() throws InterruptedException, ExecutionException{
        ExecutorService ex = Executors.newCachedThreadPool();
        List<Callable<String>> tasks = new ArrayList<Callable<String>>();
        for (int i = 0; i < 5; i++) {
            final int j = i;
            tasks.add(new Callable<String>() {
                public String call() throws IOException{
                    Socket s = connect();
                    Writer out = new PrintWriter(s.getOutputStream());
                    out.append("SET SLEEP 2000\n").flush();
                    BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    String res = in.readLine();
                    s.close();
                    return res;
                }
            });
        }
        for (Future<String> i : ex.invokeAll(tasks)) {
            Assert.assertEquals(i.get(),  "OK");
        }
    }

}
