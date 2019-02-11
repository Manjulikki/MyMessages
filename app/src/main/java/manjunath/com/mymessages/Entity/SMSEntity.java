package manjunath.com.mymessages.Entity;

public class SMSEntity {

    private String message;
    private String ReceivedTime;
    private String messageFrom;

    public SMSEntity(String message, String messageFrom, String receivedTime) {
        this.message = message;
        ReceivedTime = receivedTime;
        this.messageFrom = messageFrom;
    }

    public String getMessage() {
        return message;
    }

    public String getReceivedTime() {
        return ReceivedTime;
    }

    public String getMessageFrom() {
        return messageFrom;
    }
}
