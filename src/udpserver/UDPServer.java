/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package udpserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 * @author PC
 */
public class UDPServer {
    
    private static final int PORT = 1234;
    private static DatagramSocket dgramSocket;
    private static DatagramPacket inPkt, outPkt;
    private static byte[] buffer;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Opening port...\n");
        try {
            dgramSocket = new DatagramSocket(PORT);
        } catch (SocketException e) {
            System.out.println("Error attach port!");
            System.exit(1);
        }
        run();
    }

    private static void run() {
        String msgIn,msgOut;
        int numMsgs = 0;
        try {
            do {
                buffer = new byte[256];
                inPkt = new DatagramPacket(buffer,buffer.length);
                dgramSocket.receive(inPkt);

                InetAddress cliAddress = inPkt.getAddress();
                int cliPort = inPkt.getPort();

                msgIn = new String(inPkt.getData(),0,inPkt.getLength());
                System.out.println("Message received.");
                numMsgs++;
                msgOut = ("Msg "+numMsgs+ ": "+msgIn);

                outPkt = new DatagramPacket(msgOut.getBytes(), msgOut.length(),cliAddress,cliPort);
                


            }while (true) ;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            dgramSocket.close();
        }
    }
    
}
