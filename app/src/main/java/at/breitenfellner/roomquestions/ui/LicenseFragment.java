package at.breitenfellner.roomquestions.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import at.breitenfellner.roomquestions.R;

/**
 * Simple fragment showing the licenses of libraries used in the app
 */

public class LicenseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_licenses, container, false);
        Button closeButton = rootView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // close dialog
                getActivity().finish();
            }
        });
        return rootView;
    }
}
