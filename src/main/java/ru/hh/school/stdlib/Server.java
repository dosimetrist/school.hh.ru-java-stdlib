package ru.hh.school.stdlib;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.io.*;

public class Server {
    
    private ServerSocket ss;
    private Substitutor3000 sbst1 = new Substitutor3000();
    private int sleepDelay;

    public class SocketHandler implements Runnable {

        private Socket socket;

        public SocketHandler(Socket connect)  throws IOException {
            socket = connect;
        }

        public void run() {
            BufferedReader hIS = null;
            PrintWriter hOs = null;
            try {
                System.out.println("newConnection handled\n");
                hIS = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                hOs = new PrintWriter(socket.getOutputStream());


                //System.out.println("get input");
                String input = hIS.readLine();
                //System.out.println(input);
                String[] strs = input.split(" ");

                if (strs.length < 2) {
                    System.out.println("wrong_command\n");
                    throw new IllegalArgumentException("Bad or malformed command received");
                }
                if (strs[0].toLowerCase().equals("set")) {
                    if (strs.length != 3) throw new IllegalArgumentException("Bad or malformed command received");
                    sleepDelay = Integer.parseInt(strs[2]);;
                    hOs.append(new String("OK\n")).flush();
                }
                if (strs[0].toLowerCase().equals("get")) {
                    Thread.sleep(sleepDelay);
                    hOs.append(new String("VALUE\n" + sbst1.get(strs[1]) + "\n")).flush();
                }
                if (strs[0].toLowerCase().equals("put")) {
                    Thread.sleep(sleepDelay);
                    StringBuffer buf = new StringBuffer("");
                    for (int i = 2; i < strs.length - 1; i++) {
                        buf.append(strs[i]);
                        buf.append(" ");
                    }
                    if (strs.length > 2) buf.append(strs[strs.length - 1]);
                    sbst1.put(strs[1], buf.toString());
                    hOs.append(new String("OK\n")).flush();
                }
            }

            catch (IllegalArgumentException e) {
                hOs.append(new String("BAD OR MALFORMED COMMAND\n")).flush();
            }
            catch(IOException e){
                e.printStackTrace();
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
            catch (Exception e) {

            }
            finally {
                try {
                    socket.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }



    }


  public Server(InetSocketAddress addr) throws IOException {
      ss = new ServerSocket(addr.getPort());
      System.out.println("Started running...\n");
      sleepDelay = 0;
  }

  public void run() throws IOException {


    while (true) {
        Socket newConnection = ss.accept();
        System.out.println("Got new connection...\n");
        SocketHandler t = new SocketHandler(newConnection);
        Thread handler = new Thread(t);
        handler.start();
        
    }
  }

  public int getPort() {
    return ss.getLocalPort();
  }
}
