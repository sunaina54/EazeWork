package hr.eazework.com;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.vision.text.Line;

import hr.eazework.com.model.LineItemsModel;
import hr.eazework.com.model.SaveExpenseRequestModel;
import hr.eazework.com.ui.fragment.AddExpenseClaimFragment;
import hr.eazework.com.ui.fragment.AddExpenseFragment;

public class AddExpenseActivity extends BaseActivity {
    private FragmentManager fragmentManager;
    private FragmentTransaction fragTransaction;
    public static SaveExpenseRequestModel saveExpenseRequestModel;
    public static LineItemsModel lineItemsModel;
    public  static String SAVE_EXPENSE_REQUEST="saveExpenseRequest";
    public  static String LINE_ITEM_REQUEST="lineItemMddel";
    public static int REQUEST_CODE=10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        //saveExpenseRequestModel= AddExpenseClaimFragment.saveExpenseRequestModel;  //(SaveExpenseRequestModel) getIntent().getSerializableExtra(SAVE_EXPENSE_REQUEST);
        //lineItemsModel=(LineItemsModel) getIntent().getSerializableExtra(LINE_ITEM_REQUEST);
        populateFragment();
    }
    private void populateFragment(){
        fragmentManager =getSupportFragmentManager();
        fragTransaction=fragmentManager.beginTransaction();
        AddExpenseFragment fragment=new AddExpenseFragment();
        fragment.setExpenseRequestModel(saveExpenseRequestModel);
        fragment.setLineItemList(lineItemsModel);
        fragTransaction.replace(R.id.fragmentContainer,fragment);
        fragTransaction.commitAllowingStateLoss();
    }
}
