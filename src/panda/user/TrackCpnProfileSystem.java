/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package panda.user;

/**
 *
 * @author Khoa
 */
public class TrackCpnProfileSystem extends Thread {
    public CpnProfile cpnProfile;
    @Override
    public void run(){
        while(true){
            System.out.println("Tracking");

            cpnProfile.reloadTable();
            try{
                Thread.sleep(3000);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public TrackCpnProfileSystem(){
        cpnProfile = new CpnProfile();
    }
}
