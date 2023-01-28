package com.driver;

import java.util.*;

public class WhatsappRepository {

    private HashMap<String, User> users;
    private HashSet<String> userMobile;

    private HashMap<Group, User> adminMap;
    private HashMap<Group, List<User>> groupAndListOfUsers;
    private HashMap<Group, List<Message>> groupAndListOfMessage;

    private HashMap<Message, User> senderMap;

    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        users = new HashMap<>();
        userMobile = new HashSet<>();

        adminMap = new HashMap<>();
        groupAndListOfUsers = new HashMap<>();
        groupAndListOfMessage = new HashMap<>();

        senderMap = new HashMap<>();

        customGroupCount = 0;
        messageId = 0;

    }

    public String createUser(String name, String mobile) throws Exception {
        if(userMobile.contains(mobile)){
            throw new Exception("User already exists");
        }

        userMobile.add(mobile);
        users.put(mobile, new User(name, mobile));

        return "SUCCESS";
    }

    public Group createGroup(List<User> users) {
        Group group;
        if(users.size() == 2){
            group = new Group(users.get(1).getName(), 2);
        }else{
            customGroupCount++;
            group = new Group("Group " + customGroupCount, users.size());
            adminMap.put(group, users.get(0));
        }
        groupAndListOfUsers.put(group, users);
        return group;
    }

    public int createMessage(String content) {
        messageId++;
        Message message = new Message(messageId, content, new Date());
        return messageId;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception {
        if(!groupAndListOfUsers.containsKey(group)){
            throw new Exception("Group does not exist");
        }

        if(!groupAndListOfUsers.get(group).contains(sender)){
            throw new Exception("You are not allowed to send message");
        }

        List<Message> messageList = new ArrayList<>();

        if(groupAndListOfMessage.containsKey(group)){
            messageList = groupAndListOfMessage.get(group);
        }

        messageList.add(message);
        groupAndListOfMessage.put(group, messageList);

        return messageList.size();
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception {
        if(!groupAndListOfUsers.containsKey(group)){
            throw new Exception("Group does not exist");
        }

        if(!adminMap.get(group).equals(approver)){
            throw new Exception("Approver does not exist");
        }

        if(!groupAndListOfUsers.get(group).contains(user)){
            throw new Exception("User is not a participant");
        }

        adminMap.put(group, user);

        return "SUCCESS";
    }

    public int removeUser(User user) {
        return 1;
    }

    public String findMessage(Date start, Date end, int k) {
        return "SUCCESS";
    }
}
