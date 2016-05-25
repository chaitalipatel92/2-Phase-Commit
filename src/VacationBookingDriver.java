/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Chaitali
 */
public class VacationBookingDriver {

    public static void main(String[] args) {
        //our servers
        Hotel hotelServer = new Hotel(1111);
        Concert concertServer = new Concert(2222);

       
//        hotelServer.startReceivingIncomingMessages();
//        concertServer.startReceivingIncomingMessages();
        hotelServer.startThreadBTask();
        concertServer.startThreadBTask();
        
        //coordinator
        Coordinator coordinator = new Coordinator(new int[]{1111, 2222});
//        coordinator.startReceivingIncomingMessages();
        coordinator.startThreadBTask();

//        hotelServer.outputBuffer.add("Hi from hotel");
//        hotelServer.startSendingMessages();
//
//        concertServer.outputBuffer.add("Hi from concert");
//        concertServer.startSendingMessages();
//
//        coordinator.outputBuffer.add("Hi from coordinator");
//        coordinator.startSendingMessages();
//
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(VacationBookingDriver.class.getName()).log(Level.SEVERE, null, ex);
        }
//
//        System.out.println("Hotel received: " + hotelServer.inputBuffer);
////        hotelServer.showIPBufferContents();
//        System.out.println("Concert received: " + concertServer.inputBuffer);
////        concertServer.showIPBufferContents();
//        System.out.println("Coordinator received: " + coordinator.inputBuffer);
////        coordinator.showIPBufferContents();

    }
}
