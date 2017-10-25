package cz.koci.hackathon.service;

import cz.koci.hackathon.model.dto.*;
import cz.koci.hackathon.model.Folder;
import cz.koci.hackathon.model.dto.ListFolderArgument;
import cz.koci.hackathon.model.TemporaryLink;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by daniel on 25.10.17.
 */

public interface ApiInterface {

    @POST("files/get_temporary_link")
    Call<TemporaryLink> getTemporaryLink(@Body FilePath filePath);


    @POST("files/list_folder")
    Call<Folder> listFolder(@Body ListFolderArgument listFolderArgument);

    @POST("files/list_folder/continue")
    Call<Folder> listFolderContinue(@Body CursorArgument cursorArgument);
}
