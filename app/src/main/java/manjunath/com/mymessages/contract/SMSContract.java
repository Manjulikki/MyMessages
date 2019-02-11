package manjunath.com.mymessages.contract;

import java.util.List;

import manjunath.com.mymessages.Entity.SMSEntity;

public class SMSContract {

    public interface View {
        void setAllMessages(List<SMSEntity> smsEntities);
    }

    public interface Presenter {
        void getAllMessagesFromInbox();
    }
}
