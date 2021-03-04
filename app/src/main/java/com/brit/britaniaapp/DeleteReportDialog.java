package com.brit.britaniaapp;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Objects;

public class DeleteReportDialog extends DialogFragment {

    private ArrayList<Report> list = new ArrayList<>();
    Dialog dialog;

    ReportsAdapter adapter;
    private Dialog logoutDialog;

    @Override
    public void onStart() {
        super.onStart();
        dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setGravity(Gravity.CENTER);
        }
    }

    public static DeleteReportDialog newInstance(ArrayList<Report> list) {
        Bundle args = new Bundle();
        args.putSerializable("list", list);
        DeleteReportDialog fragment = new DeleteReportDialog();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            list.addAll((ArrayList<Report>) getArguments().getSerializable("list"));
        }
        return getLayoutInflater().inflate(R.layout.report_selection_fragment, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        view.findViewById(R.id.tvCancel).setOnClickListener(view1 -> dismiss());
        view.findViewById(R.id.btnDelete).setOnClickListener(view1 -> {
            logoutDialog = new Dialog(getActivity());
            logoutDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            logoutDialog.setCancelable(false);
            logoutDialog.setCanceledOnTouchOutside(false);
            logoutDialog.setCanceledOnTouchOutside(true);
            logoutDialog.setContentView(R.layout.delete_dialog);
            Objects.requireNonNull(logoutDialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            logoutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            TextView btnCancel = logoutDialog.findViewById(R.id.tvCancel);
            Button btnYes = logoutDialog.findViewById(R.id.btnDelete);
            btnCancel.setOnClickListener(v1 -> logoutDialog.dismiss());
            btnYes.setOnClickListener(v2 -> {
                deleteRequest();
            });
            logoutDialog.show();
        });

        ((TextView) view.findViewById(R.id.etSearch)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
        adapter = new ReportsAdapter();
        ((RecyclerView) view.findViewById(R.id.recylerview)).setAdapter(adapter);
        filter("");
    }

    private void filter(String search) {
        ArrayList<Report> fltList = new ArrayList<>();

        for (Report report :
                list) {
            if (report.reportName.toLowerCase().contains(search.toLowerCase())) {
                fltList.add(report);
            }
        }

        if (fltList.size() > 0) {
            adapter.updateList(fltList);
        } else {
            adapter.updateList(list);
        }
    }

    private void deleteRequest() {
        ((ProgressBar) getView().findViewById(R.id.progress)).setVisibility(View.VISIBLE);
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            String URL = "http://35.194.23.15:7197/deletereports";
            JSONObject jsonBody = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < adapter.list.size(); i++) {
                Report report = adapter.list.get(i);
                if (report.isChecked)
                    jsonArray.put(report.getId());
            }
            jsonBody.put("ids", jsonArray);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, (Response.Listener<String>) response -> {
                Log.i("VOLLEY", response);
                Toast.makeText(getActivity(), "Reports deleted successfully!", Toast.LENGTH_SHORT).show();
                ((ProgressBar) getView().findViewById(R.id.progress)).setVisibility(View.GONE);
                logoutDialog.dismiss();
                dialog.dismiss();

            }, (Response.ErrorListener) error -> {
                Log.e("VOLLEY", error.toString());
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                ((ProgressBar) getView().findViewById(R.id.progress)).setVisibility(View.GONE);
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
