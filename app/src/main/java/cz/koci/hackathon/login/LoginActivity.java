package cz.koci.hackathon.login;

import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;

import cz.koci.hackathon.R;
import cz.koci.hackathon.shared.BaseActivity;

public class LoginActivity extends BaseActivity {

    private String APP_KEY = "8x4akmcasi46rwv";
    private String APP_SECRET = "8yoj12oa7kpm8gc";

    private String ACCESS_TOKEN = "vN-eKaVZIsAAAAAAAAAAB1UtnJUpWrPvPZO04F0T20bJLo7Yow6hozCFfduzDBAL";

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, LoginFragment.newInstance()).commit();
        }

        try {
            initDropBox();
        } catch (DbxException | NetworkOnMainThreadException e) {
            //
        }
    }

    private void initDropBox(String... args) throws DbxException {
        DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
        FullAccount account = client.users().getCurrentAccount();
        Log.d("CLIENT", account.getName().getDisplayName());

        getUserFiles(client);
    }

    public void getUserFiles(DbxClientV2 client) throws DbxException {
        ListFolderResult result = client.files().listFolder("");
        while (true) {
            for (Metadata metadata : result.getEntries()) {
                System.out.println(metadata.getPathLower());
            }

            if (!result.getHasMore()) {
                break;
            }

            result = client.files().listFolderContinue(result.getCursor());
        }
    }
}
