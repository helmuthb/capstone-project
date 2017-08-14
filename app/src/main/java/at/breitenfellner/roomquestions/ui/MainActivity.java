package at.breitenfellner.roomquestions.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import at.breitenfellner.roomquestions.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Main Activity.
 * The app uses one activity and switches fragments depending on the context.
 */

public class MainActivity extends AppCompatActivity
implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        // TODO: bind correct fragment
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment_container, new HomeFragment());
        ft.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation menu choices
        int id = item.getItemId();
        switch(id) {
            case R.id.nav_home:
                // do home action
                break;
            case R.id.nav_questions:
                // do questions action
                break;
            case R.id.nav_license:
                // do license action
                break;
            case R.id.nav_source:
                // do source action
                break;
            case R.id.nav_gdg:
                // do GDG action
                break;
        }
        // close drawer layout
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
