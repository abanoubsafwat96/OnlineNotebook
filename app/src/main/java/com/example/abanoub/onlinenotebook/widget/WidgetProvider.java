package com.example.abanoub.onlinenotebook.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.example.abanoub.onlinenotebook.DetailedActivity;
import com.example.abanoub.onlinenotebook.SignInActivity;
import com.example.abanoub.onlinenotebook.R;

/**
 * Created by Abanoub on 2017-06-28.
 */

public class WidgetProvider extends AppWidgetProvider {

    public static final String EXTRA_ITEM = "com.example.abanoub.onlinenotebook.widget.WidgetProvider.EXTRA_ITEM";

    /**
     * this method is called every 30 mins as specified on widgetinfo.xml
     * this method is also called on every phone reboot
     **/

    @Override
    public void onUpdate(Context context, AppWidgetManager
            appWidgetManager, int[] appWidgetIds) {

    /* int[] appWidgetIds holds ids of multiple instance of your widget
     * meaning you are placing more than one widgets on your homescreen
     **/
        for (int i = 0; i < appWidgetIds.length; ++i) {

            //which layout to show on widget
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.note_widget);

            //RemoteViews Service needed to provide adapter for ListView
            Intent svcIntent = new Intent(context, WidgetService.class);
            //passing app widget id to that RemoteViews Service
            svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            //setting a unique Uri to the intent
            svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
            //setting adapter to listview of the widget
            remoteViews.setRemoteAdapter(appWidgetIds[i], R.id.listView, svcIntent);
            //setting an empty view in case of no data
            remoteViews.setEmptyView(R.id.listView, R.id.empty_view);

            // This section makes it possible for items to have individualized behavior
            Intent clickIntent = new Intent(context, DetailedActivity.class);
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            PendingIntent clickPI = PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.listView, clickPI);

            Intent clickIntent2 = new Intent(context, SignInActivity.class);
            clickIntent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            PendingIntent clickPI2 = PendingIntent.getActivity(context, 0, clickIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.linearTitle, clickPI2);

            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }
}