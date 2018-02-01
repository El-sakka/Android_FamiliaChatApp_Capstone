package com.sakkay.familiachatapp.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.RemoteViews;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sakkay.familiachatapp.MainActivity;
import com.sakkay.familiachatapp.Model.Message;
import com.sakkay.familiachatapp.R;

import java.io.IOException;
import java.io.InputStream;

/**
 * Implementation of App Widget functionality.
 */
public class ChatWidget extends AppWidgetProvider {


    RemoteViews remoteViews;
    private final int REQUEST_CODE = 0;
    private static final String ACTION_BROADCASTWIIDGET = "ACTION_BROADCASTWIIDGET";


    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        remoteViews = new RemoteViews(context.getPackageName(), R.layout.chat_widget);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,REQUEST_CODE,intent,0);

        new Download(remoteViews).execute("Familia chat");

        remoteViews.setOnClickPendingIntent(R.id.widget_title,pendingIntent);
        Intent secondIntent = new Intent(context,ChatWidget.class);
        secondIntent.setAction(ACTION_BROADCASTWIIDGET);

        context.sendBroadcast(secondIntent);
        appWidgetManager.updateAppWidget(appWidgetId,remoteViews);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        super.onEnabled(context);

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        super.onDisabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (ACTION_BROADCASTWIIDGET.equals(intent.getAction())) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.chat_widget);
            views.setTextViewText(R.id.information, getInformation());
            ComponentName componentName = new ComponentName(context, ChatWidget.class);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetManager.updateAppWidget(componentName, views);
        }
    }

    private String getInformation(){
        DatabaseReference databaseReference =
                FirebaseDatabase.getInstance().getReference().child("users");
        databaseReference.orderByChild("email").equalTo("mahmoudelsakka95@gmail.com")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            Message message = dataSnapshot.getChildren().iterator().next().getValue(Message.class);
                            remoteViews.setTextViewText(R.id.admin,message.getText()+'\n'+
                                    message.getName());
                        }catch (Exception e){

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        return "";
    }


    public class Download extends AsyncTask<String,Void,Bitmap> {

        private RemoteViews views;
        private String url = "http://findicons.com/files/icons/2101/ciceronian/59/photos.png";

        public Download(RemoteViews views){
            this.views = views;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {

            try {
                InputStream inputStream = new java.net.URL(url).openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}

