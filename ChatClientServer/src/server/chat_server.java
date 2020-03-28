/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 *Press 'Ctrl' + 'C' to terminate the program
 * 
 * @author Throw8008s
 */
public class chat_server {
    private int port;
    private Set<String> userNames = new HashSet<>();
    private Set<UserThread> userThreads = new HashSet<>();
    
    public chat_server(int port){
        this.port = port;
    }
    
    public void execute(){
        try(ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Chat Server is listening on port" + port);
            
            while (true){
                Socket socket = serverSocket.accept();
                System.out.println("New User connected");
                
                UserThread newUser = new UserThread(socket, this);
                UserThread.add(newUser);
                newUser.start();
            }
        }
        catch (IOException ex){
            System.out.println("Error in the server" + ex.getMessage());
            ex.printStackTrace();
        }
    }
    public static void main(String[] args){
        if(args.length < 1){
            System.out.println("Syntax: Java Chat Server <port-number>");
            System.exit(0);
        }
        int port = Integer.parseInt(args[0]);
        
        chat_server server = new chat_server(port);
        server.execute();
    }
    
    /* 
    *
    *Delivers a message from one user to other
    *(Broadcasting)
    */
    void broadcast(String message, UserThread excludeUser){
        for (UserThread aUser : userThreads){
            if(aUser != excludeUser){
                aUser.sendMessage(message);
            }
        }
    }
   /**
    * Stores usernames of the newly connected client
    */
    void addUserName(String userName){
        userNames.add(userName);
    }
    /**
     * When a client is disconnected, removes the associated username and UserThread
     */
    void removeUser(String userName, UserThread aUser){
        boolean removed = userNames.remove(userName);
        if(removed){
            userThreads.remove(aUser);
            System.out.println("The User "+userName + " left");
        }
    }
    
    Set<String> getUserNames(){
        return this.userNames;
    }
    /**
     * Returns true if there are other users connected
     * and not count the currently connected users
     */
    boolean hasUsers(){
        return !this.userNames.isEmpty();
    }
    
    
}


