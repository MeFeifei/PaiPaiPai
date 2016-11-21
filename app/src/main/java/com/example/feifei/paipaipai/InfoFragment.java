package com.example.feifei.paipaipai;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import DB.UserDAO;
import entity.Person;



/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {
    private static String LOG_TAG = "Info Fragment";

    private Index mActivity;
    private TextView nickname,gender,personalSign;

    public InfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        nickname = (TextView) view.findViewById(R.id.nickname_show);
        gender = (TextView) view.findViewById(R.id.gender_show);
        personalSign = (TextView) view.findViewById(R.id.signal_show);

        mActivity = (Index) getActivity();
        initData();

        return view;
    }

    private void initData(){
        Person mPerson = mActivity.getPerson();
        Log.i(LOG_TAG,"读取用户信息成功"+ mPerson.getPassword()+","+ mPerson.getUsername()+","+ mPerson.getPhone());
        nickname.setText(mPerson.getUsername());
        gender.setText(mPerson.getGender());
        personalSign.setText(mPerson.getPersonalSign());
    }

}
