package manjunath.com.mymessages.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import java.util.List;
import manjunath.com.mymessages.R;
import manjunath.com.mymessages.contract.SMSContract;
import manjunath.com.mymessages.Entity.SMSEntity;
import manjunath.com.mymessages.adapter.SMSLayoutAdapter;
import manjunath.com.mymessages.presenter.SMSPresenter;

public class MainActivity extends AppCompatActivity implements SMSContract.View {

    private final int REQUEST_CODE_ASK_PERMISSIONS = 1;
    public static boolean isThroughNotification = false;
    private SMSPresenter smsPresenter;
    private SMSLayoutAdapter smsLayoutAdapter;
    private RecyclerView smsGrid;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getIntentData();
        setupViews();
        fetchAllMessages();
    }

    @Override
    public void setAllMessages(List<SMSEntity> smsEntities) {
        layoutManager = new LinearLayoutManager(this);
        smsGrid.setLayoutManager(layoutManager);
        smsLayoutAdapter = new SMSLayoutAdapter(this,smsEntities);
        smsGrid.setAdapter(smsLayoutAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    smsPresenter.getAllMessagesFromInbox();
                } else {
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.requestPermission),Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void fetchAllMessages() {
        if(ContextCompat.checkSelfPermission(MainActivity.this, "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {
            smsPresenter.getAllMessagesFromInbox();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    private void setupViews() {
        smsGrid = (RecyclerView) findViewById(R.id.recycler_view);
        smsPresenter = new SMSPresenter(this, this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        setupListeners();
    }

    private void setupListeners() {
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        fetchAllMessages();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }

    private void getIntentData() {
        if (getIntent()!= null && getIntent().getExtras()!= null) {
            isThroughNotification = getIntent().getExtras().getBoolean(getResources().getString(R.string.throughNotification));
        }
    }
}
