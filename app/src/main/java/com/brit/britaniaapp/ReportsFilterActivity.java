package com.brit.britaniaapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.appcompat.widget.ContentFrameLayout;
import androidx.fragment.app.FragmentManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.brit.britaniaapp.base.BaseNavActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReportsFilterActivity extends BaseNavActivity {

    ArrayList<String> departments = new ArrayList<>();

    Report selectedReport = null;

    private ArrayList<Report> reports = new ArrayList<>();

    Spinner spDepartment;
    Spinner spReportName;
    private ProgressBar progress;
    private RequestQueue mRequestQueue;
    private String departmentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContentFrameLayout contentFrameLayout = findViewById(R.id.base_nav_content_section);
        getLayoutInflater().inflate(R.layout.activity_reports_filter, contentFrameLayout);

        progress = findViewById(R.id.progress);
        spDepartment = findViewById(R.id.spDepartment);
        spReportName = findViewById(R.id.spReportName);
        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);


        ((Button) findViewById(R.id.listReport)).setOnClickListener(view -> {
            Intent intent = new Intent(this, ChartListActivity.class);
            intent.putExtra("report", selectedReport);
            startActivity(intent);
        });
        ((Button) findViewById(R.id.addReport)).setOnClickListener(view -> startActivity(new Intent(this, AddReportActivity.class)));
        ((Button) findViewById(R.id.btnDelete)).setOnClickListener(view -> {
            Log.d("List count", "" + reports.size());
            FragmentManager manager = getSupportFragmentManager();
            DeleteReportDialog dialog = DeleteReportDialog.newInstance(reports);
            dialog.show(manager, "Trailer");
            manager.executePendingTransactions();
        });

        spDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                departmentName = adapterView.getSelectedItem().toString();
                String repUrl = "http://35.194.23.15:7197/reports?department=" + departmentName;
                sendRequest(Constant.REQUEST_REPORT, repUrl);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spReportName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedReport = reports.get(adapterView.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sendRequest(Constant.REQUEST_DEPARTMENT, "http://35.194.23.15:7197/departments");

    }

    private void sendRequest(int request, String url) {

        progress.setVisibility(View.VISIBLE);

        //String Request initialized
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {
                if (request == Constant.REQUEST_DEPARTMENT) {
                    departments.clear();
                    JSONObject object = new JSONObject(response);
                    JSONArray jsonArray = object.getJSONArray("departments");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        departments.add((String) jsonArray.get(i));
                        if (i == 0) {
                            String repUrl = "http://35.194.23.15:7197/reports?department=" + jsonArray.getString(i);
                            sendRequest(Constant.REQUEST_REPORT, repUrl);
                            Log.e("Url:---", repUrl);
                        }
                    }
                    //Creating the ArrayAdapter instance having the country list
                    ArrayAdapter aDepartment = new ArrayAdapter(this, R.layout.spinner_item, departments);
                    aDepartment.setDropDownViewResource(R.layout.spinner_dropdown);
                    //Setting the ArrayAdapter data on the Spinner
                    spDepartment.setAdapter(aDepartment);
                    progress.setVisibility(View.GONE);
                } else if (request == Constant.REQUEST_REPORT) {
                    reports.clear();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = (JSONObject) jsonArray.get(i);
                        int id = obj.getInt("id");
                        String department_name = obj.getString("department_name");
                        String report_name = obj.getString("report_name");
                        String report_url = obj.getString("report_url");
                        reports.add(new Report(id, department_name, report_name, report_url));
                    }
                    //Creating the ArrayAdapter instance having the country list
                    ArrayAdapter<Report> aPeportName = new ArrayAdapter(this, R.layout.spinner_item, reports);
                    aPeportName.setDropDownViewResource(R.layout.spinner_dropdown);
                    //Setting the ArrayAdapter data on the Spinner
                    spReportName.setAdapter(aPeportName);
                    progress.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("Resp:---", "" + e.getMessage());
            }
        }, error -> {
            Log.d("Resp:---", "" + error);
            progress.setVisibility(View.GONE);
        });
        mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(mStringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String repUrl = "http://35.194.23.15:7197/reports?department=" + departmentName;
        Log.d("Resume:====",repUrl);
        sendRequest(Constant.REQUEST_REPORT, repUrl);
    }
}