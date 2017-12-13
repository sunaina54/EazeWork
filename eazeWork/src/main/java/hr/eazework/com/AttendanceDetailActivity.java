package hr.eazework.com;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Date;

import hr.eazework.com.ui.util.Preferences;
import hr.eazework.com.ui.util.Utility;

/**
 * Created by Dell3 on 12-12-2017.
 */

public class AttendanceDetailActivity extends BaseActivity {

    private Context context;
    private Preferences preferences;
    private TextView tv_header_text;
    private LinearLayout rl_edit_team_member,statusLinearLayout,requestedOutTimeLl,requestedInTimeLl,remarksLinearLayout;
    private String currentDate;
    private RelativeLayout backLayout,rightRL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.attendance_details_layout);
        preferences = new Preferences(context);
        Date dateObj = new Date(getIntent().getExtras().getLong("Date", -1));
        Log.d("TAG","CURRENT DATE : "+dateObj);
        int textColor = Utility.getTextColorCode(preferences);
        int bgColor = Utility.getBgColorCode(context, preferences);
        tv_header_text=(TextView)findViewById(R.id.tv_header_text);
        currentDate=Utility.convertStringIntoDate(String.valueOf(dateObj));
        tv_header_text.setText(getResources().getString(R.string.attendance_details_header)+ " "+ currentDate);
        tv_header_text.setTextColor(textColor);
        rl_edit_team_member=(LinearLayout) findViewById(R.id.rl_edit_team_member);
        rl_edit_team_member.setBackgroundColor(bgColor);
        backLayout=(RelativeLayout)findViewById(R.id.backLayout);
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rightRL= (RelativeLayout) findViewById(R.id.rightRL);
        rightRL.setVisibility(View.GONE);
        statusLinearLayout= (LinearLayout) findViewById(R.id.statusLinearLayout);
        statusLinearLayout.setVisibility(View.VISIBLE);
        requestedOutTimeLl= (LinearLayout) findViewById(R.id.requestedOutTimeLl);
        requestedOutTimeLl.setVisibility(View.GONE);
        requestedInTimeLl= (LinearLayout) findViewById(R.id.requestedInTimeLl);
        requestedInTimeLl.setVisibility(View.GONE);
        remarksLinearLayout= (LinearLayout) findViewById(R.id.remarksLinearLayout);
        remarksLinearLayout.setVisibility(View.GONE);

    }
}
