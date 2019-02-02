package com.example.rubenfilipe.spots;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.rubenfilipe.spots.model.FirebaseManager;
import com.example.rubenfilipe.spots.model.HistorySpot;
import com.example.rubenfilipe.spots.model.Spot;
import com.example.rubenfilipe.spots.model.User;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class StatisticsParkActivity extends AppCompatActivity {
    private TextView txtRegisteredUsers, txtAuthenticatedUsers, startDate, endDate;
    BarChart barChart;
    PieChart parqueAGraph, parqueBGraph, parqueCGraph;
    Spinner spinner, spinner2;
    PieChart graphDate;
    private ArrayList<String> labels = new ArrayList<>();
    private float ocupacaoParqueA, ocupacaoParqueB, ocupacaoParqueC;
    private LinkedList<HistorySpot> spots = new LinkedList<>();
    private LinkedList<HistorySpot> spotsFiltered = new LinkedList<>();
    private Date dateStart, dateEnd, date;
    private ArrayList<BarEntry> yVals = new ArrayList<>();
    private float numeroTotalLugarParqueA = 0, numeroTotalLugarParqueB = 0, numeroTotalLugarParqueC = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_park);

        Toolbar toolbar = findViewById(R.id.toolbarStatistics);
        setSupportActionBar(toolbar);

        setTitle("Statistics");

        barChart = (BarChart) findViewById(R.id.barChart);
        parqueAGraph = (PieChart) findViewById(R.id.parqueAGraph);
        parqueBGraph = (PieChart) findViewById(R.id.parqueBGraph);
        parqueCGraph = (PieChart) findViewById(R.id.parqueCGraph);
        graphDate = findViewById(R.id.graphDate);
        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);
        spinner2 = findViewById(R.id.spinnerPark2);

        getOccupiancyRateByPark();

        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("All parks");
        spinnerArray.add("Parque A");
        spinnerArray.add("Parque B");
        spinnerArray.add("Parque C");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = findViewById(R.id.spinnerPark);
        spinner.setAdapter(adapter);
        spinner2.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    parqueAGraph.setVisibility(View.INVISIBLE);
                    parqueBGraph.setVisibility(View.INVISIBLE);
                    parqueCGraph.setVisibility(View.INVISIBLE);
                    graphDate.setVisibility(View.INVISIBLE);
                    barChart.setVisibility(View.VISIBLE);

                    ArrayList<BarEntry> yVals = new ArrayList<>();
                    yVals.add(new BarEntry(1, ocupacaoParqueA, "Parque A"));
                    yVals.add(new BarEntry(2, ocupacaoParqueB, "Parque B"));
                    yVals.add(new BarEntry(3, ocupacaoParqueC, "Parque C"));

                    BarDataSet barDataSet = new BarDataSet(yVals, "Parque A, Parque B, Parque C");
                    barDataSet.setDrawIcons(false);
                    barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

                    BarData data = new BarData(barDataSet);
                    barChart.getAxisRight().setEnabled(false);
                    barChart.getXAxis().setEnabled(false);
                    barChart.getAxisLeft().setAxisMaximum(100);
                    barChart.getAxisLeft().setAxisMinimum(0f);

                    barChart.getLegend().setFormSize(15);

                    barChart.setData(data);
                    barChart.setFitBars(true);
                    barChart.getDescription().setEnabled(false);
                    barChart.invalidate();
                }
                if (position == 1) {
                    barChart.setVisibility(View.INVISIBLE);
                    parqueAGraph.setVisibility(View.VISIBLE);
                    parqueBGraph.setVisibility(View.INVISIBLE);
                    parqueCGraph.setVisibility(View.INVISIBLE);
                    graphDate.setVisibility(View.INVISIBLE);

                    parqueAGraph.setUsePercentValues(true);
                    parqueAGraph.getDescription().setEnabled(false);
                    parqueAGraph.setDrawHoleEnabled(true);
                    barChart.getXAxis().setEnabled(false);
                    parqueAGraph.setHoleColor(Color.WHITE);
                    parqueAGraph.setTransparentCircleRadius(61f);
                    ArrayList<PieEntry> yVals1 = new ArrayList<>();
                    yVals1.add(new PieEntry(ocupacaoParqueA, "Occupancy rate Parque A"));
                    yVals1.add(new PieEntry(100 - ocupacaoParqueA, "Percentage of free Spots"));


                    PieDataSet dataSet1 = new PieDataSet(yVals1, "Parque A");
                    dataSet1.setSliceSpace(3f);
                    dataSet1.setSelectionShift(5f);
                    dataSet1.setColors(Color.rgb(200, 0, 0), Color.rgb(0, 130, 0));

                    PieData data1 = new PieData(dataSet1);
                    data1.setValueTextSize(10f);
                    data1.setValueTextColor(Color.BLACK);
                    parqueAGraph.getLegend().setFormSize(30);
                    parqueAGraph.setCenterTextColor(Color.BLACK);


                    parqueAGraph.setData(data1);

                }
                if (position == 2) {
                    parqueAGraph.invalidate();
                    parqueAGraph.clear();

                    barChart.setVisibility(View.INVISIBLE);
                    parqueAGraph.setVisibility(View.INVISIBLE);
                    parqueCGraph.setVisibility(View.INVISIBLE);
                    parqueBGraph.setVisibility(View.VISIBLE);
                    graphDate.setVisibility(View.INVISIBLE);


                    parqueBGraph.setUsePercentValues(true);
                    parqueBGraph.getDescription().setEnabled(false);
                    barChart.getXAxis().setEnabled(false);
                    parqueBGraph.setDrawHoleEnabled(true);
                    parqueBGraph.setHoleColor(Color.WHITE);
                    parqueBGraph.setTransparentCircleRadius(61f);
                    ArrayList<PieEntry> yVals2 = new ArrayList<>();
                    yVals2.add(new PieEntry(ocupacaoParqueB, "Occupancy Parque B"));
                    yVals2.add(new PieEntry(100 - ocupacaoParqueB, "Percentage of free Spots"));


                    PieDataSet dataSet2 = new PieDataSet(yVals2, "Parque B");
                    dataSet2.setSliceSpace(3f);
                    dataSet2.setSelectionShift(5f);
                    dataSet2.setColors(Color.rgb(200, 0, 0), Color.rgb(0, 130, 0));

                    PieData data2 = new PieData(dataSet2);
                    data2.setValueTextSize(10f);
                    data2.setValueTextColor(Color.BLACK);
                    parqueBGraph.getLegend().setFormSize(30);
                    parqueBGraph.setCenterTextColor(Color.BLACK);


                    parqueBGraph.setData(data2);
                }
                if (position == 3) {
                    barChart.setVisibility(View.INVISIBLE);
                    parqueAGraph.setVisibility(View.INVISIBLE);
                    parqueBGraph.setVisibility(View.INVISIBLE);
                    parqueCGraph.setVisibility(View.VISIBLE);
                    graphDate.setVisibility(View.INVISIBLE);


                    parqueCGraph.setUsePercentValues(true);
                    parqueCGraph.getDescription().setEnabled(false);
                    parqueCGraph.setDrawHoleEnabled(true);
                    barChart.getXAxis().setEnabled(false);
                    parqueCGraph.setHoleColor(Color.WHITE);
                    parqueCGraph.setTransparentCircleRadius(61f);
                    ArrayList<PieEntry> yVals3 = new ArrayList<>();
                    yVals3.add(new PieEntry(ocupacaoParqueC, "Occupancy Parque C"));
                    yVals3.add(new PieEntry(100 - ocupacaoParqueC, "Percentage of free Spots"));

                    PieDataSet dataSet3 = new PieDataSet(yVals3, "Parque C");
                    dataSet3.setSliceSpace(3f);
                    dataSet3.setSelectionShift(5f);
                    dataSet3.setColors(Color.rgb(200, 0, 0), Color.rgb(0, 130, 0));

                    PieData data3 = new PieData(dataSet3);
                    data3.setValueTextSize(10f);
                    data3.setValueTextColor(Color.BLACK);
                    parqueCGraph.getLegend().setFormSize(30);
                    parqueCGraph.setCenterTextColor(Color.BLACK);


                    parqueCGraph.setData(data3);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.dashboardMenu:
                FirebaseAuth.getInstance().getCurrentUser();
                finish();
                startActivity(new Intent(this, DashboardActivity.class));
                break;
            case R.id.menuLogout:
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String uid = mAuth.getCurrentUser().getUid();
                FirebaseDatabase.getInstance().getReference("Users").child(uid).child("loggedIn").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseAuth.getInstance().signOut();
                            finish();
                            startActivity(new Intent(StatisticsParkActivity.this, GuestDashboardActivity.class));
                        }
                    }
                });

                break;
            case R.id.profileID:
                FirebaseAuth.getInstance().getCurrentUser();
                finish();
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.statistics:
                FirebaseAuth.getInstance().getCurrentUser();
                finish();
                startActivity(new Intent(this, StatisticsParkActivity.class));
                break;
            case R.id.favorites:
                FirebaseAuth.getInstance().getCurrentUser();
                finish();
                startActivity(new Intent(this, MyFavoritesActivity.class));
                break;
        }
        return true;
    }

    public void getOccupiancyRateByPark() {
        final LinkedList<Spot> spots = new LinkedList<>();

        DatabaseReference mDatabase = FirebaseDatabase.
                getInstance().
                getReference("Spots");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                float lugaresOcupadosParqueA = 0, lugaresOcupadosParqueB = 0, lugaresOcupadosParqueC = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Spot spot = postSnapshot.getValue(Spot.class);
                    spots.add(spot);

                }
                for (Spot s : spots) {
                    if (s.getParkId() == 1) {
                        numeroTotalLugarParqueA++;
                        if (!s.getAvailable()) {
                            lugaresOcupadosParqueA++;

                        }
                    } else if (s.getParkId() == 2) {
                        numeroTotalLugarParqueB++;
                        if (!s.getAvailable()) {
                            lugaresOcupadosParqueB++;
                        }
                    } else if (s.getParkId() == 3) {
                        numeroTotalLugarParqueC++;
                        if (!s.getAvailable()) {
                            lugaresOcupadosParqueC++;
                        }
                    }
                }

                ocupacaoParqueA = (lugaresOcupadosParqueA / numeroTotalLugarParqueA) * 100;
                ocupacaoParqueB = (lugaresOcupadosParqueB / numeroTotalLugarParqueB) * 100;
                ocupacaoParqueC = (lugaresOcupadosParqueC / numeroTotalLugarParqueC) * 100;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void graphSearchByDate(View view) {
        DatabaseReference mDatabase = FirebaseDatabase.
                getInstance().
                getReference("HistorySpot");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                yVals.clear();
                spots.clear();
                labels.clear();
                spotsFiltered.clear();
                graphDate.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    spots.add(postSnapshot.getValue(HistorySpot.class));
                }

                for (HistorySpot s : spots) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        dateStart = dateFormat.parse(startDate.getText().toString());
                        dateEnd = dateFormat.parse(endDate.getText().toString());
                        date = dateFormat.parse(s.getDate());

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (date.after(dateStart) && date.before(dateEnd)) {
                        spotsFiltered.add(s);
                    }
                }

                yVals.clear();
                labels.clear();
                graphDate.clear();

                int position=spinner2.getSelectedItemPosition();
                if(position == 0){
                    if(spotsFiltered != null) {
                        LinkedList<HistorySpot> spotsPark = new LinkedList<>();
                        LinkedList<HistorySpot> sizesPark = new LinkedList<>();
                        for (HistorySpot s : spotsFiltered) {
                            if (s.getIdPark() == 1 || s.getIdPark() == 2 || s.getIdPark() ==3) {
                                sizesPark.add(s);
                                if (s.isAvailable()) {
                                    spotsPark.add(s);
                                }
                            }

                        }
                        barChart.setVisibility(View.INVISIBLE);
                        parqueAGraph.setVisibility(View.INVISIBLE);
                        parqueBGraph.setVisibility(View.INVISIBLE);
                        parqueCGraph.setVisibility(View.INVISIBLE);

                        graphDate.setUsePercentValues(true);
                        graphDate.getDescription().setEnabled(false);
                        graphDate.setDrawHoleEnabled(true);
                        graphDate.setHoleColor(Color.WHITE);
                        graphDate.setTransparentCircleRadius(61f);
                        ArrayList<PieEntry> yValsGraphDate = new ArrayList<>();
                        float avg = (spotsPark.size() * 100) / sizesPark.size();
                        yValsGraphDate.add(new PieEntry(100 - avg, "Occupancy all parks"));
                        yValsGraphDate.add(new PieEntry(avg, "Percentage of free Spots"));

                        PieDataSet dataSetyValsGraphDate = new PieDataSet(yValsGraphDate, "All parks");
                        dataSetyValsGraphDate.setSliceSpace(3f);
                        dataSetyValsGraphDate.setSelectionShift(5f);
                        dataSetyValsGraphDate.setColors(Color.rgb(200, 0, 0), Color.rgb(0, 130, 0));

                        PieData dataGraphDate = new PieData(dataSetyValsGraphDate);
                        dataGraphDate.setValueTextSize(10f);
                        dataGraphDate.setValueTextColor(Color.BLACK);
                        graphDate.getLegend().setFormSize(30);
                        graphDate.setCenterTextColor(Color.BLACK);
                        graphDate.setVisibility(View.VISIBLE);
                        graphDate.setData(dataGraphDate);
                    }else{
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(StatisticsParkActivity.this);
                        if (!isFinishing()) {
                            builder.setMessage("No data found").setTitle("Statistics")
                                    .setNegativeButton("No", dialogClickListener).show();

                        }


                    }
                }

                if(position ==1){
                    if(spotsFiltered != null) {
                        LinkedList<HistorySpot> spotsParkA = new LinkedList<>();
                        LinkedList<HistorySpot> sizesParkA = new LinkedList<>();

                        for (HistorySpot s : spotsFiltered) {
                            if (s.getIdPark() == 1) {
                                sizesParkA.add(s);
                                if (s.isAvailable()) {
                                    spotsParkA.add(s);
                                }
                            }

                        }
                        if(sizesParkA.size() != 0) {
                            barChart.setVisibility(View.INVISIBLE);
                            parqueAGraph.setVisibility(View.INVISIBLE);
                            parqueBGraph.setVisibility(View.INVISIBLE);
                            parqueCGraph.setVisibility(View.INVISIBLE);

                            graphDate.setUsePercentValues(true);
                            graphDate.getDescription().setEnabled(false);
                            graphDate.setDrawHoleEnabled(true);
                            graphDate.setHoleColor(Color.WHITE);
                            graphDate.setTransparentCircleRadius(61f);
                            ArrayList<PieEntry> yValsGraphDate = new ArrayList<>();
                            float avg = 0;
                            if(spotsParkA.size() != 0) {
                                 avg = (spotsParkA.size() * 100) / sizesParkA.size();
                            }
                            yValsGraphDate.add(new PieEntry( 100-avg, "Occupancy Park A"));
                            yValsGraphDate.add(new PieEntry(avg, "Percentage of free Spots"));

                            PieDataSet dataSetyValsGraphDate = new PieDataSet(yValsGraphDate, "All parks");
                            dataSetyValsGraphDate.setSliceSpace(3f);
                            dataSetyValsGraphDate.setSelectionShift(5f);
                            dataSetyValsGraphDate.setColors(Color.rgb(200, 0, 0), Color.rgb(0, 130, 0));

                            PieData dataGraphDate = new PieData(dataSetyValsGraphDate);
                            dataGraphDate.setValueTextSize(10f);
                            dataGraphDate.setValueTextColor(Color.BLACK);
                            graphDate.getLegend().setFormSize(30);
                            graphDate.setCenterTextColor(Color.BLACK);
                            graphDate.setVisibility(View.VISIBLE);
                            graphDate.setData(dataGraphDate);
                        }else{
                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_NEGATIVE:
                                            break;
                                    }
                                }
                            };
                            AlertDialog.Builder builder = new AlertDialog.Builder(StatisticsParkActivity.this);
                            if (!isFinishing()) {
                                builder.setMessage("No data found").setTitle("Statistics")
                                        .setNegativeButton("No", dialogClickListener).show();

                            }
                        }
                    }else{
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(StatisticsParkActivity.this);
                        if (!isFinishing()) {
                            builder.setMessage("No data found").setTitle("Statistics")
                                    .setNegativeButton("No", dialogClickListener).show();

                        }

                    }
                }

                if(position == 2){
                    if(spotsFiltered != null) {
                        LinkedList<HistorySpot> spotsParkB = new LinkedList<>();
                        LinkedList<HistorySpot> sizesParkB = new LinkedList<>();

                        for (HistorySpot s : spotsFiltered) {
                            if (s.getIdPark() == 2) {
                                sizesParkB.add(s);
                                if (s.isAvailable()) {
                                    spotsParkB.add(s);
                                }
                            }

                        }
                        if(sizesParkB.size()!=0) {
                            barChart.setVisibility(View.INVISIBLE);
                            parqueAGraph.setVisibility(View.INVISIBLE);
                            parqueBGraph.setVisibility(View.INVISIBLE);
                            parqueCGraph.setVisibility(View.INVISIBLE);

                            graphDate.setUsePercentValues(true);
                            graphDate.getDescription().setEnabled(false);
                            graphDate.setDrawHoleEnabled(true);
                            graphDate.setHoleColor(Color.WHITE);
                            graphDate.setTransparentCircleRadius(61f);
                            ArrayList<PieEntry> yValsGraphDate = new ArrayList<>();
                            float avg = 0;
                            if(spotsParkB.size() != 0) {
                                 avg = (spotsParkB.size() * 100) / sizesParkB.size();
                                System.out.println("avg"+avg);
                                System.out.println("spots"+spotsParkB.size());
                            }
                            yValsGraphDate.add(new PieEntry( 100-avg, "Occupancy Park B"));
                            yValsGraphDate.add(new PieEntry(avg, "Percentage of free Spots"));

                            PieDataSet dataSetyValsGraphDate = new PieDataSet(yValsGraphDate, "All parks");
                            dataSetyValsGraphDate.setSliceSpace(3f);
                            dataSetyValsGraphDate.setSelectionShift(5f);
                            dataSetyValsGraphDate.setColors(Color.rgb(200, 0, 0), Color.rgb(0, 130, 0));

                            PieData dataGraphDate = new PieData(dataSetyValsGraphDate);
                            dataGraphDate.setValueTextSize(10f);
                            dataGraphDate.setValueTextColor(Color.BLACK);
                            graphDate.getLegend().setFormSize(30);
                            graphDate.setCenterTextColor(Color.BLACK);
                            graphDate.setVisibility(View.VISIBLE);
                            graphDate.setData(dataGraphDate);
                        }else{
                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_NEGATIVE:
                                            break;
                                    }
                                }
                            };
                            AlertDialog.Builder builder = new AlertDialog.Builder(StatisticsParkActivity.this);
                            if (!isFinishing()) {
                                builder.setMessage("No data found").setTitle("Statistics")
                                        .setNegativeButton("No", dialogClickListener).show();

                            }
                        }
                    }else{
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(StatisticsParkActivity.this);
                        if (!isFinishing()) {
                            builder.setMessage("No data found").setTitle("Statistics")
                                    .setNegativeButton("No", dialogClickListener).show();

                        }

                    }
                }
                if(position == 3){
                    if(spotsFiltered != null) {
                        LinkedList<HistorySpot> spotsParkC = new LinkedList<>();
                        LinkedList<HistorySpot> sizesParkC = new LinkedList<>();

                        for (HistorySpot s : spotsFiltered) {
                            if (s.getIdPark() == 3) {
                                sizesParkC.add(s);
                                if (s.isAvailable()) {
                                    spotsParkC.add(s);
                                }
                            }

                        }
                        if(sizesParkC.size()!=0) {
                            barChart.setVisibility(View.INVISIBLE);
                            parqueAGraph.setVisibility(View.INVISIBLE);
                            parqueBGraph.setVisibility(View.INVISIBLE);
                            parqueCGraph.setVisibility(View.INVISIBLE);

                            graphDate.setUsePercentValues(true);
                            graphDate.getDescription().setEnabled(false);
                            graphDate.setDrawHoleEnabled(true);
                            graphDate.setHoleColor(Color.WHITE);
                            graphDate.setTransparentCircleRadius(61f);
                            ArrayList<PieEntry> yValsGraphDate = new ArrayList<>();
                            float avg = 0;
                            if(spotsParkC.size() !=0) {
                                 avg = (spotsParkC.size() * 100) / sizesParkC.size();
                            }
                            yValsGraphDate.add(new PieEntry( 100-avg, "Occupancy Park C"));
                            yValsGraphDate.add(new PieEntry(avg, "Percentage of free Spots"));

                            PieDataSet dataSetyValsGraphDate = new PieDataSet(yValsGraphDate, "All parks");
                            dataSetyValsGraphDate.setSliceSpace(3f);
                            dataSetyValsGraphDate.setSelectionShift(5f);
                            dataSetyValsGraphDate.setColors(Color.rgb(200, 0, 0), Color.rgb(0, 130, 0));

                            PieData dataGraphDate = new PieData(dataSetyValsGraphDate);
                            dataGraphDate.setValueTextSize(10f);
                            dataGraphDate.setValueTextColor(Color.BLACK);
                            graphDate.getLegend().setFormSize(30);
                            graphDate.setCenterTextColor(Color.BLACK);
                            graphDate.setVisibility(View.VISIBLE);
                            graphDate.setData(dataGraphDate);
                        }else{
                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_NEGATIVE:
                                            break;
                                    }
                                }
                            };
                            AlertDialog.Builder builder = new AlertDialog.Builder(StatisticsParkActivity.this);
                            if (!isFinishing()) {
                                builder.setMessage("No data found").setTitle("Statistics")
                                        .setNegativeButton("No", dialogClickListener).show();

                            }
                        }
                    }else {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(StatisticsParkActivity.this);
                        if (!isFinishing()) {
                            builder.setMessage("No data found").setTitle("Statistics")
                                    .setNegativeButton("No", dialogClickListener).show();

                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
