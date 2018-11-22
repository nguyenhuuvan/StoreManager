package poly.vannhph06247.storemanager.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import poly.vannhph06247.storemanager.R;
import poly.vannhph06247.storemanager.adapter.AdapterAppInfor;
import poly.vannhph06247.storemanager.model.InforApp;

public class AppInforFragment extends Fragment {
    private List<InforApp> introduceList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_infor_app,container,false);
        RecyclerView lvListIntro = view.findViewById(R.id.lvList);
        introduceList = new ArrayList<>();
        fakeDate();
        AdapterAppInfor introduceAdapter = new AdapterAppInfor(introduceList);
        lvListIntro.setAdapter(introduceAdapter);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        lvListIntro.setLayoutManager(manager);

        return view;
    }

    private void fakeDate(){
        InforApp introduce = new InforApp("Quản lý cửa hàng","Tên phần mềm");
        introduceList.add(introduce);
        InforApp introduce1 = new InforApp("01/11/2018","Ngày tạo");
        introduceList.add(introduce1);
        InforApp introduce2 = new InforApp("Nguyễn Hữu Văn","Tác giả");
        introduceList.add(introduce2);
        InforApp introduce3 = new InforApp("13.3","Khóa");
        introduceList.add(introduce3);
    }
}
