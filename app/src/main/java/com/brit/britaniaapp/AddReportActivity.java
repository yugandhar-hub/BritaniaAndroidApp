package com.brit.britaniaapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class AddReportActivity extends AppCompatActivity {

    ArrayList<Report> list = new ArrayList<>();
    ArrayList<String> departments = new ArrayList<>();

    Spinner spDepartment;
    TextInputEditText etReportName;
    TextInputEditText etWebUrl;
    private ProgressBar progress;
    private RequestQueue mRequestQueue;
    private String departmentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);
        ((ImageButton) findViewById(R.id.ibBack)).setOnClickListener(view -> onBackPressed());
        ((TextView) findViewById(R.id.tvTitle)).setText("Add Report");

        progress = findViewById(R.id.progress);
        spDepartment = findViewById(R.id.spDepartment);
        etReportName = findViewById(R.id.etReportName);
        etWebUrl = findViewById(R.id.etWebUrl);
        WebUrlRecyclerView adapter = new WebUrlRecyclerView(list);
        ((RecyclerView) findViewById(R.id.recylerview)).setAdapter(adapter);
        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);
        ((Button) findViewById(R.id.addButton)).setOnClickListener(view1 -> {
            String reportName = etReportName.getText().toString();
            String webUrl = etWebUrl.getText().toString();
            if (!reportName.isEmpty() && !webUrl.isEmpty()) {
                list.add(new Report(reportName, webUrl));
                etReportName.setText("");
                etWebUrl.setText("");
                adapter.notifyDataSetChanged();
            }

        });

        ((Button) findViewById(R.id.addReport)).setOnClickListener(view1 -> {
            addPeportRequest();
        });

        spDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                departmentName = adapterView.getSelectedItem().toString();
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
                JSONObject object = new JSONObject(response);
                JSONArray jsonArray = object.getJSONArray("departments");
                for (int i = 0; i < jsonArray.length(); i++) {
                    departments.add((String) jsonArray.get(i));
                }
                //Creating the ArrayAdapter instance having the country list
                ArrayAdapter aDepartment = new ArrayAdapter(this, R.layout.spinner_item, departments);
                aDepartment.setDropDownViewResource(R.layout.spinner_dropdown);
                //Setting the ArrayAdapter data on the Spinner
                spDepartment.setAdapter(aDepartment);
                progress.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            progress.setVisibility(View.GONE);
        });
        mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(mStringRequest);
    }


    private void addPeportRequest() {
        progress.setVisibility(View.VISIBLE);
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://35.194.23.15:7197/addreports";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("department_name", departmentName);
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = new JSONObject();
                obj.put("report_name", list.get(i).reportName);
                obj.put("report_url", list.get(i).webUrl);
                jsonArray.put(obj);
            }
            jsonBody.put("reports", jsonArray);
            final String requestBody = jsonBody.toString();
            Log.d("req:-----",requestBody);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                    Toast.makeText(AddReportActivity.this, "Report added successfully!", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);

                    onBackPressed();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                    Toast.makeText(AddReportActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                }
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