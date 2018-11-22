package poly.vannhph06247.storemanager.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import poly.vannhph06247.storemanager.R;
import poly.vannhph06247.storemanager.model.InforApp;

public class AdapterAppInfor extends RecyclerView.Adapter<AdapterAppInfor.ViewHolder> {
    private final List<InforApp> introduceList;

    public AdapterAppInfor(List<InforApp> introduceList) {
        this.introduceList = introduceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_introduce, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        InforApp introduce = introduceList.get(position);
        holder.tvName.setText(introduce.getName());
        holder.tvDes.setText(introduce.getDes());
    }

    @Override
    public int getItemCount() {
        if (introduceList == null) return 0;
        return introduceList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvName;
        final TextView tvDes;

        ViewHolder(View itemView) {
            super(itemView);
            tvDes = itemView.findViewById(R.id.tvDes);
            tvName = itemView.findViewById(R.id.tvName);
        }

    }
}
