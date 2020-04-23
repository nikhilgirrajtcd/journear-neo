package com.journear.app.core.services;
import org.apache.commons.lang3.StringUtils;

public class JourNearMessage {

    private JourNearMessageSet messageFlag;
    private String messageId;

    /**
     * Phone number if available
     */
    private String phoneNumber = "";

    /**
     * This field would contain the ID of the ride that this conversation is about.
     */
    private String subject;

    /**
     *  MAC Address of the sender.
     */
    private String sender;

    public static JourNearMessage createFromReconstructableString(String reconstructableString) {
        if(StringUtils.countMatches(reconstructableString, "|") == 4)
        {
            String[] parts = StringUtils.split(reconstructableString, '|');
            return new JourNearMessage(parts[0], JourNearMessageSet.valueOf(parts[1]), parts[2], parts[3], parts[4]);
        }
        throw new IllegalArgumentException("Cannot create JourNearMessage from String: " + reconstructableString);
    }

    public JourNearMessage(String messageId, JourNearMessageSet messageFlag, String phoneNumber, String subject, String sender)
    {
        this.messageId = messageId;
        this.messageFlag = messageFlag;
        this.phoneNumber = phoneNumber;
        this.subject = subject;
        this.sender = sender;
    }

    public String toReconstructableString() {
        return StringUtils.joinWith("|", messageId, messageFlag.name(), phoneNumber, subject, sender);
    }

    public String getMessageId() {
        return messageId;
    }
}
