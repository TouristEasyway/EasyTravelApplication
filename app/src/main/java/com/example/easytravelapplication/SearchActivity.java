package com.example.easytravelapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.easytravelapplication.Adapter.ManageCarAdapter;
import com.example.easytravelapplication.Adapter.ManagePackageAdapter;
import com.example.easytravelapplication.Model.ManageCarResponse;
import com.example.easytravelapplication.Model.PackageListResponse;
import com.example.easytravelapplication.Utils.AppConstant;
import com.example.easytravelapplication.Utils.CommonMethod;
import com.example.easytravelapplication.databinding.ActivitySearchBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    ActivitySearchBinding binding;
    ArrayList<ManageCarResponse> carArrayList;
    ArrayList<PackageListResponse> packageArrayList;
    ProgressDialog pd;
    SharedPreferences sp;
    String userType;

    private ArrayList<String> cityList = new ArrayList<>();
    String city = "";
    String stringQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);

        getSupportActionBar().setTitle("Global Search");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getSharedPreferences(AppConstant.PREF, Context.MODE_PRIVATE);
        userType = sp.getString(AppConstant.USERTYPE, "");

        cityList.add("City");
        cityList.add("Ahmedabad");
        cityList.add("Dahod");
        cityList.add("Surat");

        ArrayAdapter cityAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, cityList);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        binding.citySpinner.setAdapter(cityAdapter);

        binding.citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (cityList.get(i).equalsIgnoreCase("City")) {
                    city = "";
                } else {
                    city = cityList.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                stringQuery = String.valueOf(editable);
            }
        });

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (stringQuery.equals("")) {
                    new CommonMethod(SearchActivity.this, "Please Enter Price For Search");
                } else {
                    pd = new ProgressDialog(SearchActivity.this);
                    pd.setMessage("Please Wait...");
                    pd.setCancelable(false);
                    pd.show();
                    getCarData(stringQuery, city);
                    getPackageData(stringQuery, city);
                }
            }
        });
    }

    private void getPackageData(String query, String city) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Manage Packages");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    packageArrayList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        PackageListResponse data = snapshot.getValue(PackageListResponse.class);
                        if (data != null) {
                            if (query!= "" && city!="") {
                                Log.d("RESPONSE",query+"_______"+data.getPrice()+"______"+city+"____"+data.getCity());
                                if ((Integer.parseInt(query) >= Integer.parseInt(data.getPrice())) && city.equalsIgnoreCase(data.getCity())) {
                                    packageArrayList.add(data);
                                    ManagePackageAdapter adapter = new ManagePackageAdapter(packageArrayList, SearchActivity.this);
                                    binding.rvPackage.setVisibility(View.VISIBLE);
                                    binding.noPackageData.setVisibility(View.GONE);
                                    binding.rvPackage.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    packageArrayList.remove(data);
                                }
                            }
                            else {
                                if (Integer.parseInt(query) >= Integer.parseInt(data.getPrice())) {
                                    packageArrayList.add(data);
                                    ManagePackageAdapter adapter = new ManagePackageAdapter(packageArrayList, SearchActivity.this);
                                    binding.rvPackage.setVisibility(View.VISIBLE);
                                    binding.noPackageData.setVisibility(View.GONE);
                                    binding.rvPackage.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    packageArrayList.remove(data);
                                }
                            }
                            pd.dismiss();
                        } else {
                            binding.rvPackage.setVisibility(View.GONE);
                            binding.noPackageData.setVisibility(View.VISIBLE);
                            pd.dismiss();
                        }
                    }
                } else {
                    binding.rvPackage.setVisibility(View.GONE);
                    binding.noPackageData.setVisibility(View.VISIBLE);
                    pd.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.noPackageData.setVisibility(View.VISIBLE);
                binding.rvPackage.setVisibility(View.GONE);
                pd.dismiss();
            }
        });
    }

    private void getCarData(String query, String city) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Manage Car");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    carArrayList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ManageCarResponse data = snapshot.getValue(ManageCarResponse.class);
                        if (data != null) {
                            Log.d("RESPONSE_QUERY",query+"_____"+city+"______");
                            if (query!= "" && city!="") {
                                if ((Integer.parseInt(query) >= Integer.parseInt(data.getRatePerKM())) && city.equalsIgnoreCase(data.getCity())) {
                                    carArrayList.add(data);
                                    ManageCarAdapter adapter = new ManageCarAdapter(SearchActivity.this, carArrayList, userType);
                                    binding.rvCar.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                    binding.rvCar.setVisibility(View.VISIBLE);
                                    binding.noCarData.setVisibility(View.GONE);
                                } else {
                                    carArrayList.remove(data);
                                }
                            } else {
                                if (Integer.parseInt(query) >= Integer.parseInt(data.getRatePerKM())) {
                                    carArrayList.add(data);
                                    ManageCarAdapter adapter = new ManageCarAdapter(SearchActivity.this, carArrayList, userType);
                                    binding.rvCar.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                    binding.rvCar.setVisibility(View.VISIBLE);
                                    binding.noCarData.setVisibility(View.GONE);
                                } else {
                                    carArrayList.remove(data);
                                }
                            }


                            pd.dismiss();
                        } else {
                            binding.rvCar.setVisibility(View.GONE);
                            binding.noCarData.setVisibility(View.VISIBLE);
                            pd.dismiss();
                        }
                    }
                } else {
                    binding.rvCar.setVisibility(View.GONE);
                    binding.noCarData.setVisibility(View.VISIBLE);
                    pd.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.rvCar.setVisibility(View.GONE);
                binding.noCarData.setVisibility(View.VISIBLE);
                pd.dismiss();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}