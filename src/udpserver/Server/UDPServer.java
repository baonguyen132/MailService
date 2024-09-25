/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package udpserver.Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import netscape.javascript.JSObject;
import udpserver.Client.ClientRequestServer;
import udpserver.Client.ClientStatus;

/**
 *
 * @author PC
 */
public class UDPServer {
    
    private static final int PORT = 1234;
    private static DatagramSocket dgramSocket;
    private static byte[] buffer = new byte[256];
    private static DatagramPacket inPkt, outPkt;
    private static String dir = "D:\\VKU/3/Network/Lap 5/Code/UDPServer/MailService/data/" ;
    private static PrintWriter pwMain  ;
    private static String datafile = "" ;
    
    private static ArrayList<InetAddress> listAddress = new ArrayList<>();
    private static ArrayList<Integer> listPort = new ArrayList<>();
    
    private static InetAddress hostServer ;
    private static int portServer ;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Opening port...\n");
        try {
            dgramSocket = new DatagramSocket(PORT);
            try { 
                File file = new File(dir+"/list.txt");
                BufferedReader br = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8);
                
                datafile =  br.readLine()  ;
                br.close();
               
            } catch (FileNotFoundException ex) {
                Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SocketException e) {
            System.out.println("Error attach port!");
            System.exit(1);
        }
        run();
    }
    private static void appendContent(String sender, String target, String subject, String content) {
        BufferedReader br = null;
        try {
            File file = new File(dir+target+"/listMail.txt");
            br = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8);
            String dataListMail =  br.readLine()  ;
            br.close();
            
            PrintWriter pwMainAppend = new PrintWriter(dir+target+"/listMail.txt");
            dataListMail = dataListMail + "//" +sender+"@"+subject ;
            pwMainAppend.println(dataListMail) ;
            pwMainAppend.flush();
            pwMainAppend.close();
            
            File file1 = new File(dir+target+"/"+subject+".txt");
            file1.createNewFile();
            
            PrintWriter createFile = new PrintWriter(dir+target+"/"+subject+".txt");
            createFile.println(sender);
            createFile.println(target);
            createFile.println(subject);
            createFile.print(content);
            createFile.flush();
            createFile.close();
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    } 
    private static void setUpData(InetAddress address, int cliPort, String username, String password) {
        PrintWriter pw = null ;
        try {
            pw = new PrintWriter(dir+username+"/data.txt");
            pw.println(address.toString());
            pw.println(cliPort);
            pw.println(ClientStatus.ONLINE.name());
            pw.println(username);
            pw.println(password) ;
            pw.flush();
            pw.close();
            
            pwMain = new PrintWriter(dir+"list.txt");
            datafile = datafile + "~" +username ;
            pwMain.println(datafile) ;
            pwMain.flush();
            pwMain.close();
            
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            pw.close();
        }
    }
    
    private static void login(InetAddress address, int cliPort, String username, String password) {
        
        boolean condition = false ;
        try {
            File foler1 = new File(dir+username);
             
            if(!foler1.exists()){
                foler1.mkdir() ;
                
                File file1 = new File(dir+username+"/data.txt") ;
                file1.createNewFile() ;
                
                File file2 = new File(dir+username+"/listMail.txt");
                file2.createNewFile();
                
                setUpData(address, cliPort, username, password);
                appendContent("server", username, "welcome", "Thank you for using this service. we hope that you will feel comfortabl........") ;
                
                
                for (int i = 0; i < listAddress.size(); i++) { sendList(listAddress.get(i), listPort.get(i)); }
                listAddress.add(address); listPort.add(cliPort);
                
                condition = true ;
            }
            else {
                File file = new File(dir+username+"/data.txt");
                BufferedReader br = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8);
                ArrayList<String> data = new ArrayList<>();
               
                String line = "";
                do {                    
                    line = br.readLine();
                    if(line != null) data.add(line);
                } while (line != null);
                br.close();
                
                if(password.equals(data.getLast())) {
                    condition = true ;
                    
                    data.set(0, address.toString());
                    data.set(1, cliPort+"");
                    
                    boolean isEmty = true ;
                    for (int i = 0; i < listAddress.size(); i++) {
                        if(listAddress.get(i).toString().equals(data.get(0))){
                            listAddress.set(i, address);
                            listPort.set(i, cliPort);
                            isEmty = false ;
                        }
                    }
                    
                    if(isEmty){
                        listAddress.add(address);
                        listPort.add(cliPort);
                    }
                    
                    PrintWriter pw = null ;
                    pw = new PrintWriter(dir+username+"/data.txt");
                    pw.println(data.get(0));
                    pw.println(data.get(1));
                    pw.println(ClientStatus.ONLINE.name());
                    pw.println(username);
                    pw.println(password) ;
                    pw.flush();
                    pw.close();
                    
                    
                    
                }    
            }
            
            
            // Ghi dữ liệu
            
            String msgOut = ServerResponeClient.RESPONE_LOGIN.name() + "~" + condition;
            outPkt = new DatagramPacket(msgOut.getBytes(), msgOut.length(),address,cliPort);
            dgramSocket.send(outPkt);
            
            msgOut = ServerResponeClient.HAVE_USER.name() + "~" + address + "@" + cliPort + "@" + username ;
            outPkt = new DatagramPacket(msgOut.getBytes(), msgOut.length(),hostServer,portServer);
            dgramSocket.send(outPkt);
            
        } catch (IOException ex) {  
            System.out.println(ex.toString());
        } finally {
           
        }
    }

    private static void sendList(InetAddress address, int cliPort) {

        String msgOut = ServerResponeClient.RESPONE_LIST.name() + "~" + datafile ;
        outPkt = new DatagramPacket(msgOut.getBytes(), msgOut.length(),address,cliPort);
        try {
            dgramSocket.send(outPkt);
        } catch (IOException ex) {
            Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    private static void sendItemMail(String username , String subject, InetAddress address, int cliPort){
        BufferedReader br = null;
        try {
            File file = new File(dir+username+"/"+subject+".txt");
            br = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8);
            String data = "" ;
            String line = "";
            
            do {
                line = br.readLine();
                if(line != null) data += "//"+line;
            } while (line != null);
            br.close();
            
            String msgOut = ServerResponeClient.RESPONE_ITEM_MAIL.name() + "~" + data ;
           
            outPkt = new DatagramPacket(msgOut.getBytes(), msgOut.length(),address,cliPort); 
            dgramSocket.send(outPkt);
            
            
        } catch (IOException ex) {
            Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private static void sendListMail(InetAddress address, int cliPort, String username) {
        
        BufferedReader br = null;
        try {
            File file = new File(dir+username+"/listMail.txt");
            br = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8);
            String dataListMail =  br.readLine()  ;
            br.close();
            
            String msgOut = ServerResponeClient.RESPONE_LIST_MAIL.name() + "~" + dataListMail ;
            outPkt = new DatagramPacket(msgOut.getBytes(), msgOut.length(),address,cliPort); 
            try {
                dgramSocket.send(outPkt);
            } catch (IOException ex) {
                Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    private static void run() {
        String data;
        try {
            do {
                
                inPkt = new DatagramPacket(buffer,buffer.length);
                dgramSocket.receive(inPkt);
                
                InetAddress cliAddress = inPkt.getAddress();
                int cliPort = inPkt.getPort();
                data = new String(inPkt.getData(),0,inPkt.getLength());
                String dataHandle[] = data.split("~");
                
                if(dataHandle[0].equals(ClientRequestServer.REQUEST_LOGIN.name())){
                    login(cliAddress, cliPort, dataHandle[1],  dataHandle[2]);
                }
                else if(dataHandle[0].equals(ClientRequestServer.REQUEST_LIST.name())){
                    sendList(cliAddress, cliPort);
                }
                else if(dataHandle[0].equals(ClientRequestServer.REQUEST_LIST_MAIL.name())) {
                    sendListMail(cliAddress, cliPort, dataHandle[1]);
                }
                else if(dataHandle[0].equals(ClientRequestServer.REQUEST_ITEM_MAIL.name())) {
                    sendItemMail(dataHandle[1] , dataHandle[2] , cliAddress, cliPort) ;
                }
                else if(dataHandle[0].equals(ClientRequestServer.REQUEST_SEND_MAIL.name())){
                    
                    String email[] = dataHandle[1].split("//");
                    appendContent(email[1] , email[0] , email[2], email[3]) ;
                    
                    try {
                        File file = new File(dir+email[0]+"/data.txt");
                        BufferedReader br = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8);
                        ArrayList<String> datas = new ArrayList<>();
               
                        String line = "";
                         do {                    
                            line = br.readLine();
                            if(line != null) datas.add(line);
                        } while (line != null);
                        br.close();
                    
                        InetAddress address = null ;
                        for (InetAddress item : listAddress) {
                            if(item.toString().equals(datas.get(0))) {
                                address = item ;
                             break ;
                            }
                        }
                        sendListMail(address , Integer.parseInt(datas.get(1)) , email[0]);
                    } 
                    catch (Exception e) {
                    
                    }
                }
                else if(dataHandle[0].equals(ServerResponeClient.SERVER_LOGIN.name())) {
                    hostServer = cliAddress ;
                    portServer = cliPort ; 
                }
                else if(dataHandle[0].equals(ServerResponeClient.SERVER_REQUEST_LIST_MAIL.name())) {
                    sendListMail(hostServer, portServer, dataHandle[1]);
                }
                else if(dataHandle[0].equals(ServerResponeClient.SERVER_REQUEST_ITEM_MAIL.name())) {
                    sendItemMail(dataHandle[1] , dataHandle[2] , hostServer, portServer) ;
                }
                

            }while (true) ;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            dgramSocket.close();
        }
    }
    
}
