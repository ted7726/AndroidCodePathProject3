package com.codepath.apps.twitterapp.models;

import com.codepath.apps.twitterapp.CallBack;
import com.codepath.apps.twitterapp.TwitterApplication;
import com.codepath.apps.twitterapp.TwitterClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by wilsonsu on 2/27/16.
 */
public class MessagesController {
    private static MessagesController mInstance;
    private Queue<CallBack> tasksQueue;
    private ArrayList<Message> messages;
    private TwitterClient client;
    private long currentUserId;

    public static synchronized MessagesController getInstance() {
        if (mInstance == null) {
            mInstance = new MessagesController();
        }
        return mInstance;
    }

    private MessagesController() {
        messages = new ArrayList<>();
        client = TwitterApplication.getRestClient(); // singleton client
        tasksQueue = new LinkedList<>();
        currentUserId = CurrentUser.user.userId;
    }

    public void sendMessage(String text, User user, final CallBack callBack) {
        client.sendMessage(text, user, new CallBack(){
            @Override
            public void messageCallBack(Message message) {
                messages.add(message);
                callBack.messageCallBack(message);
            }
        });
    }

    public void getMessages(long userId, CallBack callBack) {

        int len = messages.size();
        if (len==0) {
            fetchMessages();
            tasksQueue.add(new GettingMessageTask(userId, callBack));
            processTasksQueue();
            return;
        }
        ArrayList<Message> userMessages = new ArrayList<>();
        for (int i=0;i<len;++i) {
            Message message = messages.get(i);
            if (message.senderId == userId || message.recipientId == userId) {
                userMessages.add(message);
            }
        }
        callBack.messagesCallBack(userMessages);
    }

    private void fetchMessages() {
        CallBack taskReceive = new CallBack() {
            @Override
            public void task() {
                client.getMessages(new CallBack() {
                    @Override
                    public void messagesCallBack(ArrayList<Message> messages) {
                        handleNewMessages(messages);
                        processTasksQueue();
                    }
                });
            }
        };

        CallBack taskSent = new CallBack() {
            @Override
            public void task() {
                client.getMessagesSent(new CallBack() {
                    @Override
                    public void messagesCallBack(ArrayList<Message> messages) {
                        handleNewMessages(messages);
                        processTasksQueue();
                    }
                });
            }
        };

        tasksQueue.add(taskReceive);
        tasksQueue.add(taskSent);
    }



    private void handleNewMessages(ArrayList<Message> returnedMessages) {
        messages.addAll(returnedMessages);
        Collections.sort(messages, new Comparator<Message>() {
            @Override
            public int compare(Message lhs, Message rhs) {
                return lhs.time.after(rhs.time) ? 1 : -1;
            }
        });
    }

    // assume the last one is the callback
    private void processTasksQueue() {
        if (tasksQueue.size()>0) {
            CallBack task = tasksQueue.remove();
            if (task instanceof GettingMessageTask) {
                GettingMessageTask gettingMessageTask = (GettingMessageTask)task;
                getMessages(gettingMessageTask.userId, gettingMessageTask.callBack);
            }else {
                task.task();
            }
        }
    }

    private static class GettingMessageTask extends CallBack {
        private long userId;
        private CallBack callBack;

        public GettingMessageTask(long userId, CallBack callBack) {
            this.userId = userId;
            this.callBack = callBack;
        }
    }


}
