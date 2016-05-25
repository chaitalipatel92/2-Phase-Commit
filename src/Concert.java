/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Chaitali
 */
public class Concert extends Entity{

    public Concert(int port) {
        super(true, new int[]{port});
        threadB = new TaskThread(this) {

            @Override
            public void run() {
                try {
                    System.out.println("In Task Method of oncert");

                    BufferedReader br = null;
                    br = new BufferedReader(new FileReader("hotel.txt"));
                    String coordinfo = br.readLine();
                    String[] coord = coordinfo.split(" ");
                    String coordIP = coord[0];
//                    System.out.println("Coordinator IP : " + coordIP);
                    int coordport = Integer.parseInt(coord[1]);
//                    System.out.println("Coordinator Port# : " + coordport);

                    HashMap<Integer, Integer> hotelrooms = new HashMap<Integer, Integer>();
                    String currentLine;
                    boolean success = false;
                    int bookingID;
                    int requestedRooms;
                    while ((currentLine = br.readLine()) != null) {
                        String[] splited = currentLine.split("\\s+");
                        hotelrooms.put(Integer.parseInt(splited[0]), Integer.parseInt(splited[1]));
                    }
                    System.out.println("Concert Details : " + hotelrooms.entrySet());
                    underlyingEntity.startReceivingIncomingMessages();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                 //                    if (!underlyingEntity.inputBuffer.isEmpty()) {
                    for (int i = 0; i < underlyingEntity.inputBuffer.size(); i++) {
                     
                        String ProcessingRequest = underlyingEntity.inputBuffer.get(i);
                        System.out.println("Processing Request : " + ProcessingRequest);

                        String[] splitRequest = ProcessingRequest.split("\\s+");
                        bookingID = Integer.parseInt(splitRequest[0]);
                        requestedRooms = Integer.parseInt(splitRequest[1]);

                        for (int j = 2; j < splitRequest.length; j++) {
                            int dateRequested = Integer.parseInt(splitRequest[j]);
                            if (requestedRooms <= hotelrooms.get(dateRequested)) {
                                int remaining_rooms = hotelrooms.get(dateRequested) - requestedRooms;
                                hotelrooms.replace(dateRequested, remaining_rooms);
                                success=true;
                                
                            } else {
                                success = false;
                                
                            }
                        }
                        
                        if (success){
                            underlyingEntity.outputBuffer.add(bookingID + " yes");
                        }
                        else{
                            underlyingEntity.outputBuffer.add(bookingID + " no");
                        }                        
//                        underlyingEntity.inputBuffer.remove(0);
                    }
                    underlyingEntity.startSendingMessages();

//                    System.out.println("Hotel InputBuffer : " + underlyingEntity.inputBuffer);
//                    System.out.println("Hotel OutputBuffer : " + underlyingEntity.outputBuffer);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Hotel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Hotel.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        };
    }
    
    public static void main(String[] args) {
        Concert concertServer = new Concert(2222);
        concertServer.startThreadBTask();
   }
    
}
