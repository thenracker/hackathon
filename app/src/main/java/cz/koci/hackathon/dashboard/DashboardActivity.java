package cz.koci.hackathon.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import cz.koci.hackathon.R;
import cz.koci.hackathon.model.Metadata;
import cz.koci.hackathon.model.datasource.ReceivedLinkDataSource;
import cz.koci.hackathon.model.dto.SharedLinkArgument;
import cz.koci.hackathon.model.table.ReceivedLink;
import cz.koci.hackathon.service.RestClient;
import cz.koci.hackathon.shared.BaseActivity;
import cz.koci.hackathon.shared.LinkMetadataLoadedEvent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by petrw on 25.10.2017.
 */

public class DashboardActivity extends BaseActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, DashboardFragment.newInstance()).commit();
        }

        for (final ReceivedLink receivedLink : ReceivedLinkDataSource.findNewlyReceivedLinks()) {
            SharedLinkArgument arg = new SharedLinkArgument(receivedLink.getLink(), (String) null);
            RestClient.get().getSharedLinkMetadata(arg).enqueue(new Callback<Metadata>() {
                @Override
                public void onResponse(Call<Metadata> call, Response<Metadata> response) {
                    if (response.code() == 200) {
                        Metadata metadata = response.body();
                        metadata.setShared(true);
                        metadata.save();
                        receivedLink.delete();
                        EventBus.getDefault().post(new LinkMetadataLoadedEvent());
                    } else if (response.code() >= 400) {
                        System.out.println("fail 400+");
                        // TODO: 25.10.2017
                    }
                }

                @Override
                public void onFailure(Call<Metadata> call, Throwable t) {
                    System.out.println("dsaiud");
                }
            });
        }
    }
}
