package sample;



import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CreateFriend {

    private static String name;
    private static String gender;
    private static int age;
    private static long phone;
    private static FileReader fr;
    private static BufferedReader br;
    private  ArrayList<Friend> friends = new ArrayList<>();

    //Requires: valid filename
    //Modifies: this
    //Effects: Fills friends ArrayList with all friend objects in given txt file
    public  ArrayList createAllFriends(String fileName) throws IOException {
        System.out.println(fileName);
        fr = new FileReader(fileName);
        br = new BufferedReader(fr);
        String line;
        String friendString = "";
        while ((line = br.readLine()) != null){
            if (!line.equals(";")){
                friendString += line;
            }
            else{
                parseFriend(friendString);
                friendString = "";
            }
        }
        return friends;
    }

    //Requires: valid string
    //Modifies:this
    //Effects: Turns string from createAllFriends into Friend object and adds to friends ArrayList
    private  void parseFriend(String string){
        int pos = 0;
        int count = 0;
        String name = "";
        String gender = "";
        int age = 0;
        long phone = 0;

        for (int i = 0; i < string.length();i++) {
            if (string.substring(i, i + 1).equals(",")) {
                if (count == 0) {
                    count += 1;
                    name = string.substring(0, i);
                    pos = i+1;
                } else if (count == 1) {
                    count += 1;
                    gender = string.substring(pos, i);
                    pos = i+1;
                } else if (count == 2) {
                    count += 1;
                    age = Integer.parseInt(string.substring(pos, i));
                    pos = i+1;

                } else if (count == 3) {
                    count += 1;
                    phone = Long.parseLong(string.substring(pos, i));
                }
            }
        }
        friends.add(new Friend(name,gender,age,phone));



    }

}
