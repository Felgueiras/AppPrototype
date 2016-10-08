package com.example.rafael.appprototype.ViewPatientsTab;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.Main.MainActivity;
import com.example.rafael.appprototype.R;
import com.example.rafael.appprototype.ViewPatientsTab.SinglePatient.ViewSinglePatientFragment;

import java.util.ArrayList;

public class ViewPatientsGridAdapter extends RecyclerView.Adapter<ViewPatientsGridAdapter.MyViewHolder> {

    private Context context;
    /**
     * Data to be displayed.
     */
    private ArrayList<Patient> patients;

    /**
     * Create a View
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, age;
        public ImageView photo, overflow;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.patientName);
            age = (TextView) view.findViewById(R.id.patientAge);
            photo = (ImageView) view.findViewById(R.id.patientPhoto);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }

    /**
     * Constructor of the ViewSinglePatientCardAdapter
     *
     * @param context
     * @param patients
     */
    public ViewPatientsGridAdapter(Context context, ArrayList<Patient> patients) {
        this.context = context;
        this.patients = patients;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_patient_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Patient patient = patients.get(position);
        holder.name.setText(patient.getName());
        // holder.type.setText(patient.getAge());

        // loading album cover using Glide library
        Glide.with(context).load(patient.getPicture()).into(holder.photo);

        // add on click listener for the photo
        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Patients","Clicked photo");
                Bundle args = new Bundle();
                args.putSerializable(ViewSinglePatientFragment.PATIENT, patient);
                ((MainActivity) context).replaceFragment(ViewSinglePatientFragment.class, args);
            }
        });

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(context, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(context, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }
}
