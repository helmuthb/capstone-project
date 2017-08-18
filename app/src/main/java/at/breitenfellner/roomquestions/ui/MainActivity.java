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
import android.widget.LinearLayout;
import android.widget.TextView;

import at.breitenfellner.roomquestions.R;
import at.breitenfellner.roomquestions.di.GlideApp;
import at.breitenfellner.roomquestions.model.Room;
import at.breitenfellner.roomquestions.model.User;
import at.breitenfellner.roomquestions.state.MainViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Main Activity.
 * The app uses one activity and switches fragments depending on the context.
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LifecycleRegistryOwner,
        RoomsAdapter.RoomSelectionListener, RoomsFragment.OpenQuestionOperation {
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @Nullable
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Nullable
    @BindView(R.id.tablet_top_layout)
    LinearLayout tabletLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
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
        dv = new DrawerViews();
        ButterKnife.bind(dv, navigationView.getHeaderView(0));
        // on phone we have drawer, on tablet we don't
        if (drawerLayout != null) {
            toggle = new ActionBarDrawerToggle(
                    this,
                    drawerLayout,
                    toolbar,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
        }
        navigationView.setNavigationItemSelectedListener(this);
        // get view model
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // Bind fragment if no save data - otherwise Android will do it for me
        FragmentManager fm = getSupportFragmentManager();
        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_home);
            doNavigationAction(R.id.nav_home);
        }
        // get existing fragment: shall we bind listeners?
        Fragment current = fm.findFragmentById(R.id.fragment_container);
        if (current instanceof HomeFragment) {
            ((HomeFragment) current).setRoomSelectionListener(this);
        } else if (current instanceof RoomsFragment) {
            ((RoomsFragment) current).setQuestionOperations(this);
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
                } else {
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

    void doNavigationAction(int itemId) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment oldFragment = fm.findFragmentById(R.id.fragment_container);
        switch (itemId) {
            case R.id.nav_home:
                // do home action
                if (!(oldFragment instanceof HomeFragment)) {
                    FragmentTransaction ft = fm.beginTransaction();
                    HomeFragment homeFragment = new HomeFragment();
                    homeFragment.setRoomSelectionListener(this);
                    ft.replace(R.id.fragment_container, homeFragment);
                    ft.commit();
                }
                break;
            case R.id.nav_questions:
                // do questions action
                if (!(oldFragment instanceof RoomsFragment)) {
                    FragmentTransaction ft = fm.beginTransaction();
                    RoomsFragment roomsFragment = new RoomsFragment();
                    roomsFragment.setQuestionOperations(this);
                    ft.replace(R.id.fragment_container, roomsFragment);
                    ft.commit();
                }
                break;
            case R.id.nav_license:
                // do license action
                if (!(oldFragment instanceof LicenseFragment)) {
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.fragment_container, new LicenseFragment());
                    ft.commit();
                }
                break;
            case R.id.nav_source:
                // do source action
                break;
            case R.id.nav_gdg:
                // do GDG action
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation menu choices
        int id = item.getItemId();
        doNavigationAction(id);
        // close drawer layout (if we have one)
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    public void onRoomSelected(Room room, int position) {
        viewModel.setCurrentRoomPosition(position);
        navigationView.setCheckedItem(R.id.nav_questions);
        doNavigationAction(R.id.nav_questions);
    }

    @Override
    public void openAskQuestion(Room room) {
        // start activity
        Intent questionIntent = new Intent(this, NewQuestionActivity.class);
        Bundle args = new Bundle();
        args.putString(NewQuestionFragment.ARG_ROOMKEY, room.key);
        questionIntent.putExtras(args);
        startActivity(questionIntent);
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
