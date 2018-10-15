package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.util.ArrayList;

public class Controller {

    public TextField textGetAge;
    public TextField textGetPhone;
    public TextField textGetName;
    public TextField textGetGender;
    public ListView<Friend> friendList = new ListView<>();
    public Label lblName;
    public Label lblGender;
    public Label lblPhone;
    public Label lblAge;
    public Button btnRemove;
    public Button btnAddFriend;
    public Label lblViewName;
    public Label lblViewGender;
    public Label lblViewAge;
    public Label lblViewPhone;
    public Label lblInfo;
    public Button btnSaveFriend;
    public TextField textCreateGroup;
    public ObservableList<String> groupList = FXCollections.observableArrayList();
    public ChoiceBox choiceAddGroup;
    public ChoiceBox choiceLoadGroup;
    public Button btnAddToGroup;
    public Button btnLoadGroup;

    //Requires: nothing
    //Modifies: this
    //Effects: Runs on startup, initializes group lists in dropdown menus
    @FXML
    private void initialize() throws IOException {
        ArrayList<String> list = existingGroups();
        for (String s : list){
            groupList.add(s);
        }
        choiceAddGroup.setItems(groupList);
        choiceLoadGroup.setItems(groupList);

        if (groupList.size() != 0){
            btnLoadGroup.setDisable(false);
        }
    }

    //Requires: nothing
    //Modifies:this
    //Effects: Adds friend to list and clears text fields
    public void addFriend(ActionEvent actionEvent) throws IOException {
        String name = textGetName.getText();
        int age = Integer.parseInt(textGetAge.getText());
        long num = Long.parseLong(textGetPhone.getText());
        String gender = textGetGender.getText();
        Friend temp = new Friend(name,gender,age,num);
        friendList.getItems().add(temp);
        textGetName.clear();
        textGetPhone.clear();
        textGetAge.clear();
        textGetGender.clear();
        btnAddFriend.setDisable(true);

    }

    //Requires: nothing
    //Modifies:this
    //Effects: Displays friends attributes
    public void displayFriend(MouseEvent mouseEvent) throws IOException {
        Friend temp;
        temp = friendList.getSelectionModel().getSelectedItem();
        lblAge.setText(Integer.toString(temp.getAge()));
        lblName.setText(temp.name);
        lblPhone.setText(Long.toString(temp.getPhoneNumber()));
        lblGender.setText(temp.gender);
        btnAddToGroup.setDisable(groupsExist());
        btnRemove.setDisable(false);
        enableAttributes(true);
        btnSaveFriend.setDisable(friendInGroup(temp,"friends.txt"));



    }

    //Requires: nothing
    //Modifies: this
    //Effects: Removes friend from list
    public void removeFriend(ActionEvent actionEvent) {
        Friend temp;
        temp = friendList.getSelectionModel().getSelectedItem();
        friendList.getItems().remove(temp);
        lblName.setText("");
        lblAge.setText("");
        lblPhone.setText("");
        lblGender.setText("");
        btnRemove.setDisable(true);
        enableAttributes(false);
        btnSaveFriend.setDisable(true);
        btnAddToGroup.setDisable(true);

    }

    //Requires: nothing
    //Modifies: this
    //Effects: Checks if all fields have input before allowing user to add friend
    public void keyReleased() {
        boolean isFull = (textGetAge.getText().isEmpty() || textGetGender.getText().isEmpty() || textGetPhone.getText().isEmpty() || textGetName.getText().isEmpty());
        btnAddFriend.setDisable(isFull);
    }

    //Requires: nothing
    //Modifies: this
    //Effects: Hides info categories unless a friend is selected
    private void enableAttributes(boolean status){
        btnRemove.setVisible(status);
        lblViewAge.setVisible(status);
        lblViewGender.setVisible(status);
        lblViewName.setVisible(status);
        lblViewName.setVisible(status);
        lblViewPhone.setVisible(status);
        lblInfo.setVisible(status);

    }

    //Requires: nothing
    //Modifies: this
    //Effects: Adds selected friend to selected group
    public void addToGroup(ActionEvent actionEvent) throws IOException {
        Friend temp;
        temp = friendList.getSelectionModel().getSelectedItem();
        String group = choiceAddGroup.getValue() + ".txt";
        if (!friendInGroup(temp,group)) {
            temp.writeToFile(group);
        }
    }

    //Requires: nothing
    //Modifies:this
    //Effects: Loads all saved friends from "friends.txt"
    public void loadAll(ActionEvent actionEvent) throws IOException {
        System.out.println("Running loadall");
        friendList.getItems().clear();
        CreateFriend load = new CreateFriend();
        ArrayList<Friend> friends = load.createAllFriends("friends.txt");
        for (Friend f : friends){
          friendList.getItems().add(f);
        }
    }

    //Requires: nothing
    //Modifies: this
    //Effects: Loads all friends saved in selected group
    public void loadGroup(ActionEvent actionEvent) throws IOException {
        friendList.getItems().clear();
        String group = choiceLoadGroup.getValue() + ".txt";
        System.out.println(group);
        CreateFriend addGroup = new CreateFriend();
        ArrayList<Friend> friends = addGroup.createAllFriends(group);
        for (Friend f: friends) {
            friendList.getItems().add(f);
        }


    }

    //Requires: nothing
    //Modifies: this
    //Effects: Saves friend to "friends.txt"
    public void saveFriend(ActionEvent actionEvent) throws IOException{
        Friend temp;
        temp = friendList.getSelectionModel().getSelectedItem();
        temp.writeToFile("friends.txt");
        btnSaveFriend.setDisable(true);

    }

    //Requires: nothing
    //Modifies: this
    //Effects: Checks if a given friend is in a given group, returns true if they are
    public boolean friendInGroup(Friend friend, String fileName) throws IOException {
        CreateFriend check = new CreateFriend();
        ArrayList<Friend> friends1 = check.createAllFriends(fileName);
        for (Friend f : friends1){
            if (friend.isSame(f)) {
                return true;
            }
        }
        return false;

    }

    //Requires:nothing
    //Modifies: this
    //Effects: Creates new group
    public void createGroup(ActionEvent actionEvent) throws IOException {
        String fileName = textCreateGroup.getText() + ".txt";
        PrintWriter writer = new PrintWriter(fileName);
        groupList.add(textCreateGroup.getText());
        addToGroupFile(textCreateGroup.getText());
        textCreateGroup.clear();
        choiceAddGroup.setItems(groupList);
        choiceLoadGroup.setItems(groupList);
        btnLoadGroup.setDisable(false);
        btnAddToGroup.setDisable(false);
        writer.close();
    }

    //Requires: nothing
    //Modifies: this
    //Effects: Finds existing group files that have been saved and adds them to new program iteration's groupList
    public ArrayList<String> existingGroups() throws IOException {
        ArrayList<String> list = new ArrayList<>();
        FileReader fr = new FileReader("groups.txt");
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) {
            list.add(line);
        }
        return list;
    }

    //Requires: nothing
    //Modifies: this
    //Effects: Adds friend to selected group
    public void addToGroupFile(String group) throws IOException {
        FileWriter fw = new FileWriter("groups.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(group+"\r");
        bw.close();

    }

    //Requires: nothing
    //Modifies: this
    //Effects: Checks if any groups have been made yet, returns true if they have
    public boolean groupsExist(){
        return groupList.size() == 0;
    }
}
