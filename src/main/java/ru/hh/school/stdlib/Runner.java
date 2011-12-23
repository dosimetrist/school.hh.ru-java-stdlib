package ru.hh.school.stdlib;

import java.io.*;
import java.net.Socket;

public class Runner implements Runnable {

    private Socket socket;
    private Substitutor3000 sbst1;
    private int sleepDelay;

    public Runner(Socket connect, int delay, Substitutor3000 sbst)  throws IOException {
        socket = connect;
        sbst1 = sbst;
        sleepDelay = delay;
    }

    public void run() {
        InputStream is = null;
        OutputStream os = null;
        BufferedReader hIS = null;//new BufferedReader(new InputStreamReader(is));
        PrintWriter hOs = null;//new BufferedWriter(new OutputStreamWriter(os));
        try {
            System.out.println("newConnection handled\n");
            //is = socket.getInputStream();
            //os = socket.getOutputStream();
            hIS = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            hOs = new PrintWriter(socket.getOutputStream());

            //byte[] inputBuf = new byte[256];
            //int readBytes = is.read(inputBuf);
            //String input = new String(inputBuf, 0, readBytes);
            System.out.println("des");
            String input = hIS.readLine();
            System.out.println(input);
            String[] strs = input.split(" ");

            if (strs.length < 2) {
                System.out.println("wrong_command\n");
                throw new IllegalArgumentException("Bad or malformed command received");
            }
            if (strs[0].toLowerCase().equals("set")) {
                if (strs.length != 3) throw new IllegalArgumentException("Bad or malformed command received");
                sleepDelay = Integer.parseInt(strs[2]);;
                //os.write(new String("OK\n").getBytes());
                hOs.print(new String("OK\n"));
            }
            if (strs[0].toLowerCase().equals("get")) {
                Thread.sleep(sleepDelay);
                //os.write(new String("VALUE\n" + sbst1.get(strs[1]) + "\n").getBytes());
                hOs.print(new String("VALUE\n" + sbst1.get(strs[1]) + "\n"));
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
                //os.write(new String("OK\n").getBytes());
                hOs.print(new String("OK\n"));
            }
        }

        catch (IllegalArgumentException e) {
           // try {
                // os.write(new String("BAD OR MALFORMED COMMAND\n").getBytes());
                hOs.print(new String("BAD OR MALFORMED COMMAND\n"));
            //}
            /*catch (IOException ioE) {
                ioE.printStackTrace();
            }  */
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
                // if (is != null) is.close();
                //if (os != null) os.close();
                socket.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
