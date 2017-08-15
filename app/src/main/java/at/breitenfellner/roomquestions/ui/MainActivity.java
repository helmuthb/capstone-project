package at.breitenfellner.roomquestions.ui;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import at.breitenfellner.roomquestions.R;
import at.breitenfellner.roomquestions.di.GlideApp;
import at.breitenfellner.roomquestions.model.User;
import at.breitenfellner.roomquestions.state.MainViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Main Activity.
 * The app uses one activity and switches fragments depending on the context.
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LifecycleRegistryOwner {
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    DrawerViews dv;
    MainViewModel viewModel;
    LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MainViewModel.REQUESTCODE_AUTH) {
            viewModel.getUser();
            viewModel.isLoggedIn();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

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
        dv = new DrawerViews();
        ButterKnife.bind(dv, navigationView.getHeaderView(0));
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        // get view model
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // Bind fragment is no save data - otherwise Android will do it for me
        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragment_container, new HomeFragment());
            ft.commit();
        }
        // listen for authentication changes
        viewModel.liveGetUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if (user == null) {
                    // we are not logged in!
                    dv.userName.setVisibility(View.GONE);
                    dv.userPicture.setVisibility(View.GONE);
                    dv.logout.setVisibility(View.GONE);
                    dv.whyLoginText.setVisibility(View.VISIBLE);
                    dv.login.setVisibility(View.VISIBLE);
                }
                else {
                    // we are logged in!
                    dv.userName.setVisibility(View.VISIBLE);
                    dv.userPicture.setVisibility(View.VISIBLE);
                    dv.logout.setVisibility(View.VISIBLE);
                    dv.whyLoginText.setVisibility(View.GONE);
                    dv.login.setVisibility(View.GONE);
                    dv.userName.setText(user.name);
                    GlideApp.with(MainActivity.this)
                            .load(user.photoUrl)
                            .circleCrop()
                            .into(dv.userPicture);
                }
            }
        });
        // perform login on button click
        View.OnClickListener loginOperation = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.startLogin(MainActivity.this);
            }
        };
        dv.login.setOnClickListener(loginOperation);
        View.OnClickListener logoutOperation = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.performLogout(MainActivity.this);
            }
        };
        dv.logout.setOnClickListener(logoutOperation);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment oldFragment = fm.findFragmentById(R.id.fragment_container);
        // Handle navigation menu choices
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:
                // do home action
                if (!(oldFragment instanceof HomeFragment)) {
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.fragment_container, new HomeFragment());
                    ft.commit();
                }
                break;
            case R.id.nav_questions:
                // do questions action
                if (!(oldFragment instanceof RoomsFragment)) {
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.fragment_container, new RoomsFragment());
                    ft.commit();
                }
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

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    class DrawerViews {
        // navigation drawer items
        @BindView(R.id.nav_user_name)
        TextView userName;
        @BindView(R.id.nav_user_picture)
        ImageView userPicture;
        @BindView(R.id.nav_logout)
        Button logout;
        @BindView(R.id.nav_why_login)
        TextView whyLoginText;
        @BindView(R.id.nav_login)
        Button login;
    }
}
