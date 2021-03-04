package com.brit.britaniaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ContentFrameLayout;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.brit.britaniaapp.base.BaseNavActivity;

import java.util.ArrayList;

public class UserFilterActivity extends BaseNavActivity {

    String[] departments = {"Sales"};

    Report selectedReport=null;

    private ArrayList<Report> reports=new ArrayList<>();
    String[] reportNames = {
            "NSM REPORT",
            "JAN 2020TOMAR2020",
            "4MonthNSMULPO",
            "202001_202003_UDDPUL",
            "CLOSEINGSTOCK",
            "PRIMARYVSSECONDARY",
            "EASTTEMPLATE",
            "RPDBilledReport",
            "JUL2020TOSEP2020",
            "BusinessReport"};

    Spinner spDepartment;
    Spinner spReportName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContentFrameLayout contentFrameLayout = findViewById(R.id.base_nav_content_section);
        getLayoutInflater().inflate(R.layout.activity_user_filter, contentFrameLayout);

//        reports.add(new Report("NSM REPORT","https://reports.britindia.com/#/views/4MonthsNSMReport/4MonthsNSMReport?:iid=1 "));
//        reports.add(new Report("JAN 2020TOMAR2020","https://reports.britindia.com/#/views/JAN2020TOMAR2020/202001to202003?:iid=2"));
//        reports.add(new Report("4MonthNSMULPO","https://reports.britindia.com/#/views/4MonthNSMULPO/4MonthNSMULPO"));
//        reports.add(new Report("202001_202003_UDDPUL","https://reports.britindia.com/#/views/202001_202003_UDDPUL/JAN2020toMAR2020"));
//        reports.add(new Report("CLOSEINGSTOCK","https://reports.britindia.com/#/views/CLOSINGSTOCK/CLOSINGSTOCK"));
//        reports.add(new Report("PRIMARYVSSECONDARY","https://reports.britindia.com/#/views/PRIMARYVSSECONDARY/PRIMARYVSSECONDARY?:iid=5"));
//        reports.add(new Report("AI-PRIMARYVSSECONDARY","https://reports.britindia.com/#/workbooks/1041/views"));
//        reports.add(new Report("EASTTEMPLATE","https://reports.britindia.com/#/views/EASTTEMPLATE/EastTemplate?:iid=6"));
//        reports.add(new Report("RPDBilledReport","https://reports.britindia.com/#/views/RPDBilledOutletsReport/RPDBilledReport?:iid=5"));
//        reports.add(new Report("JUL2020TOSEP2020","https://reports.britindia.com/#/views/JUL2020TOSEP2020/202007to202009?:iid=9"));
//        reports.add(new Report("BusinessReport","https://reports.britindia.com/#/views/BusinessReport-"));

        spDepartment = findViewById(R.id.spDepartment);
        spReportName = findViewById(R.id.spReportName);

        ((Button) findViewById(R.id.listReport)).setOnClickListener(view -> {
            Intent intent =new Intent(this, ChartListActivity.class);
            intent.putExtra("report",selectedReport);
            startActivity(intent);
        });

        spDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spReportName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedReport= reports.get(adapterView.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aDepartment = new ArrayAdapter(this, R.layout.spinner_item, departments);
        aDepartment.setDropDownViewResource(R.layout.spinner_dropdown);
        //Setting the ArrayAdapter data on the Spinner
        spDepartment.setAdapter(aDepartment);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aPeportName = new ArrayAdapter(this, R.layout.spinner_item, reportNames);
        aPeportName.setDropDownViewResource(R.layout.spinner_dropdown);
        //Setting the ArrayAdapter data on the Spinner
        spReportName.setAdapter(aPeportName);

    }
}