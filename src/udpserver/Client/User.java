/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package udpserver.Client;

import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author PC
 */
public class User {
    String name ;
    InetAddress address ;
    int port ;
    DatagramSocket datagramSocket ;

    public User(String name, InetAddress address, int port, DatagramSocket datagramSocket) {
        this.name = name;
        this.address = address;
        this.port = port;
        this.datagramSocket = datagramSocket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public DatagramSocket getDatagramSocket() {
        return datagramSocket;
    }

    public void setDatagramSocket(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }
    
    
    
}
