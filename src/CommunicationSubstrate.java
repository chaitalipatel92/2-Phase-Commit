/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Chaitali
 */
public class CommunicationSubstrate {

    private SenderThread sender;
    private ReceiverThread receiver;
    private ServerSocket serverSocket;
    private Socket clientSocket[];
    private DataInputStream in[];
    private DataOutputStream out[];
    private boolean isServerSubstrate;

    public void sendMessage() {
        //System.out.println("Sending...");
        sender.start();
        //System.out.println("Sent");
    }

    public void receiveMessage() {
        receiver.start();
    }

    private class SenderThread extends Thread {

        ArrayList<String> bufferToSendFrom;

        public SenderThread(ArrayList<String> bufferToSendFrom) {
            this.bufferToSendFrom = bufferToSendFrom;
        }

        /*
         Sender loop
         */
        @Override
        public void run() {
            while (true) {
            if (!bufferToSendFrom.isEmpty()) {
                System.out.println("OutputBuffer " +bufferToSendFrom);
                String message = bufferToSendFrom.get(0);
                try {
                    if (isServerSubstrate) {
//                        System.out.println(message + "Sent");
                        out[0].writeUTF(message);
                        System.out.println(message + " Sent to Coordinator");
                    } else {
                        System.out.println(message);
                        out[0].writeUTF(message);
                        System.out.println(message + " Sent to Hotel");
                        out[1].writeUTF(message);
                        System.out.println(message + " Sent to Concert");
                    }
                    bufferToSendFrom.remove(0);
                    System.out.println("Remaining Requests "+bufferToSendFrom);
                } catch (IOException ex) {
                    Logger.getLogger(CommunicationSubstrate.class.getName()).log(Level.SEVERE, null, ex);
                }
                catch(NullPointerException npe){
                    
                }

            }
            }
        }
    }

    private class ReceiverThread extends Thread {

        ArrayList<String> bufferToReceiveIn;

        public ReceiverThread(ArrayList<String> bufferToReceiveIn) {
            this.bufferToReceiveIn = bufferToReceiveIn;
        }

        /*
         Receiver loop
         */
        @Override
        public void run() {
            try {
                if (isServerSubstrate) {
//                    clientSocket[0] = serverSocket.accept();
//                    in[0] = new DataInputStream(clientSocket[0].getInputStream());
//                    out[0] = new DataOutputStream(clientSocket[0].getOutputStream());

                    while (true) {
                        String messageFromServer1 = in[0].readUTF();
                        //String messageFromServer2 = in[1].readUTF();
                        
                        bufferToReceiveIn.add(messageFromServer1);
                        //bufferToReceiveIn.add(messageFromServer2);
                        System.out.println(messageFromServer1 + " Received from Coordinator");
                        System.out.println("InputBuffer " +bufferToReceiveIn);
                    }
                } else {
//                    in[0] = new DataInputStream(clientSocket[0].getInputStream());
//                    out[0] = new DataOutputStream(clientSocket[0].getOutputStream());
//                    in[1] = new DataInputStream(clientSocket[1].getInputStream());
//                    out[1] = new DataOutputStream(clientSocket[1].getOutputStream());

                    while (true) {
                        String messageFromServer1 = in[0].readUTF();
                        String messageFromServer2 = in[1].readUTF();

                        bufferToReceiveIn.add(messageFromServer1);
                        bufferToReceiveIn.add(messageFromServer2);
                        System.out.println(messageFromServer1 + " Received from Hotel");
                        System.out.println(messageFromServer2 + " Received from Concert");
                        System.out.println("InputBuffer " +bufferToReceiveIn);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(CommunicationSubstrate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public CommunicationSubstrate(boolean isServerSubstrate, int port[],
            ArrayList<String> entityOPBuffer, ArrayList<String> entityIPBuffer) {
        sender = new SenderThread(entityOPBuffer);
        receiver = new ReceiverThread(entityIPBuffer);
        this.isServerSubstrate = isServerSubstrate;

        try {
            if (isServerSubstrate) {
                serverSocket = new ServerSocket(port[0]);
                clientSocket = new Socket[1];
                clientSocket[0] = serverSocket.accept();
                in = new DataInputStream[1];
                out = new DataOutputStream[1];
                in[0] = new DataInputStream(clientSocket[0].getInputStream());
                out[0] = new DataOutputStream(clientSocket[0].getOutputStream());
            } else {
                clientSocket = new Socket[2];
                clientSocket[0] = new Socket("localhost", port[0]);
                clientSocket[1] = new Socket("localhost", port[1]);
                in = new DataInputStream[2];
                out = new DataOutputStream[2];

                in[0] = new DataInputStream(clientSocket[0].getInputStream());
                out[0] = new DataOutputStream(clientSocket[0].getOutputStream());
                in[1] = new DataInputStream(clientSocket[1].getInputStream());
                out[1] = new DataOutputStream(clientSocket[1].getOutputStream());
            }

        } catch (IOException ex) {
            Logger.getLogger(Entity.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
