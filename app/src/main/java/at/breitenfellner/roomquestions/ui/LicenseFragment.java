package at.breitenfellner.roomquestions.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import at.breitenfellner.roomquestions.R;

/**
 * Simple fragment showing the licenses of libraries used in the app
 */

public class LicenseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_licenses, container, false);
        return rootView;
    }
}
