/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;

/**
 *
 * @author Chaitali
 */
public abstract class Entity {

    ArrayList<String> inputBuffer;
    ArrayList<String> outputBuffer;
    protected FailureRecoveryThread threadA;
    protected TaskThread threadB;
    protected CommunicationSubstrate communicationSubstrate;

    public ArrayList<String> getInputBuffer() {
        return inputBuffer;
    }

    public ArrayList<String> getOutputBuffer() {
        return outputBuffer;
    }

//    public void showIPBufferContents() {
//        System.out.println(inputBuffer);
//    }
//    
//    public void showOPBufferContents() {
//        System.out.println(outputBuffer);
//    }
//    public void addToOutputBuffer(String message) {
//        outputBuffer.add(message);
//    }
//    
//    public void addToInputBuffer(String message) {
//        inputBuffer.add(message);
//    }
//    
//    public String removeFromInputBuffer() {
//        return inputBuffer.remove(0);
//    }
//    
//    public String removeFromOutputBuffer() {
//        return outputBuffer.remove(0);
//    }
    public void startSendingMessages() {
        //System.out.println("Attempting to send from substrate");
        communicationSubstrate.sendMessage();
        //System.out.println("Sent from substrate");        
    }

    public void startReceivingIncomingMessages() {
        communicationSubstrate.receiveMessage();
    }
    
    public void openConnection(){
        
    }
            

    public CommunicationSubstrate getCommunicationSubstrate() {
        return communicationSubstrate;
    }

    public Entity(boolean isParticipant, int socketPorts[]) {
        inputBuffer = new ArrayList<>();
        outputBuffer = new ArrayList<>();
        threadA = new FailureRecoveryThread();

        communicationSubstrate = new CommunicationSubstrate(isParticipant, socketPorts, outputBuffer, inputBuffer);

    }

    protected abstract class TaskThread extends Thread {

        Entity underlyingEntity;

        public TaskThread(Entity e) {
            underlyingEntity = e;
        }

        @Override
        public abstract void run();
    }

    protected void startThreadATask() {
        threadA.start();
    }

    protected void startThreadBTask() {
        threadB.start();
    }

    protected class FailureRecoveryThread extends Thread {

        public void run() {

        }
    }

}
