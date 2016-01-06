package ml.mk.jm.ay.ak.studenttoolkit.calendar;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

import ml.mk.jm.ay.ak.studenttoolkit.R;
import ml.mk.jm.ay.ak.studenttoolkit.calendar.helper.TimeFormatHelper;
import ml.mk.jm.ay.ak.studenttoolkit.todo.ToDoActivity;

import static ml.mk.jm.ay.ak.studenttoolkit.calendar.helper.TimeFormatHelper.dayOfWeekConverter;

/**
 * Created by Specter
 *  MainActivity of Calendar
 */

public class MainActivity extends AppCompatActivity {

    private MyPagerAdapter pagerAdapter;
    private TabLayout tabLayout;
    public static NavigationView nvDrawer;
    private TextView tb_year_tv;
    private TextView tb_month_tv;
    private String tabTitles[] = new String[]{"Mon", "Tue ", "Wed", "Thu ", "Fri", "Sat", "Sun"};
    private ConditionalViewPager viewPager;

    /**
     * return Weekoffset value
     * @return weekofset
     */
    public int getWeekOffset() {
        return weekOffset;
    }

    private int weekOffset = 0;
    private final int MY_PERMISSIONS_REQUEST_READ_CALENDAR = 123;
    private final int MY_PERMISSIONS_REQUEST_WRITE_CALENDAR = 124;

    /**
     * return view mode state
     * @return editmode value
     */
    public boolean isEditMode() {
        return editMode;
    }

    private boolean editMode = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weekOffset = 0;
                refreshEvents();
                setToolbarTitle(tabLayout.getSelectedTabPosition());
                updaeTablayoutDate(tabLayout);
                Calendar day = Calendar.getInstance();
                TabLayout.Tab todayTab = tabLayout.getTabAt(dayOfWeekConverter(day.get(Calendar.DAY_OF_WEEK)));
                todayTab.select();
                Calendar today= Calendar.getInstance();
                int weekOfNow=today.get(Calendar.WEEK_OF_YEAR);
                Calendar startOfSemester = Calendar.getInstance();
                startOfSemester.set(2015, 8, 7);
                int weekStartOfSemester = startOfSemester.get(Calendar.WEEK_OF_YEAR);
                Log.i("week", "" + weekOfNow + " " + weekStartOfSemester);
                Snackbar.make(MainActivity.nvDrawer, "Week " +(weekOfNow-weekStartOfSemester+1), Snackbar.LENGTH_SHORT).show();
            }
        });

        tb_year_tv = (TextView) findViewById(R.id.tb_year);
        tb_month_tv = (TextView) findViewById(R.id.tb__month);
        setToolbarTitle(dayOfWeekConverter(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)));//initiallize toolbaltitle

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        /**
         *  fab long click listener    When user long click the FAB, mode will toggle in view mode and edit mode
         */
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onLongClick(View v) {
                editMode = !editMode;
                FloatingActionButton fab = (FloatingActionButton) v;
                if (editMode) {
                    fab.setImageResource(R.drawable.editmodeicon);
                } else {
                    fab.setImageResource(R.drawable.viewmodeicon);
                }
                viewPager.setEditmode(editMode);
                /**
                 *  phone should vibrate to give user a feedback
                 */
//                Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                vb.vibrate(200);
                v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                return false;
            }
        });
        /**
         *  bind swipe gesture to FAB
         */
        final GestureDetector gestureDetector = new GestureDetector(this, new MyGestureDetector());
        fab.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return gestureDetector.onTouchEvent(event);
                    }
                }
        );

        viewPager = (ConditionalViewPager) findViewById(R.id.pager);

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);



        nvDrawer = (NavigationView) findViewById(R.id.nav_view);
        setupDrawerContent(nvDrawer);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setToolbarTitle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

/**
 * requare permission to access user calendar data
 */
        loadData();
    }

    /**
     * require permision to show calendar data
     */
    void loadData() {
/**
 * request access calendar read permission
 */
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CALENDAR)) {

                explanPermission("We need you permission to show your calendar events!");
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CALENDAR},
                        MY_PERMISSIONS_REQUEST_READ_CALENDAR);
            }
        } else {
/**
 * request access calendar write permission
 */
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_CALENDAR)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_CALENDAR)) {
                    explanPermission("We need you permission to show your calendar events!");
                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_CALENDAR},
                            MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
                }
            } else {
                /**
                 * have gotten access calendar permission
                 */
                setupAdapter();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CALENDAR: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    setupAdapter();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    explanPermission("Calendar data is essential for this part, plear allow us access your calendar!");
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_WRITE_CALENDAR: { if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
                setupAdapter();
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                explanPermission("Calendar data is essential for this part, plear allow us access your calendar!");
            }
                return;
            }
        }
    }

    void explanPermission(String explanTxt){
        Snackbar.make(nvDrawer,explanTxt,
                Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.WRITE_CALENDAR},
                                MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
                    }
                })
                .show();
    }

    /**
     * showing calendar data after getting user's permission
     */
    void setupAdapter(){
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setSelectedTabIndicatorHeight(7);
        tabLayout.setupWithViewPager(viewPager);
        setTabLayout(tabLayout);
        /**
         *  make date bigger in selected tab
         */
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                TextView tvDate = (TextView) tab.getCustomView().findViewById(R.id.tv_date);
                tvDate.setTextSize(22);
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView tvDate = (TextView) tab.getCustomView().findViewById(R.id.tv_date);
                tvDate.setTextSize(15);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    /**
     * reload the pagerAdapter data
     */
    public void refreshEvents() {
        pagerAdapter.notifyDataSetChanged();
    }

    /**
     * set month and year in toolbar
     * @param position select page in viewpager and week offset
     */
    private void setToolbarTitle(int position) {
        tb_year_tv.setText(TimeFormatHelper.getTimeField(Calendar.YEAR, position));
        tb_month_tv.setText(TimeFormatHelper.getTimeField(Calendar.MONTH, position));
    }

    /**
     * initial the tab layout which shows  the date of each tab
     * @param tabLayout tablayout in activity
     */
    private void setTabLayout(TabLayout tabLayout) {
        Calendar day = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        long todayMillis = today.getTimeInMillis();
        day.add(Calendar.DATE, -dayOfWeekConverter(day.get(Calendar.DAY_OF_WEEK)) + 7 * weekOffset);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(R.layout.tab_of_tablayout);
            TextView tvDayOfWeek = (TextView) tab.getCustomView().findViewById(R.id.tv_day_of_week);
            tvDayOfWeek.setText(tabTitles[i]);
            TextView tvDate = (TextView) tab.getCustomView().findViewById(R.id.tv_date);
            tvDate.setText(day.get(Calendar.DAY_OF_MONTH) + "");
            tvDayOfWeek.setTextSize(13);
            tvDate.setTextSize(15);
            /**
             * set today date text to accent color
             */
            if (day.getTimeInMillis() == todayMillis) {
                // today tablayout
                tvDate.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorAccent));
            }
            else{
                tvDate.setTextColor(0xeeffffff);
            }
            day.add(Calendar.DAY_OF_MONTH, 1);
        }
        day = Calendar.getInstance();
        TabLayout.Tab todayTab = tabLayout.getTabAt(dayOfWeekConverter(day.get(Calendar.DAY_OF_WEEK)));
        TextView tvDate = (TextView) todayTab.getCustomView().findViewById(R.id.tv_date);
        tvDate.setTextSize(22);
        todayTab.select();
    }

    /**
     *  update the date in tab layout when week has been changed
     * @param tabLayout tablayout in activiy
     */
    private void updaeTablayoutDate(TabLayout tabLayout) {
        Calendar day = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        long todayMillis = today.getTimeInMillis();
        day.add(Calendar.DATE, -dayOfWeekConverter(day.get(Calendar.DAY_OF_WEEK)) + 7 * weekOffset);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            TextView tvDayOfWeek = (TextView) tab.getCustomView().findViewById(R.id.tv_day_of_week);
            tvDayOfWeek.setText(tabTitles[i]);
            TextView tvDate = (TextView) tab.getCustomView().findViewById(R.id.tv_date);
            tvDate.setText(day.get(Calendar.DAY_OF_MONTH) + "");
            tvDayOfWeek.setTextSize(13);
            tvDate.setTextSize(15);
            /**
             * set today date text to accent color
             */
            if (day.getTimeInMillis() == todayMillis) {
                // today tablayout
                tvDate.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorAccent));
            }
            else{
                tvDate.setTextColor(0xeeffffff);
            }
            day.add(Calendar.DAY_OF_MONTH, 1);
        }
        TabLayout.Tab currentTab = tabLayout.getTabAt(tabLayout.getSelectedTabPosition());
        TextView tvDate = (TextView) currentTab.getCustomView().findViewById(R.id.tv_date);
        tvDate.setTextSize(22);
        currentTab.select();
    }


    /**
     * FAB gesturen detector
     */
    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        /**
         *  detect the swipe gesturn in FAB
         */
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            /**
             *  swipe up
             */
            if (e1.getY() - e2.getY() > 100) {
//                Toast.makeText(MainActivity.this, "Up Swipe", Toast.LENGTH_SHORT).show();
                weekOffset -= 1;
                Calendar today = Calendar.getInstance();
                int weekOfNow = today.get(Calendar.WEEK_OF_YEAR);
                Calendar startOfSemester = Calendar.getInstance();
                startOfSemester.set(2015, 8, 7);
                int weekStartOfSemester = startOfSemester.get(Calendar.WEEK_OF_YEAR);
                Log.i("week", "" + weekOfNow + " " + weekStartOfSemester);
                Snackbar.make(MainActivity.nvDrawer, "Week " + (weekOfNow - weekStartOfSemester + 1 + weekOffset), Snackbar.LENGTH_SHORT).show();
                refreshEvents();
                setToolbarTitle(tabLayout.getSelectedTabPosition() + 7 * weekOffset);
                updaeTablayoutDate(tabLayout);
            }
            /**
             * swipe down
             */
            else if (e2.getY() - e1.getY() > 100) {
//                Toast.makeText(MainActivity.this, "Down Swipe", Toast.LENGTH_SHORT).show();
                weekOffset += 1;
                Calendar today= Calendar.getInstance();
                int weekOfNow=today.get(Calendar.WEEK_OF_YEAR);
                Calendar startOfSemester = Calendar.getInstance();
                startOfSemester.set(2015, 8, 7);
                int weekStartOfSemester = startOfSemester.get(Calendar.WEEK_OF_YEAR);
                Log.i("week", "" + weekOfNow + " " + weekStartOfSemester);
                Snackbar.make(MainActivity.nvDrawer, "Week " +(weekOfNow-weekStartOfSemester+1+weekOffset), Snackbar.LENGTH_SHORT).show();
                refreshEvents();
                setToolbarTitle(tabLayout.getSelectedTabPosition() + 7 * weekOffset);
                updaeTablayoutDate(tabLayout);
            }
            return false;
        }
    }

    /**
     * set onNavigationItemSelectedListenner on navigation
     * @param navigationView navifationView used in MainActivity
     */
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }

    /**
     *  set the target activity of each item in Drawer
     * @param item item in Drawer
     */
    public void selectDrawerItem(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_calendar:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_todo:
                intent = new Intent(this, ToDoActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     *  PageAdapter for viewpager
     */
    public class MyPagerAdapter extends FragmentStatePagerAdapter {
        private int NUM_ITEM = 7;
        private String tabTitles[] = new String[]{"Mon", "Tue ", "Wed", "Thu ", "Fri", "Sat", "Sun"};

        /**
         * constructor
         * @param fm
         */
        public MyPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        /**
         * when data changed, return POSITION_NONE to force each fragment in adapter be recreate
         * @param object
         * @return always return POSTION_NONE
         */
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        /**
         * initialize the tab content
         * @param position
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        /**
         * @return number of page in adapter
         */
        @Override
        public int getCount() {
            return NUM_ITEM;
        }

        /**
         * called when new fragment need be created
         * @param position
         * @return
         */
        @Override
        public Fragment getItem(int position) {
            Log.i("Position", position + "");
            return ScreenSlideFragment.newInstance(position, weekOffset);

        }
    }


}
