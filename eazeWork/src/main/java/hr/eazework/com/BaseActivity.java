package hr.eazework.com;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.communication.IBaseResponse;

public class BaseActivity extends AppCompatActivity implements IBaseResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    @Override
    public void validateResponse(ResponseData response) {

    }


}
