/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Chaitali
 */
public class Coordinator extends Entity {

    public Coordinator(int[] socketPorts) {
        super(false, socketPorts);
        threadB = new TaskThread(this) {
            @Override
            public void run() {
                try {
                    System.out.println("In Task Method of Coordinator");

                    BufferedReader br = null;
                    br = new BufferedReader(new FileReader("coord.txt"));
                    String hotelinfo = br.readLine();
                    String[] hotel = hotelinfo.split(" ");
                    String hotelIP = hotel[0];
//                    System.out.println("Hotel IP : " + hotelIP);
                    int hotelport = Integer.parseInt(hotel[1]);
//                    System.out.println("Hotel Port# : " + hotelport);

                    String concertinfo = br.readLine();
                    String[] concert = concertinfo.split(" ");
                    String concertIP = concert[0];
//                    System.out.println("Concert IP : " + concertIP);
                    int concertport = Integer.parseInt(concert[1]);
//                    System.out.println("Concert Port# : " + concertport);

                    String bookingfilename = br.readLine();
//                    System.out.println("BookingFile Name : " + bookingfilename);

                    BufferedReader br1 = new BufferedReader(new FileReader(bookingfilename));
//                    requestlines = new ArrayList<>();
                    String str;
                    while ((str = br1.readLine()) != null) {
                        str = str.replace(" [", " ");
                        str = str.replace("]", "");
//                        requestlines.add(str);
                        underlyingEntity.outputBuffer.add(str);
                    }
                    underlyingEntity.startSendingMessages();
//                    underlyingEntity.outputBuffer.add("Hi from Coordinator");

                    Thread checkresponses = new Thread(new CheckInputBuffer(underlyingEntity));
                    checkresponses.start();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    underlyingEntity.startReceivingIncomingMessages();

//                    CheckInputBuffer checkresponses = new CheckInputBuffer();
//                    checkresponses.start();
//                    System.out.println("Coordinator InputBuffer : " + underlyingEntity.inputBuffer);
//                    System.out.println("Coordinator OutputBuffer : " + underlyingEntity.outputBuffer);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Coordinator.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Coordinator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
    }

    public static void main(String[] args) {
        //coordinator

        Coordinator coordinator = new Coordinator(new int[]{1111, 2222});
        coordinator.startThreadBTask();
    }

    private static class CheckInputBuffer implements Runnable {

        Entity underlyinEntity;

        private CheckInputBuffer(Entity underlyingEntity) {
            this.underlyinEntity = underlyingEntity;
        }

        @Override
        public void run() {
            while (true) {
                if (!underlyinEntity.inputBuffer.isEmpty()) {
                    System.out.println(underlyinEntity.inputBuffer);
//                    if (underlyinEntity.inputBuffer.get(0).equalsIgnoreCase("yes")
//                            && underlyinEntity.inputBuffer.get(1).equalsIgnoreCase("yes")) {
//                        System.out.println("Got both responses YES");
//                        underlyinEntity.inputBuffer.clear();
////                        underlyinEntity.outputBuffer.remove(0);
//                        break;
//                        
//                    } else if (underlyinEntity.inputBuffer.get(0).equalsIgnoreCase("no")
//                            || underlyinEntity.inputBuffer.get(1).equalsIgnoreCase("no")) {
//                        System.out.println("One or both the responses are NO");
//                    }
//                } else {
//                    System.out.println("Waiting for responses from both");
//                }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Coordinator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
}
