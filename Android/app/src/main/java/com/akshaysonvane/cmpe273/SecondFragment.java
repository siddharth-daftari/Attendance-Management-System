package com.akshaysonvane.cmpe273;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akshaysonvane.cmpe273.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SecondFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondFragment extends Fragment
{
    @Bind(R.id.imgInfo)
    ImageView imgInfo;
    @Bind(R.id.txtName)
    TextView txtName;
    @Bind(R.id.txtEmail)
    TextView txtEmail;
    @Bind(R.id.txtClass)
    TextView txtClass;
    @Bind(R.id.txtStudentId)
    TextView txtStudentId;

    SharedPreferences cmpe273prefs;

    private OnFragmentInteractionListener mListener;

    public SecondFragment()
    {
        // Required empty public constructor
    }

    public static SecondFragment newInstance()
    {
        SecondFragment fragment = new SecondFragment();

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        ButterKnife.bind(this, view);

        initUI();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    public void initUI()
    {
        cmpe273prefs = getActivity().getSharedPreferences("cmpe273", Context.MODE_PRIVATE);
        String name = "Name: " + cmpe273prefs.getString("firstName", "John") + "  " + cmpe273prefs.getString("lastName", "Doe");
        txtName.setText(name);
        String email = "Email: " + cmpe273prefs.getString("emailId", "john.doe@gmail.com");
        txtEmail.setText(email);
        String classId = "Class: " + cmpe273prefs.getString("inputClass", "CMPE273");
        txtClass.setText(classId);
        String studentId = "Student Id: " + cmpe273prefs.getString("studentId", "011324989");
        txtStudentId.setText(studentId);
        Picasso.with(getActivity()).load(cmpe273prefs.getString("displayPicUrl", "http://i1.wp.com/www.techrepublic.com/bundles/techrepubliccore/images/icons/standard/icon-user-default.png")).transform(new CircleTransform()).into(imgInfo);
    }
}
