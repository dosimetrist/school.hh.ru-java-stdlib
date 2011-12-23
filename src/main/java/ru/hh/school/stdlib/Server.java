package ru.hh.school.stdlib;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.io.*;

public class Server {
    
    private ServerSocket ss;
    private InetSocketAddress serverAddr;
    private Substitutor3000 sbst1 = new Substitutor3000();
    private int sleepDelay;
    


  public Server(InetSocketAddress addr) throws IOException {
     //try {

      serverAddr = addr;
      sleepDelay = 0;

      //ss.bind(serverAddr);
    // }
     //catch (IOException e) {
       //  throw e;
     //}
  }

  public void run() throws IOException {

     ss = new ServerSocket(serverAddr.getPort());
    System.out.println("Started running...\n");
    while (true) {
        Socket newConnection = ss.accept();
        System.out.println("Got new connection...\n");
        Runner t = new Runner(newConnection, sleepDelay, sbst1);
        Thread handler = new Thread(t);
        handler.start();
        
    }
  }

  public int getPort() {
    return serverAddr.getPort();
  }
}
