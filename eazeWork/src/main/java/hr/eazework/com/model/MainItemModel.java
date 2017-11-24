package hr.eazework.com.model;

public class MainItemModel {
	private String mLeftTitle;
	private String mLeftSubTitle;
	private String mRightTitle;
	private String mRightSubTitle;
	private int imgResource;
	private String imgUrl;
	private boolean isImageResource=true;
	private boolean isRightLayoutVisible;
	private boolean isRightImageVisible=true;
	private boolean isUnderLined;
	private String objectId;

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public MainItemModel(String lTitle, int imgResource){
		mLeftTitle = lTitle;
		this.imgResource = imgResource;
	}
	
	public MainItemModel(String lTitle,String lSubTitle,int imgResource) {
		mLeftTitle=lTitle;
		mLeftSubTitle=lSubTitle;
		this.imgResource=imgResource;
		isImageResource=true;
		isRightLayoutVisible=false;
	}
	public MainItemModel(String lTitle,String lSubTitle,int imgResource,boolean isImageRight) {
		mLeftTitle=lTitle;
		mLeftSubTitle=lSubTitle;
		this.imgResource=imgResource;
		isImageResource=true;
		isRightLayoutVisible=false;
		isRightImageVisible=isImageRight;
	}
	public MainItemModel(String lTitle,String lSubTitle,String imgUrl,int imgResource) {
		mLeftTitle=lTitle;
		mLeftSubTitle=lSubTitle;
		this.imgUrl=imgUrl;
		this.imgResource=imgResource;
		isImageResource=false;
		isRightImageVisible=false;
		isRightLayoutVisible=false;
	}

	public MainItemModel(String lTitle,String lSubTitle,String rTitle,String rSubTitle,int imgResource) {
		mLeftTitle=lTitle;
		mLeftSubTitle=lSubTitle;
		mRightTitle=rTitle;
		mRightSubTitle=rSubTitle;
		this.imgResource=imgResource;
		isImageResource=true;
		isRightLayoutVisible=true;
	}
	public MainItemModel(String lTitle,String lSubTitle,int imgResource,boolean isRightLayoutVisible,boolean isImageResource) {
		mLeftTitle=lTitle;
		mLeftSubTitle=lSubTitle;
		mRightTitle="";
		mRightSubTitle="";
		this.imgResource=imgResource;
		this.isImageResource=isImageResource;
		this.isRightLayoutVisible=isRightLayoutVisible;
	}
	public MainItemModel(String lTitle,String lSubTitle,String rTitle,String rSubTitle,int imgResource,boolean isUnderLined) {
		mLeftTitle=lTitle;
		mLeftSubTitle=lSubTitle;
		mRightTitle=rTitle;
		mRightSubTitle=rSubTitle;
		this.imgResource=imgResource;
		isImageResource=true;
		isRightLayoutVisible=true;
		this.isUnderLined=isUnderLined;
	}
	public MainItemModel(String lTitle,String lSubTitle,String rTitle,String rSubTitle,String imgUrl) {
		mLeftTitle=lTitle;
		mLeftSubTitle=lSubTitle;
		mRightSubTitle=rSubTitle;
		this.imgResource=imgResource;
		this.imgUrl=imgUrl;
		isImageResource=false;
		isRightLayoutVisible=true;
	}
	public String getmLeftTitle() {
		return mLeftTitle;
	}
	public void setmLeftTitle(String mLeftTitle) {
		this.mLeftTitle = mLeftTitle;
	}
	public String getmLeftSubTitle() {
		return mLeftSubTitle;
	}
	public void setmLeftSubTitle(String mLeftSubTitle) {
		this.mLeftSubTitle = mLeftSubTitle;
	}
	public String getmRightTitle() {
		return mRightTitle;
	}
	public void setmRightTitle(String mRightTitle) {
		this.mRightTitle = mRightTitle;
	}
	public String getmRightSubTitle() {
		return mRightSubTitle;
	}
	public void setmRightSubTitle(String mRightSubTitle) {
		this.mRightSubTitle = mRightSubTitle;
	}
	public int getImgResource() {
		return imgResource;
	}
	public void setImgResource(int imgResource) {
		this.imgResource = imgResource;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public boolean isImageResource() {
		return isImageResource;
	}
	public void setImageResource(boolean isImageResource) {
		this.isImageResource = isImageResource;
	}
	public boolean isRightLayoutVisible() {
		return isRightLayoutVisible;
	}
	public void setRightLayoutVisible(boolean isRightLayoutVisible) {
		this.isRightLayoutVisible = isRightLayoutVisible;
	}
	public boolean isImageVisible() {
		return isRightImageVisible;
	}
	public void setImageVisible(boolean isImageVisible) {
		this.isRightImageVisible = isImageVisible;
	}
	public boolean isUnderLined() {
		return isUnderLined;
	}
	public void setUnderLined(boolean isUnderLined) {
		this.isUnderLined = isUnderLined;
	}
	
}
