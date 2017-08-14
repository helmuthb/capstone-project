package at.breitenfellner.roomquestions.ui;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import at.breitenfellner.roomquestions.R;
import at.breitenfellner.roomquestions.model.User;
import at.breitenfellner.roomquestions.state.MainViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Home fragment class
 */

public class HomeFragment extends LifecycleFragment {
    MainViewModel viewModel;
    @BindView(R.id.text_why_login)
    TextView whyLoginText;
    @BindView(R.id.button_login)
    Button buttonLogin;
    @BindView(R.id.text_welcome)
    TextView welcomeText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);
        // get view model
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // listen for authentication changes
        viewModel.liveGetUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                boolean isLoggedIn = (user != null);
                if (isLoggedIn) {
                    whyLoginText.setVisibility(View.GONE);
                    buttonLogin.setVisibility(View.GONE);
                    String welcome = getResources().getString(R.string.welcome_text, user.name);
                    welcomeText.setText(welcome);
                    welcomeText.setVisibility(View.VISIBLE);
                }
                else {
                    whyLoginText.setVisibility(View.VISIBLE);
                    buttonLogin.setVisibility(View.VISIBLE);
                    welcomeText.setText("");
                    welcomeText.setVisibility(View.GONE);
                }
            }
        });
        // activate login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.startLogin(getActivity());
            }
        });
        return rootView;
    }
}
