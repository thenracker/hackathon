package cz.koci.hackathon.utils;

import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by vlado on 25/10/2017.
 */

public class FileUtils {

    private static final int PICKFILE_REQUEST_CODE = 99;

    public static void pickFile(Fragment activity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        activity.startActivityForResult(intent, PICKFILE_REQUEST_CODE);
    }

}
