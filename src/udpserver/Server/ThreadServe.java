/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udpserver.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import udpserver.Client.ThreadClient;
import udpserver.Interface.Handle;

/**
 *
 * @author PC
 */
public class ThreadServe extends Thread {
    DatagramPacket in  ;
    DatagramSocket dgramSocket ;
    Handle handle ;
    String message ;
    private static byte[] buff = new byte[256];
    
    public ThreadServe(DatagramSocket dgramSocket, Handle handle){
        this.in = new DatagramPacket(buff, buff.length) ;
        this.dgramSocket = dgramSocket ;
        this.handle = handle ;
        
    }
    

    @Override
    public void run() {
        
        do {            
            try {
                dgramSocket.receive(in);
                message = new String(in.getData(), 0, in.getLength());
                
                handle.handle(message);
                
                
                
            } catch (IOException ex) {
                Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (true);
        
        
       
    }
}
