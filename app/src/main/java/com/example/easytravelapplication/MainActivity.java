package com.example.easytravelapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easytravelapplication.Adapter.UserCarAdapter;
import com.example.easytravelapplication.Adapter.UserCarHistoryAdapter;
import com.example.easytravelapplication.Adapter.UserDashBoardPackageAdapter;
import com.example.easytravelapplication.Adapter.UserPackageHistoryAdapter;
import com.example.easytravelapplication.Model.CarHistoryResponse;
import com.example.easytravelapplication.Model.ManageCarResponse;
import com.example.easytravelapplication.Model.PackageHistoryListResponse;
import com.example.easytravelapplication.Model.PackageListResponse;
import com.example.easytravelapplication.Utils.AppConstant;
import com.example.easytravelapplication.Utils.CommonMethod;
import com.example.easytravelapplication.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth auth;
    SharedPreferences sp;
    HomeAdapter adapter;
    DatabaseReference reference;
    private ProgressDialog pd;


    String[] adminList = {"Manage User", "Manage Package", "Package Purchase History", "Manage Hotel", "Manage Car Rents", "Car Rental History"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        sp = getSharedPreferences(AppConstant.PREF, Context.MODE_PRIVATE);
        sp.getString(AppConstant.USERTYPE, "");
        String userType = sp.getString(AppConstant.USERTYPE, "");
        pd = new ProgressDialog(this);

        if (userType.equals("User")) {
            getSupportActionBar().setTitle("Home");
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
            setPackageData();
            setPackageHistoryData();
//            setHotelData();
//            setHotelHistoryData();
            setCarData();
            setCarHistoryData();


//            adapter = new HomeAdapter(this, userList);
        } else {
            getSupportActionBar().setTitle("Admin");
            binding.rvDashboard.setVisibility(View.VISIBLE);
            adapter = new HomeAdapter(this, adminList);
            binding.rvDashboard.setAdapter(adapter);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        return true;
                    case R.id.package1:
                        startActivity(new Intent(getApplicationContext(), ManagePackageActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.car:
                        startActivity(new Intent(getApplicationContext(), ManageCarRentActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    private void setCarHistoryData() {

        binding.rvCarHistory.setVisibility(View.VISIBLE);
        binding.tvCarHistory.setVisibility(View.VISIBLE);
        binding.carHistoryViewAll.setVisibility(View.VISIBLE);
        reference = FirebaseDatabase.getInstance().getReference("Book Car");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<CarHistoryResponse> arrayList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        CarHistoryResponse data = snapshot.getValue(CarHistoryResponse.class);
                        arrayList.add(data);
                    }

                    UserCarHistoryAdapter adapter = new UserCarHistoryAdapter(arrayList);
                    binding.rvCarHistory.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } else {
                    binding.rvCarHistory.setVisibility(View.VISIBLE);
                    binding.noDataCarHistory.setVisibility(View.VISIBLE);
                }
                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                pd.dismiss();

            }
        });
        binding.carHistoryViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(MainActivity.this, CarRentalHistoryActivity.class);
            }
        });
    }

    private void setCarData() {
        binding.rvCar.setVisibility(View.VISIBLE);
        binding.tvCar.setVisibility(View.VISIBLE);
        binding.carViewAll.setVisibility(View.VISIBLE);

        reference = FirebaseDatabase.getInstance().getReference("Manage Car");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<ManageCarResponse> arrayList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ManageCarResponse data = snapshot.getValue(ManageCarResponse.class);
                        arrayList.add(data);
                    }

                    UserCarAdapter adapter = new UserCarAdapter(arrayList);
                    binding.rvCar.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    pd.dismiss();

                } else {
                    pd.dismiss();
                    binding.noDataCar.setVisibility(View.VISIBLE);
                    binding.rvCar.setVisibility(View.GONE);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                pd.dismiss();

            }
        });
        binding.carViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(MainActivity.this, ManageCarRentActivity.class);
            }
        });
    }

    private void setPackageHistoryData() {
        binding.rvPackageHistory.setVisibility(View.VISIBLE);
        binding.tvPackageHistory.setVisibility(View.VISIBLE);
        binding.packageHistoryViewAll.setVisibility(View.VISIBLE);
        reference = FirebaseDatabase.getInstance().getReference("Book Packages");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<PackageHistoryListResponse> arrayList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        PackageHistoryListResponse data = snapshot.getValue(PackageHistoryListResponse.class);
                        arrayList.add(data);
                    }

                    UserPackageHistoryAdapter adapter = new UserPackageHistoryAdapter(arrayList);
                    binding.rvPackageHistory.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    pd.dismiss();

                } else {
                    binding.noDataPacakgeHistory.setVisibility(View.VISIBLE);
                    binding.rvPackageHistory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                pd.dismiss();

            }
        });
        binding.packageHistoryViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(MainActivity.this, PackagePurchaseHistoryActivity.class);
            }
        });
    }

    private void setPackageData() {
        binding.rvPackage.setVisibility(View.VISIBLE);
        binding.tvPackage.setVisibility(View.VISIBLE);
        binding.packageViewAll.setVisibility(View.VISIBLE);
        reference = FirebaseDatabase.getInstance().getReference("Manage Packages");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<PackageListResponse> arrayList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        PackageListResponse data = snapshot.getValue(PackageListResponse.class);
                        arrayList.add(data);
                    }

                    UserDashBoardPackageAdapter adapter = new UserDashBoardPackageAdapter(arrayList);
                    binding.rvPackage.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    pd.dismiss();

                } else {
                    binding.noDataPacakge.setVisibility(View.VISIBLE);
                    binding.rvPackage.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                pd.dismiss();

            }
        });
        binding.packageViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(MainActivity.this, ManagePackageActivity.class);
            }
        });

    }

    private class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyHolder> {

        Context context;
        String[] arrayList;

        //Creating Parameterized Constructor
        public HomeAdapter(MainActivity activity, String[] arrayList) {
            this.context = activity;
            this.arrayList = arrayList;
        }

        //Use This Method For Set Custom XML File
        @NonNull
        @Override
        public HomeAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
            return new HomeAdapter.MyHolder(view);
        }

        //Initialized Id Method For Connect XML File Id
        public class MyHolder extends RecyclerView.ViewHolder {

            TextView name;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.tv_name);
            }
        }

        //Initialized Method For Set Data
        @Override
        public void onBindViewHolder(@NonNull HomeAdapter.MyHolder holder, int position) {
            holder.name.setText(arrayList[position]);

            //Click Event On Specific CustomView
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (arrayList[position].equals("Manage User")) {
                        new CommonMethod(context, ManageUserActivity.class);
                    } else if (arrayList[position].equals("Manage Package") || arrayList[position].equals("Package")) {
                        new CommonMethod(context, ManagePackageActivity.class);
                    } else if (arrayList[position].equals("Package Purchase History")) {
                        new CommonMethod(context, PackagePurchaseHistoryActivity.class);
                    } else if (arrayList[position].equals("Manage Hotel")) {
                        new CommonMethod(context, ManageHotelActivity.class);
                    } else if (arrayList[position].equals("Manage Car Rents")) {
                        new CommonMethod(context, ManageCarRentActivity.class);
                    } else if (arrayList[position].equals("Car Rental History")) {
                        new CommonMethod(context, CarRentalHistoryActivity.class);
                    } else {

                    }
                }
            });

        }

        //Define Array Size
        @Override
        public int getItemCount() {
            return arrayList.length;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.opetion_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.icSearch) {
            startActivity(new Intent(this, SearchActivity.class));
        }
        return true;
    }
}