package com.akshaysonvane.cmpe273;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akshaysonvane.cmpe273.adapters.RestAdapterClass;
import com.akshaysonvane.cmpe273.api.ConnectionApi;
import com.akshaysonvane.cmpe273.model.AttendanceModel;
import com.akshaysonvane.cmpe273.model.ResponseModel;
import com.squareup.picasso.Picasso;


import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class FirstFragment extends Fragment
{
    @Bind(R.id.txtStatus)
    TextView txtStatus;

    @Bind(R.id.imgStatus)
    ImageView imgStatus;

    SharedPreferences cmpe273prefs;

    private OnFragmentInteractionListener mListener;

    public FirstFragment()
    {
        // Required empty public constructor
    }


    public static FirstFragment newInstance()
    {
        FirstFragment fragment = new FirstFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {

        }

    }

    public void checkAttendace(AttendanceModel attendanceModel)
    {
        ConnectionApi connectionApi = new RestAdapterClass().getApiClassObject();

        connectionApi.checkAttendance(attendanceModel, new Callback<ResponseModel>()
        {
            @Override
            public void success(ResponseModel responseModel, Response response)
            {
                if (responseModel != null)
                {
                    Toast.makeText(getActivity(), responseModel.getMessage(), Toast.LENGTH_LONG).show();

                    if (responseModel.getData().equalsIgnoreCase("true"))
                    {
                        Picasso.with(getActivity()).load(R.drawable.tick).into(imgStatus);
                        txtStatus.setText(responseModel.getMessage());
                    }
                }
            }

            @Override
            public void failure(RetrofitError error)
            {
                error.printStackTrace();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        ButterKnife.bind(this, view);

        Picasso.with(getActivity()).load(R.drawable.cross).into(imgStatus);
        txtStatus.setText("Attendance not marked yet.");

        cmpe273prefs = getActivity().getSharedPreferences("cmpe273", Context.MODE_PRIVATE);
        AttendanceModel attendanceModel = new AttendanceModel();
        attendanceModel.setMacAddress(cmpe273prefs.getString("mac", "c0:ee:fb:30:09:e8"));
        attendanceModel.setClassId(cmpe273prefs.getString("inputClass", "CMPE273"));

        checkAttendace(attendanceModel);
        return view;
    }

    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
