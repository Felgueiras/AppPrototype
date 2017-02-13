package com.example.rafael.appprototype.Patients.ReviewEvaluation.ReviewSingleTest;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.Patient;
import com.example.rafael.appprototype.DataTypes.Scales;
import com.example.rafael.appprototype.Main.GridSpacingItemDecoration;
import com.example.rafael.appprototype.R;


/**
 * Create the Card for each of the Tests of a Session
 */
public class ReviewArea extends RecyclerView.Adapter<ReviewArea.TestCardHolder> {

    /**
     * Session for the Tests.
     */
    private final Session session;
    /**
     * Patient for this Session
     */
    private final Patient patient;
    /**
     * Context.
     */
    private Context context;
    /**
     * Name of Test being displayed.
     */
    private String testName;


    /**
     * Default constructor for the RV adapter.
     *
     * @param context
     * @param session
     */
    public ReviewArea(Context context, Session session) {
        this.context = context;
        this.session = session;
        this.patient = session.getPatient();
    }


    /**
     * CGACardHolder class.
     */
    public class TestCardHolder extends RecyclerView.ViewHolder {
        public TextView area;
        public RecyclerView scales;
        /**
         * Test card view.
         */
        public View testCard;

        /**
         * Create a CGACardHolder from a View
         *
         * @param view
         */
        public TestCardHolder(View view) {
            super(view);
            area = (TextView) view.findViewById(R.id.area);
            scales = (RecyclerView) view.findViewById(R.id.area_scales);
        }
    }


    /**
     * Create a CGACardHolder from a View
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public TestCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // get the Test CardView
        final View testCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_review_area, parent, false);

        return new TestCardHolder(testCard);
    }

    /**
     * Display the data at a certain position
     *
     * @param holder   data to be displayed
     * @param position position to "put" the data
     */
    @Override
    public void onBindViewHolder(final TestCardHolder holder, int position) {
        String area = Constants.cga_areas[position];
        holder.area.setText(area);

        // check if the session had any scale from this area
        if (Scales.getTestsForArea(session.getTestsFromSession(), area).size() == 0) {
            // TODO remove this content
        }

        /**
         * Show info about evaluations for every area.
         */
        ReviewScale adapter = new ReviewScale(context, session, Constants.cga_areas[position]);
        int numbercolumns = 1;
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, numbercolumns);
        holder.scales.setLayoutManager(mLayoutManager);
        holder.scales.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        holder.scales.setItemAnimator(new DefaultItemAnimator());
        holder.scales.setAdapter(adapter);
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    @Override
    public int getItemCount() {
        return Constants.cga_areas.length;
    }

}
