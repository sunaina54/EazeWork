package hr.eazework.com.model;


public class DrawerItemModel {
	/**
	 * @author Pradeep
	 */
	private String mTitle;
	private int mTitleIconId;
	private boolean isIconVisible;
	private int itemCount=0;
	private boolean isItemCountVisible;
	public DrawerItemModel(String title) {
		mTitle=title;
		mTitleIconId=-1;
		isIconVisible=false;
	}
	public DrawerItemModel(String title,int iconId) {
		mTitle=title;
		mTitleIconId=iconId;
		isIconVisible=true;
	}
	public DrawerItemModel(String title,int iconId,boolean isIconShow) {
		mTitle=title;
		mTitleIconId=iconId;
		isIconVisible=isIconShow;
	}
	public String getmTitle() {
		return mTitle;
	}
	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}
	public int getmTitleIconId() {
		return mTitleIconId;
	}
	public void setmTitleIconId(int mTitleIconId) {
		this.mTitleIconId = mTitleIconId;
	}
	public boolean isIconVisible() {
		return isIconVisible;
	}
	public void setIconVisible(boolean isIconVisible) {
		this.isIconVisible = isIconVisible;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return mTitle;
	}
	public int getItemCount() {
		return itemCount;
	}
	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}
	public boolean isItemCountVisible() {
		return isItemCountVisible;
	}
	public void setItemCountVisible(boolean isItemCountVisible) {
		this.isItemCountVisible = isItemCountVisible;
	}
	
}
