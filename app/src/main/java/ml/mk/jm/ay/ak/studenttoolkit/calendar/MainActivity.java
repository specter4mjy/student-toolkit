package ml.mk.jm.ay.ak.studenttoolkit.calendar;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import ml.mk.jm.ay.ak.studenttoolkit.R;
import ml.mk.jm.ay.ak.studenttoolkit.todo.ToDoActivity;

public class MainActivity extends AppCompatActivity {

    private FragmentPagerAdapter pagerAdapter;
    private TabLayout tabLayout;
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private TextView tb_day_tv;
    private TextView tb_year_tv;
    private TextView tb_month_tv;

    public boolean isEditMode() {
        return editMode;
    }

    private boolean editMode = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tb_day_tv = (TextView) findViewById(R.id.tb__day);
        tb_year_tv = (TextView) findViewById(R.id.tb_year);
        tb_month_tv = (TextView) findViewById(R.id.tb__month);
        setToolbarTitle(0);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final ConditionalViewPager viewPager = (ConditionalViewPager) findViewById(R.id.pager);

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onLongClick(View v) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                editMode = !editMode;
                FloatingActionButton fab = (FloatingActionButton) v;
                if (editMode) {
                    fab.setImageDrawable(getDrawable(R.drawable.editmodeicon));
                } else {
                    fab.setImageDrawable(getDrawable(R.drawable.viewmodeicon));
                }
                viewPager.setEditmode(editMode);
//                Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                vb.vibrate(5000);
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

        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        tb_year_tv.setText(getYear(position));
        tb_month_tv.setText(getMonth(position));
        tb_day_tv.setText(getDay(position));
    }



    private String getDay(int position) {
        Calendar today = Calendar.getInstance();
        return today.get(Calendar.DAY_OF_MONTH) + position + "";
    }
    private String getMonth(int position) {
        Calendar today = Calendar.getInstance();
        DateFormat monthFormatter = new SimpleDateFormat("MMMM");
        return monthFormatter.format(today.getTime());
    }
    private String getYear(int position) {
        Calendar today = Calendar.getInstance();
        return today.get(Calendar.YEAR) + "";
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getY() - e2.getY() > 100) {
                Toast.makeText(MainActivity.this, "Up Swipe", Toast.LENGTH_SHORT).show();
            }
            else if (e2.getY() - e1.getY() > 100) {
                Toast.makeText(MainActivity.this, "Down Swipe", Toast.LENGTH_SHORT).show();
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

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEM = 7;
        private String tabTitles[] = new String[]{"Mon", "Tue ", "Wed", "Thu ", "Fri", "Sat", "Sun"};

        public MyPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
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
            return ScreenSlideFragment.newInstance(position);
        }
    }


}
