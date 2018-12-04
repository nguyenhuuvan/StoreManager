package poly.project.storemanager.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.List;

import poly.project.storemanager.R;
import poly.project.storemanager.listener.OnClick;
import poly.project.storemanager.model.Information;

public class AdapterInformation extends RecyclerView.Adapter<AdapterInformation.ViewHolder> {

    private final List<Information> information;
    private final Context context;
    private int lastPosition = -1;
    private final OnClick onClick;

    public AdapterInformation(Context context, List<Information> information, OnClick onClick) {
        this.information = information;
        this.context = context;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_layout_information, parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint({"RecyclerView", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        setAnimation(holder.itemView, position);
        Information information = this.information.get(position);
        holder.tvDetail.setText(information.getDetail());
        holder.tvTitle.setText(information.getTitle());
        if (information.getContent().equals("")) {
            holder.tvContent.setText("Chưa có thông tin");
            holder.tvContent.setTextColor(Color.RED);
            holder.tvContent.setTextSize(12);
        } else {
            holder.tvContent.setText(information.getContent());
            holder.tvContent.setTextColor(Color.BLACK);
            holder.tvContent.setTextSize(16);
        }

        holder.tvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    onClick.onClick(position);
                } catch (Exception e) {
                    Log.e("lỗi onclick","lỗi onclick");
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        if (information == null) return 0;
        return information.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTitle;
        final TextView tvContent;
        final TextView tvDetail;


        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvDetail = itemView.findViewById(R.id.tvDetail);
        }

    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
