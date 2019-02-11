package manjunath.com.mymessages.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import manjunath.com.mymessages.Entity.SMSEntity;
import manjunath.com.mymessages.R;
import manjunath.com.mymessages.Utils.Utils;
import manjunath.com.mymessages.activity.MainActivity;

public class SMSLayoutAdapter extends RecyclerView.Adapter<SMSLayoutAdapter.ItemViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private List<SMSEntity> smsEntities;
    private TextView headerTV;
    private TextView messagesTV;
    private TextView fromTV;
    private TextView timeReceivedTV;
    private Context context;
    private boolean zeroHourShown = false;
    private boolean oneHourShown = false;
    private boolean twoHourShown = false;
    private boolean threeHourShown = false;
    private boolean sixHourShown = false;
    private boolean twelveHourShown = false;
    private boolean oneDayShown = false;
    private String headerText = "";

    public SMSLayoutAdapter(Context context, List<SMSEntity> smsEntities) {
        this.context = context;
        this.smsEntities = smsEntities;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int position) {

        int viewType = getViewType(position);
        if (viewType == TYPE_HEADER) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_layout, parent, false);
            if (MainActivity.isThroughNotification) {
                layoutView.setBackgroundColor(context.getResources().getColor(R.color.orange));
            } else {
                layoutView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            }
            return new HeaderViewHolder(layoutView);
        } else if (viewType == TYPE_ITEM) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sms_layout, parent, false);
            return new ItemViewHolder(layoutView);
        }
        throw new RuntimeException("No match for " + viewType + ".");
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        SMSEntity smsEntity = smsEntities.get(position);
        String time = Utils.getDate(Long.valueOf(smsEntity.getReceivedTime()));
        if (holder instanceof HeaderViewHolder) {
            if (getTodayTime() == getReceivedTime(position) && !zeroHourShown) {
                if (MainActivity.isThroughNotification) {
                    headerTV.setBackgroundColor(context.getResources().getColor(R.color.darkOrange));
                    MainActivity.isThroughNotification = false;
                } else {
                    headerTV.setBackgroundColor(context.getResources().getColor(R.color.darkBlue));
                }
            }
            headerTV.setText(headerText);
        } else {
            messagesTV.setText(smsEntity.getMessage());
            fromTV.setText(smsEntity.getMessageFrom());
            timeReceivedTV.setText(time);
        }
    }

    @Override
    public int getItemCount() {
        return smsEntities.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private int getViewType(int position) {
        int todayTime = getTodayTime();
        int receivedTime = getReceivedTime(position);
        int todayDay = getTodayDate();
        int receivedDay = getReceivedDate(position);
        if (todayTime == receivedTime && !zeroHourShown) {
            zeroHourShown = true;
            headerText = context.getResources().getString(R.string.zeroHourAgo);
            return TYPE_HEADER;
        } else if (todayTime - 1 == receivedTime && !oneHourShown) {
            oneHourShown = true;
            headerText = context.getResources().getString(R.string.oneHourAgo);
            return TYPE_HEADER;
        } else if (todayTime - 2 == receivedTime && !twoHourShown) {
            twoHourShown = true;
            headerText = context.getResources().getString(R.string.twoHourAgo);
            return TYPE_HEADER;
        } else if (todayTime - 3 == receivedTime && !threeHourShown) {
            threeHourShown = true;
            headerText = context.getResources().getString(R.string.threeHourAgo);
            return TYPE_HEADER;
        } else if (todayTime - 6 == receivedTime && !sixHourShown) {
            sixHourShown = true;
            headerText = context.getResources().getString(R.string.sixHourAgo);
            return TYPE_HEADER;
        } else if (todayTime - 12 == receivedTime && !twelveHourShown) {
            twelveHourShown = true;
            headerText = context.getResources().getString(R.string.twelveHourAgo);
            return TYPE_HEADER;
        } else if (todayDay - 1 == receivedDay && !oneDayShown) {
            oneDayShown = true;
            headerText = context.getResources().getString(R.string.oneDayAgo);
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    private int getTodayDate() {
        String today = Utils.getDate(System.currentTimeMillis());
        return Integer.valueOf(today.substring(0, 2));
    }

    private int getReceivedDate(int position) {
        String time = Utils.getDate(Long.valueOf(smsEntities.get(position).getReceivedTime()));
        return Integer.valueOf(time.substring(0, 2));
    }
    private int getTodayTime() {
        String today = Utils.getDate(System.currentTimeMillis());
        return Integer.valueOf(today.substring(today.length() - 8, today.length() - 6));
    }

    private int getReceivedTime(int position) {
        String receivedTime = Utils.getDate(Long.valueOf(smsEntities.get(position).getReceivedTime()));
        return Integer.valueOf(receivedTime.substring(receivedTime.length() - 8, receivedTime.length() - 6));
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public ItemViewHolder(View view) {
            super(view);
            messagesTV = (TextView) view.findViewById(R.id.message);
            fromTV = (TextView) view.findViewById(R.id.from);
            timeReceivedTV = (TextView) view.findViewById(R.id.time);
        }
    }

    private class HeaderViewHolder extends ItemViewHolder {

        public HeaderViewHolder(View view) {
            super(view);
            headerTV = (TextView) view.findViewById(R.id.timeAgo);
        }
    }
}