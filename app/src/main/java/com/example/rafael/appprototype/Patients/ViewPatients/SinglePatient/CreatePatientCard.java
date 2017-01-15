package com.example.rafael.appprototype.Patients.ViewPatients.SinglePatient;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.Main.MainActivity;
import com.example.rafael.appprototype.Evaluations.NewEvaluation.NewEvaluation;
import com.example.rafael.appprototype.R;

import java.util.ArrayList;

public class CreatePatientCard extends RecyclerView.Adapter<CreatePatientCard.MyViewHolder> implements Filterable {

    private final ArrayList<Patient> filteredList;
    private Context context;
    /**
     * Data to be displayed.
     */
    private ArrayList<Patient> patients;
    private PatientsFilter patientsFilter;

    @Override
    public Filter getFilter() {
        if (patientsFilter == null)
            patientsFilter = new PatientsFilter(this, patients);
        return patientsFilter;
    }

    /**
     * Create a View
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, age;
        public ImageView photo, overflow;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.patientName);
            //age = (TextView) view.findViewById(R.id.patientAge);
            photo = (ImageView) view.findViewById(R.id.patientPhoto);
            //overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }

    /**
     * Constructor of the ReviewSessionCards
     *
     * @param context
     * @param patients
     */
    public CreatePatientCard(Context context, ArrayList<Patient> patients) {
        this.context = context;
        this.patients = patients;
        this.filteredList = new ArrayList<>();
        filteredList.addAll(patients);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_patient_info2, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Patient patient = filteredList.get(position);

        holder.name.setText(patient.getName());
        // holder.type.setText(patient.getAge());


        // loading album cover using Glide library
        //Glide.with(context).load(patient.getPicture()).into(holder.photo);

        // add on click listener for the photo


        holder.name.setOnClickListener(new View.OnClickListener() {
            public String patientTransitionName;

            @Override
            public void onClick(View v) {
                /**
                 * Pick a patient to be associated with a Session.
                 */

                if (Constants.selectPatient) {
                    Log.d("Patient", "Selected patient");
                    // go back to CreateSession
                    Bundle args = new Bundle();
                    args.putSerializable(NewEvaluation.PATIENT, patient);
                    args.putBoolean(NewEvaluation.SAVE_SESSION, true);
                    ((MainActivity) context).replaceFragment(new NewEvaluation(), args, "");
                    Constants.selectPatient = false;
                    return;
                } else {
                    // TODO add shared elements for transitions
                    Fragment endFragment = new ViewSinglePatientInfo();
                    /*
                    endFragment.setSharedElementReturnTransition(TransitionInflater.from(
                            context).inflateTransition(R.transition.change_image_trans));


                    endFragment.setSharedElementEnterTransition(TransitionInflater.from(
                            context).inflateTransition(R.transition.change_image_trans));
                    */


                    patientTransitionName = holder.name.getTransitionName();
                    Bundle args = new Bundle();
                    args.putString("ACTION", holder.name.getText().toString());
                    args.putString("TRANS_TEXT", patientTransitionName);
                    args.putSerializable(ViewSinglePatientInfo.PATIENT, patient);
                    ((MainActivity) context).replaceFragmentSharedElements(endFragment, args, Constants.tag_view_patien_info_records,
                            holder.name);
                }

            }
        });


        /*
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });
        */
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
        return filteredList.size();
    }

    /**
     * Class that allows for the Patients to be filtered by name.
     */
    private class PatientsFilter extends Filter {
        private final CreatePatientCard adapter;
        private final ArrayList<Patient> originalList;
        private final ArrayList<Patient> filteredList;

        public PatientsFilter(CreatePatientCard adapter, ArrayList<Patient> patients) {
            super();
            this.adapter = adapter;
            this.originalList = new ArrayList<>();
            originalList.addAll(patients);
            this.filteredList = new ArrayList<>();

        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();

                for (final Patient patient : originalList) {
                    if (patient.getName().toString().toLowerCase().trim().contains(filterPattern)) {
                        filteredList.add(patient);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            adapter.filteredList.clear();
            adapter.filteredList.addAll((ArrayList<Patient>) filterResults.values);
            adapter.notifyDataSetChanged();
        }
    }
}
