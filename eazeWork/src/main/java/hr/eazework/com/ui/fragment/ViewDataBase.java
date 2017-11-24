package hr.eazework.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hr.eazework.com.R;
import hr.eazework.com.ui.util.EventDataSource;

/**
 * Created by Manjunath on 14-04-2017.
 */

public class ViewDataBase extends BaseFragment {
    public static final String TAG = "ViewDataBase";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.database_item,container,false);
        EventDataSource dataSource = new EventDataSource(getContext());
        TextView textView = (TextView) rootView.findViewById(R.id.dbtext);
        textView.setText(dataSource.getTableAsString());
        return rootView;
    }
}
