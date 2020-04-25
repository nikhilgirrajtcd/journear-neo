package com.journear.app.core.services;

import com.journear.app.core.entities.User;

import org.apache.commons.lang3.StringUtils;

public class JnMessage {

    private String senderName;
    private String senderGender;
    private String senderRating;
    private JnMessageSet messageFlag;
    private String messageId;
    private long timeStamp;
    /**
     * Phone number if available
     */
    private String phoneNumber = "";

    public String getSubject() {
        return subject;
    }

    /**
     * This field would contain the ID of the ride that this conversation is about.
     */
    private String subject;

    /**
     * MAC Address of the senderId.
     */
    private String senderId;

    public static JnMessage createFromReconstructableString(String reconstructableString) {
        if (StringUtils.countMatches(reconstructableString, "|") == 5) {
            String[] parts = StringUtils.split(reconstructableString, '|');
            // Original order messageId, messageFlag.name(), phoneNumber, subject, senderId, senderName, senderGender, senderRating, timeStamp);
            return new JnMessage(parts[0], JnMessageSet.valueOf(parts[1]), parts[2], parts[3], parts[4], parts[5], parts[6], parts [7], Long.parseLong(parts[8]));
        }
        throw new IllegalArgumentException("Cannot create JnMessage from String: " + reconstructableString);
    }

    public JnMessageSet getMessageFlag() {
        return messageFlag;
    }

    public JnMessage(String messageId, JnMessageSet messageFlag, String phoneNumber, String subject, String senderId, String senderName, String senderGender, String rating, long timeStamp) {
        this.messageFlag = messageFlag;
        this.messageId = messageId;
        this.timeStamp = timeStamp;
        this.phoneNumber = phoneNumber;
        this.subject = subject;
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderGender = senderGender;
        this.senderRating = rating;
    }

    public JnMessage(String messageId, JnMessageSet messageFlag, String phoneNumber, String subject, User sender) {
        this(messageId, messageFlag, phoneNumber, subject, sender.getUserId(), sender.getName(), sender.getGender(), sender.getRating(), System.currentTimeMillis());
    }

    // {C1=null:37:1|RequestedToJoinWithContact|||37}
    public String toReconstructableString() {
        return StringUtils.joinWith("|", messageId, messageFlag.name(), phoneNumber, subject, senderId, senderName, senderGender, senderRating, timeStamp);
    }

    public String getMessageId() {
        return messageId;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getSender() {
        return senderId;
    }
}
