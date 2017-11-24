package hr.eazework.com.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.Iterator;

import hr.eazework.com.R;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.communication.IBaseResponse;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyBaseFragment extends Fragment implements IBaseResponse,View.OnClickListener {


    public MyBaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }

    public boolean isSessionValid(String string) {
        boolean isValid=true;
        try {
            JSONObject jsonObject=new JSONObject(string);
            Iterator<String> iterator=jsonObject.keys();
            if(iterator.hasNext()){
                JSONObject object=jsonObject.optJSONObject(iterator.next());
                if(object!=null){
                    return object.optInt("ErrorCode", 0)!=-999;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return isValid;
    }

    @Override
    public void validateResponse(ResponseData response) {

    }

    @Override
    public void onClick(View v) {

    }
}
