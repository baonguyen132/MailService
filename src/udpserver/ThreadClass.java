/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udpserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import static udpserver.Frame.msgIn;

/**
 *
 * @author PC
 */
public class ThreadClass  extends Thread{
    DatagramPacket in  ;
    DatagramSocket dgramSocket ;
    Handle h ;
    String message ;
    public ThreadClass(DatagramPacket in , DatagramSocket dgramSocket, Handle h){
        this.in = in ;
        this.dgramSocket = dgramSocket ;
        this.h = h ;
    }
    

    @Override
    public void run() {
        
        do {            
            try {
                dgramSocket.receive(in);
                message = new String(in.getData(), 0, in.getLength());
                h.handle(message);
            } catch (IOException ex) {
                Logger.getLogger(ThreadClass.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (!message.equals("BYE"));
        
       
    }
    
}
