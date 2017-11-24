package hr.eazework.com.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.ExpenseDetailItemModel;
import hr.eazework.com.model.MenuItemModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.ui.fragment.AdvanceRequestSummaryFragment;
import hr.eazework.com.ui.fragment.BaseFragment;
import hr.eazework.com.ui.fragment.ExpenseClaimSummaryFragment;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.util.Preferences;
import hr.eazework.com.ui.util.Utility;


/**
 * Created by Dell3 on 23-08-2017.
 */

public class ExpenseHomeFragment extends BaseFragment {
    public static final String TAG = "ExpenseHomeFragment";
    private ViewPager viewPager;
    private TabLayout mTabLayout;
    private static Context context;
    private FragmentManager fragmentManager;
    private Preferences preferences;
    private TextView tv_header_text;
    private ImageView ibWrong, ibRight;
    private ViewPagerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.setShowPlusMenu(true);
        this.setShowEditTeamButtons(false);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.expense_home, container, false);
        context = getContext();
        preferences = new Preferences(getContext());
        setupView(rootView);
        return rootView;
    }

    private void setupView(View view) {
        fragmentManager = getActivity().getSupportFragmentManager();
        preferences = new Preferences(context);
        int textColor = Utility.getTextColorCode(preferences);
        int bgColor = Utility.getBgColorCode(context, preferences);
        ((TextView) getActivity().findViewById(R.id.tv_header_text)).setTextColor(textColor);
        ((TextView) ((MainActivity) getActivity()).findViewById(R.id.tv_header_text)).setText("Home");
        ((MainActivity) getActivity()).findViewById(R.id.ibRight).setVisibility(View.GONE);
        ((MainActivity) getActivity()).findViewById(R.id.ibWrong).setVisibility(View.GONE);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        mTabLayout.setBackgroundColor(bgColor);
        setupViewPager(view);
    }


    private void setupViewPager(View view) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.add(getResources().getString(R.string.expense_claim_home), getResources().getColor(R.color.white));
        adapter.add(getResources().getString(R.string.expense_request_home), getResources().getColor(R.color.primary_color_yellow));
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(0);
        mTabLayout.setupWithViewPager(viewPager);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragmentList = new ArrayList<>();
        private List<String> mFragmentTitleList = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void add(String title, int color) {
            Fragment fragment;
            Log.d("Home Activity", "Title Name : " + title);
            if (title.equalsIgnoreCase(context.getString(R.string.expense_claim_home))) {
                fragment = ExpenseClaimSummaryFragment.newInstance(title, color);
                mFragmentList.add(fragment);
                mFragmentTitleList.add(title);
            } else if (title.equalsIgnoreCase(context.getString(R.string.expense_request_home))) {
                fragment = AdvanceRequestSummaryFragment.newInstance(title, color);
                mFragmentList.add(fragment);
                mFragmentTitleList.add(title);
            }

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

}
