package com.fashion.forlempopoli.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fashion.forlempopoli.Entity.request.NotificationRequest;
import com.fashion.forlempopoli.R;

import java.util.List;

public class NotificationAdapter extends  RecyclerView.Adapter<NotificationAdapter.ViewHolder>  {
    Context context;
    List<NotificationRequest> notificationRequests;
    LayoutInflater inflter;

    public NotificationAdapter(Context applicationContext, List<NotificationRequest> cityRequestList) {
        this.context = applicationContext;
        this.notificationRequests = cityRequestList;
        inflter = (LayoutInflater.from(applicationContext));


    }
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_adapter_layout, viewGroup, false);


        return new NotificationAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {

        holder.tvMessage.setText(notificationRequests.get(position).getNotifyName());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return notificationRequests.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvMessage;
        TextView tv_unread;
        LinearLayout ll_notify;

        ViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tv_unread = itemView.findViewById(R.id.tv_unread);
            ll_notify = itemView.findViewById(R.id.ll_notify);

            ll_notify.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ll_notify:
                    tv_unread.setVisibility(View.GONE);
                    break;
            }
        }
    }
}
