/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udpserver.Client;

import udpserver.Interface.Handle;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import udpserver.Server.ServerResponeClient;

/**
 *
 * @author PC
 */
public class ThreadClient  extends Thread{
    DatagramPacket in  ;
    DatagramSocket dgramSocket ;
    Handle handle ;
    String message ;
    private static byte[] buff = new byte[256];
    
    public ThreadClient(DatagramSocket dgramSocket, Handle handle){
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
                
                String data[] = message.split("~");
                
                if(data[0].equals(ServerResponeClient.RESPONE_LOGIN.name()) && Boolean.parseBoolean(data[1])){
                    handle.handle(data[1]);
                    break ;
                }
                
                handle.handle(message);
                
                
                
            } catch (IOException ex) {
                Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (true);
        
        
       
    }
    
}
