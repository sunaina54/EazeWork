package hr.eazework.com.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.crashlytics.android.Crashlytics;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.LineItemsModel;
import hr.eazework.com.ui.interfaces.UserActionListner;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.communication.IBaseResponse;


public abstract class BaseFragment extends Fragment implements OnClickListener,IBaseResponse {

    private static final String TAG = "BaseFragment";
	protected View rootView;
	protected UserActionListner mUserActionListener;
	protected boolean showPlusMenu = true;
	protected boolean showLeaveButtons = false;
	protected boolean showEditTeam =false;
	protected boolean showEditTeamButtons = false;
    protected boolean showPaySlipHeader = false;
    public static boolean geofencesAlreadyRegistered = false;

    public boolean isShowPaySlipHeader() {
        return showPaySlipHeader;
    }

    public void setShowPaySlipHeader(boolean showPaySlipHeader) {
        this.showPaySlipHeader = showPaySlipHeader;
    }

    public boolean isShowEditTeamButtons() {
		return showEditTeamButtons;
	}

	public void setShowEditTeamButtons(boolean showEditTeamButtons) {
		this.showEditTeamButtons = showEditTeamButtons;
	}

	public boolean isShowEditTeam() {
		return showEditTeam;
	}

	public void setShowEditTeam(boolean showEditTeam) {
		this.showEditTeam = showEditTeam;
	}

	public boolean isShowLeaveButtons() {
		return showLeaveButtons;
	}

	public void setShowLeaveButtons(boolean showLeaveButtons) {
		this.showLeaveButtons = showLeaveButtons;
	}

	public boolean isShowPlusMenu() {
		return showPlusMenu;
	}

	public void setShowPlusMenu(boolean showPlusMenu) {
		this.showPlusMenu = showPlusMenu;
	}



	@Override
	public void onResume() {
		if (this instanceof SplashFragment 
				|| this instanceof LoginFragment) {
			((MainActivity) getActivity()).showHideActionBar(false);
		} else {
			((MainActivity) getActivity()).showHideActionBar(true);
		}

		if ( this instanceof HomeFragment) {
			((MainActivity) getActivity()).showHideActionBarLogo(true);
		} else {
			((MainActivity) getActivity()).showHideActionBarLogo(false);
		}
		super.onResume();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()== R.id.home){
			if(!(this instanceof HomeFragment)){
				getActivity().onBackPressed();
				return false;
			}
		}
		return super.onOptionsItemSelected(item);
	}
	/*@Override
	public void onAttach(Activity activity) {
		if (activity instanceof UserActionListner) {
			mUserActionListener = (UserActionListner) activity;
		}
		super.onAttach(activity);
	}*/

    public void refreshUi() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity;

        if(context instanceof Activity) {
            activity = (Activity) context;
            try {
                mUserActionListener = (UserActionListner) activity;
            } catch (ClassCastException e) {
                Log.e(TAG,e.getMessage(),e);
                Crashlytics.logException(e);
            }
        }

        Log.e(TAG,context.toString());

    }

    @Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	public void showHideProgressView(boolean isShow){
        Log.d(MainActivity.TAG,"View(rootView) found in show progress for activity : "+ rootView);
        if(rootView==null)
			return;
		if(rootView.findViewById(R.id.ll_progress_container)!=null){
			rootView.findViewById(R.id.ll_progress_container).setVisibility(isShow?View.VISIBLE:View.GONE);
		}
	}
	public void showHideNetworkErrorView(boolean isShow){
		if(rootView==null)
			return;
		if(rootView.findViewById(R.id.ll_error_layout)!=null){
			rootView.findViewById(R.id.ll_error_layout).setVisibility(isShow?View.VISIBLE:View.GONE);
		}
	}
	@Override
	public void validateResponse(ResponseData response) {
		
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


}
