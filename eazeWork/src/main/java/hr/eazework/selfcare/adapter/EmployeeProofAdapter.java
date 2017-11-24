package hr.eazework.selfcare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import hr.eazework.com.R;
import hr.eazework.com.model.MemberReqInputModel;
import hr.eazework.com.ui.interfaces.OnItemClickListner;

/**
 * Created by Manjunath on 24-03-2017.
 */

public class EmployeeProofAdapter extends RecyclerView.Adapter<EmployeeProofAdapter.MyViewHolder> {

    private ArrayList<MemberReqInputModel> list;
    private LayoutInflater inflater;
    private OnItemClickListner listner;

    public EmployeeProofAdapter(Context context, ArrayList<MemberReqInputModel> list, OnItemClickListner listner) {
        this.list = list;
        this.inflater = LayoutInflater.from(context);
        this.listner = listner;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View  view = inflater.inflate(R.layout.employee_photo_list_item,parent,false);
        EmployeeProofAdapter.MyViewHolder holder = new EmployeeProofAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MemberReqInputModel employeeProof = list.get(position);
        holder.setData(employeeProof,position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        int position;
        ImageView icon;
        MemberReqInputModel employeeProof;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listner.onItemClick(getAdapterPosition());
                }
            });
        }


        public void setData(MemberReqInputModel employeeProof, int position) {
            this.title.setText(employeeProof.getmFieldLabel());
            this.icon.setImageResource(R.drawable.team_icon);
            this.position = position;
            this.employeeProof = employeeProof;
        }
    }
}
