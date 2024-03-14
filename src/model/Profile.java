/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Khoa
 */
public class Profile {
    public String profile;
    public String resource_name;
    public String limit;

    public Profile(String profile, String resource_name, String limit) {
        this.profile = profile;
        this.resource_name = resource_name;
        this.limit = limit;
    }

    public Profile() {
    }
    
    
}
