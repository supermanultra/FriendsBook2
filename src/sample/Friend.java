package sample;

import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;

public class Friend {
    public String name;
    public String gender;
    private int age;

    //Getter
    public int getAge() {
        return age;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    private long phoneNumber;

    //Requires: Parameters meet req
    //Modifies: this
    //Effects: Builds friend object with given parameters
    Friend(String n, String gender, int age, long phone){
        name = n;
        this.gender = gender;
        this.age = age;
        phoneNumber = phone;
    }

    //Requires: nothing
    //Modifies: this
    //Effects: Prints name when object is printed
    public String toString(){
        return name;
    }

    //Requires: valid txt file
    //Modifies: this
    //Effects: Adds friend to given file
    public void writeToFile(String fileName) throws IOException {
        FileWriter fw = new FileWriter(fileName,true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(name + ",\r");
        bw.write(gender + ",\r");
        bw.write(Integer.toString(age) + ",\r");
        bw.write(Long.toString(phoneNumber) + ",\r");
        bw.write(";\r");
        bw.close();

    }

    //Requires: nothing
    //Modifies: this
    //Effects: Checks if two friends objects are the same
    public boolean isSame(Friend friend2) {
        return ((this.name.equals(friend2.name)) && (this.age == friend2.age) && (this.gender.equals(friend2.gender)) && (this.phoneNumber == friend2.phoneNumber));
    }


}
