package com.journear.app.ui.share;

import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.journear.app.R;
import com.journear.app.core.entities.NearbyDevice;
import com.journear.app.core.services.CommunicationListener;
import com.journear.app.core.services.JnMessage;
import com.journear.app.core.services.JnMessageSet;
import com.journear.app.core.services.ServiceLocator;

import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.lang3.StringUtils;

public class ShareFragment extends Fragment implements CommunicationListener {

    private ShareViewModel shareViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shareViewModel =
                ViewModelProviders.of(this).get(ShareViewModel.class);
        View root = inflater.inflate(R.layout.fragment_share, container, false);
//        final TextView textView = root.findViewById(R.id.text_share);
//        shareViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }


    private void Reject(JnMessage message, NearbyDevice associatedRide) {
        ServiceLocator.getCommunicationHub().sendMessage(associatedRide, JnMessageSet.Reject, this);
    }

    public void Accept(JnMessage message, NearbyDevice nd) {
        ServiceLocator.getCommunicationHub().sendMessage(nd, JnMessageSet.Accept, this);
    }

    @Override
    public void onResponse(final JnMessage message, final NearbyDevice associatedRide) {
        LinearLayout container = getView().findViewById(R.id.messages_container);

        if (StringUtils.containsIgnoreCase(message.getMessageFlag().name(), "request")) {

            View layoutToAdd = getLayoutInflater().inflate(R.layout.accept_reject_button_layout, container, false);
            Button acceptButton = layoutToAdd.findViewById(R.id.message_accept);
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Accept(message, associatedRide);
                }
            });

            Button rejectButton = layoutToAdd.findViewById(R.id.message_reject);

            rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Reject(message, associatedRide);
                }
            });
            container.addView(layoutToAdd);
        } else {
            // fill the tv
            TextView tv = new TextView(getContext());
            container.addView(tv);
        }
    }


    @Override
    public void onExpire(JnMessage expiredMessage, NearbyDevice nearbyDevice) {
        // Add an entry to the linear layout with "Expired: " before the text
    }
}