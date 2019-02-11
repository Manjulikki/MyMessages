package manjunath.com.mymessages.presenter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import manjunath.com.mymessages.Entity.SMSEntity;
import manjunath.com.mymessages.R;
import manjunath.com.mymessages.Utils.Utils;
import manjunath.com.mymessages.contract.SMSContract;

public class SMSPresenter implements SMSContract.Presenter{

    private SMSContract.View view;
    private Context context;

    public SMSPresenter(SMSContract.View view , Context context){
        this.view = view;
        this.context = context;
    }

    @Override
    public void getAllMessagesFromInbox() {
        SimpleDateFormat formatter = new SimpleDateFormat(context.getResources().getString(R.string.dateFormat));
        String today = String.valueOf(formatter.format(Calendar.getInstance().getTime()));
            Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
            List<SMSEntity> smsEntities = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    String msgData = "";
                    String messageBody = "";
                    String messageFrom = "";
                    String dateReceived = "";
                    for (int idx = 0; idx < cursor.getColumnCount(); idx++) {
                        msgData += "&" + cursor.getColumnName(idx) + "=" + cursor.getString(idx);
                        if (cursor.getColumnName(idx).contains("body")) {
                            messageBody = cursor.getString(idx);
                        } else if (cursor.getColumnName(idx).contains("address")) {
                            messageFrom = cursor.getString(idx);
                        } else if (cursor.getColumnName(idx).equals("date")) {
                            dateReceived = cursor.getString(idx);
                        }
                    }
                    String dateReceivedString = Utils.getDate(Long.valueOf(dateReceived));
                    int receivedDate = Integer.valueOf(dateReceivedString.substring(0,2));
                    int todayDate = Integer.valueOf(today.substring(0,2));
                    if (todayDate == receivedDate || todayDate -1 == receivedDate) {
                        SMSEntity smsEntity = new SMSEntity(messageBody, messageFrom, dateReceived );
                        smsEntities.add(smsEntity);
                        Log.d("All Messages ", msgData);
                    }
                } while (cursor.moveToNext());
                Collections.sort(smsEntities, new Comparator<SMSEntity>() {
                    @Override
                    public int compare(SMSEntity lhs, SMSEntity rhs) {
                        return Long.valueOf(lhs.getReceivedTime()) > Long.valueOf(rhs.getReceivedTime()) ? -1 :
                                (Long.valueOf(lhs.getReceivedTime()) < Long.valueOf(rhs.getReceivedTime()) ? 1 : 0);
                    }
                });
                view.setAllMessages(smsEntities);
            } else {
                Toast.makeText(context, "Your inbox is empty", Toast.LENGTH_LONG).show();
            }
    }
}
