package ml.mk.jm.ay.ak.studenttoolkit.calendar;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
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
import android.widget.Toast;

import java.util.Calendar;

import ml.mk.jm.ay.ak.studenttoolkit.R;
import ml.mk.jm.ay.ak.studenttoolkit.calendar.helper.TimeFormatHelper;
import ml.mk.jm.ay.ak.studenttoolkit.todo.ToDoActivity;

import static ml.mk.jm.ay.ak.studenttoolkit.calendar.helper.TimeFormatHelper.dayOfWeekConverter;

public class MainActivity extends AppCompatActivity {

    private MyPagerAdapter pagerAdapter;
    private TabLayout tabLayout;
    private NavigationView nvDrawer;
    private TextView tb_year_tv;
    private TextView tb_month_tv;
    private String tabTitles[] = new String[]{"Mon", "Tue ", "Wed", "Thu ", "Fri", "Sat", "Sun"};
    private ConditionalViewPager viewPager;

    public int getWeekOffset() {
        return weekOffset;
    }

    private int weekOffset = 0;

    public boolean isEditMode() {
        return editMode;
    }

    private boolean editMode = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tb_year_tv = (TextView) findViewById(R.id.tb_year);
        tb_month_tv = (TextView) findViewById(R.id.tb__month);
        setToolbarTitle(dayOfWeekConverter(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)));//initiallize toolbaltitle

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onLongClick(View v) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                editMode = !editMode;
                FloatingActionButton fab = (FloatingActionButton) v;
                if (editMode) {
                    fab.setImageResource(R.drawable.editmodeicon);
                } else {
                    fab.setImageResource(R.drawable.viewmodeicon);
                }
                viewPager.setEditmode(editMode);
//                Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                vb.vibrate(200);
                v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                return false;
            }
        });
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
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setSelectedTabIndicatorHeight(10);
        tabLayout.setupWithViewPager(viewPager);
        setTabLayout(tabLayout);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                Calendar day= Calendar.getInstance();
//                day.add(Calendar.DATE,Calendar.MONDAY-day.get(Calendar.DAY_OF_WEEK)+tab.getPosition());
//                View view = tab.getCustomView();
//                ViewGroup viewGroup = (ViewGroup) view.getParent();
//                viewGroup.removeAllViews();
//                tab.setCustomView(R.layout.select_tab_tablayout);
//                TextView textView = (TextView) tab.getCustomView().findViewById(R.id.tv_select_tab);
//                textView.setText(day.get(Calendar.DAY_OF_MONTH)+"");

                TextView tvDate = (TextView) tab.getCustomView().findViewById(R.id.tv_date);
                tvDate.setTextSize(22);
                viewPager.setCurrentItem(tab.getPosition());
//                if (tab.getPosition() == (day.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY)) {
//                    TextView textView = (TextView) tab.getCustomView().findViewById(R.id.tv_date);
//                    textView.setText("specter");
//                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                Calendar day= Calendar.getInstance();
//                day.add(Calendar.DATE,Calendar.MONDAY-day.get(Calendar.DAY_OF_WEEK)+tab.getPosition());
//                View view = tab.getCustomView();
//                ViewGroup viewGroup = (ViewGroup) view.getParent();
//                viewGroup.removeAllViews();
//                tab.setCustomView(R.layout.tab_of_tablayout);
//                TextView tvDayOfWeek = (TextView) tab.getCustomView().findViewById(R.id.tv_day_of_week);
//                tvDayOfWeek.setText(tabTitles[tab.getPosition()]);
//                TextView tvDate = (TextView) tab.getCustomView().findViewById(R.id.tv_date);
//                tvDate.setText(day.get(Calendar.DAY_OF_MONTH)+"");
                TextView tvDate = (TextView) tab.getCustomView().findViewById(R.id.tv_date);
                tvDate.setTextSize(15);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

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

    }

    private void setToolbarTitle(int position) {
        tb_year_tv.setText(TimeFormatHelper.getTimeField(Calendar.YEAR, position));
        tb_month_tv.setText(TimeFormatHelper.getTimeField(Calendar.MONTH, position));
    }

    private void setTabLayout(TabLayout tabLayout) {
        Calendar day = Calendar.getInstance();
        day.add(Calendar.DATE, -dayOfWeekConverter(day.get(Calendar.DAY_OF_WEEK))+7*weekOffset);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(R.layout.tab_of_tablayout);
            TextView tvDayOfWeek = (TextView) tab.getCustomView().findViewById(R.id.tv_day_of_week);
            tvDayOfWeek.setText(tabTitles[i]);
            TextView tvDate = (TextView) tab.getCustomView().findViewById(R.id.tv_date);
            tvDate.setText(day.get(Calendar.DAY_OF_MONTH) + "");
            tvDayOfWeek.setTextSize(13);
            tvDate.setTextSize(15);
            day.add(Calendar.DAY_OF_MONTH, 1);
        }
        day = Calendar.getInstance();
        TabLayout.Tab todayTab = tabLayout.getTabAt(dayOfWeekConverter(day.get(Calendar.DAY_OF_WEEK)));
        TextView tvDate = (TextView) todayTab.getCustomView().findViewById(R.id.tv_date);
        tvDate.setTextSize(22);
        todayTab.select();
    }
    private void updaeTablayoutDate(TabLayout tabLayout) {
        Calendar day = Calendar.getInstance();
        day.add(Calendar.DATE, -dayOfWeekConverter(day.get(Calendar.DAY_OF_WEEK))+7*weekOffset);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            TextView tvDayOfWeek = (TextView) tab.getCustomView().findViewById(R.id.tv_day_of_week);
            tvDayOfWeek.setText(tabTitles[i]);
            TextView tvDate = (TextView) tab.getCustomView().findViewById(R.id.tv_date);
            tvDate.setText(day.get(Calendar.DAY_OF_MONTH) + "");
            tvDayOfWeek.setTextSize(13);
            tvDate.setTextSize(19);
            day.add(Calendar.DAY_OF_MONTH, 1);
        }
        TabLayout.Tab currentTab = tabLayout.getTabAt(tabLayout.getSelectedTabPosition());
        TextView tvDate = (TextView) currentTab.getCustomView().findViewById(R.id.tv_date);
        tvDate.setTextSize(27);
        currentTab.select();
    }


    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getY() - e2.getY() > 100) {
                Toast.makeText(MainActivity.this, "Up Swipe", Toast.LENGTH_SHORT).show();
                weekOffset -= 1;
                pagerAdapter.notifyDataSetChanged();
                setToolbarTitle(tabLayout.getSelectedTabPosition() + 7 * weekOffset);
                updaeTablayoutDate(tabLayout);

            } else if (e2.getY() - e1.getY() > 100) {
                Toast.makeText(MainActivity.this, "Down Swipe", Toast.LENGTH_SHORT).show();
                weekOffset += 1;
                pagerAdapter.notifyDataSetChanged();
                setToolbarTitle(tabLayout.getSelectedTabPosition() + 7 * weekOffset);
                updaeTablayoutDate(tabLayout);
            }
            return false;
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }

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

    public class MyPagerAdapter extends FragmentStatePagerAdapter{
        private int NUM_ITEM = 7;
        private String tabTitles[] = new String[]{"Mon", "Tue ", "Wed", "Thu ", "Fri", "Sat", "Sun"};

        public MyPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return NUM_ITEM;
        }

        @Override
        public Fragment getItem(int position) {
            Log.i("Position", position + "");
            return ScreenSlideFragment.newInstance(position,weekOffset);

        }
    }


}
