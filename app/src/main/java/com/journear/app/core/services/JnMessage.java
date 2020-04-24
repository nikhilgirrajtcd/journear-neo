package com.journear.app.core.services;
import org.apache.commons.lang3.StringUtils;

public class JnMessage {

    private JnMessageSet messageFlag;
    private String messageId;

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
     *  MAC Address of the sender.
     */
    private String sender;

    public static JnMessage createFromReconstructableString(String reconstructableString) {
        if(StringUtils.countMatches(reconstructableString, "|") == 4)
        {
            String[] parts = StringUtils.split(reconstructableString, '|');
            return new JnMessage(parts[0], JnMessageSet.valueOf(parts[1]), parts[2], parts[3], parts[4]);
        }
        throw new IllegalArgumentException("Cannot create JnMessage from String: " + reconstructableString);
    }

    public JnMessageSet getMessageFlag() {
        return messageFlag;
    }

    public JnMessage(String messageId, JnMessageSet messageFlag, String phoneNumber, String subject, String sender)
    {
        this.messageId = messageId;
        this.messageFlag = messageFlag;
        this.phoneNumber = phoneNumber;
        this.subject = subject;
        this.sender = sender;
    }
    // {C1=null:37:1|RequestedToJoinWithContact|||37}
    public String toReconstructableString() {
        return StringUtils.joinWith("|", messageId, messageFlag.name(), phoneNumber, subject, sender);
    }

    public String getMessageId() {
        return messageId;
    }
}
