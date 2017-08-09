package com.android.leonardotalero.bakingapp.sync;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by leonardotalero on 7/12/17.
 */

public class SyncIntentService extends IntentService {

    public SyncIntentService() {
        super("SyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SyncTask.syncRecipes(this);


    }

}
