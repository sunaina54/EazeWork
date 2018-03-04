package hr.eazework.com.ui.interfaces;

import android.support.v4.app.Fragment;
import android.view.View;

public interface UserActionListner {
	void performUserActionFragment(int pActionType, Fragment pView, Object pData);
	 void performUserAction(int pActionType, View pView, Object pData);

}
