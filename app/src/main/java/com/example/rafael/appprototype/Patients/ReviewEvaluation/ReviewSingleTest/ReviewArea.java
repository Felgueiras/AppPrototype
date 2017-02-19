package com.example.rafael.appprototype.Patients.ReviewEvaluation.ReviewSingleTest;

import android.app.Activity;
import android.content.res.Resources;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.TextView;

import com.example.rafael.appprototype.Constants;
import com.example.rafael.appprototype.DataTypes.DB.Session;
import com.example.rafael.appprototype.DataTypes.DB.Patient;
import com.example.rafael.appprototype.DataTypes.Scales;
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
    private final boolean comparePrevious;
    /**
     * Context.
     */
    private Activity context;
    /**
     * Name of Test being displayed.
     */
    private String testName;


    /**
     * Default constructor for the RV adapter.
     *
     * @param context
     * @param session
     * @param comparePreviousSession
     */
    public ReviewArea(Activity context, Session session, boolean comparePreviousSession) {
        this.context = context;
        this.session = session;
        this.patient = session.getPatient();
        this.comparePrevious = comparePreviousSession;
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
            testCard = view;
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

        // check if the session had any scale from this area
        if (Scales.getTestsForArea(session.getScalesFromSession(), area).size() == 0) {
            ViewManager parentView = (ViewManager) holder.area.getParent();
            if (parentView != null) {
                parentView.removeView(holder.area);
                parentView.removeView(holder.scales);
            }
        } else {
            /**
             * Show info about evaluations for every area.
             */
            holder.area.setText(area);
            ReviewScale adapter = new ReviewScale(context, session, Constants.cga_areas[position], comparePrevious);
            int numbercolumns = 1;
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, numbercolumns);
            holder.scales.setLayoutManager(mLayoutManager);
            holder.scales.setItemAnimator(new DefaultItemAnimator());
            holder.scales.setAdapter(adapter);
        }

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
