package kz.programmer.loolzrules.infiniball;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Random;

import static android.appwidget.AppWidgetManager.ACTION_APPWIDGET_UPDATE;
import static kz.programmer.loolzrules.infiniball.BallWidgetConfigureActivity.PREFS_NAME;
import static kz.programmer.loolzrules.infiniball.BallWidgetConfigureActivity.PREF_DURATION_KEY;


public class BallWidget extends AppWidgetProvider {
    
    // TODO: widgets get same duration

    public static final String IS_CLICK = "is_click";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ball_widget);
        views.setOnClickPendingIntent(R.id.whole, setupPendingIntent(context, appWidgetId));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static PendingIntent setupPendingIntent(Context context, int appWidgetId) {
        Intent intent = new Intent();
        intent.setAction(ACTION_APPWIDGET_UPDATE);
        intent.putExtra(IS_CLICK, true);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        Log.i("LOL_1", String.valueOf(appWidgetId));
        Log.i("LOL_2", String.valueOf(AppWidgetManager.EXTRA_APPWIDGET_ID));

        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onUpdate(final Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            BallWidgetConfigureActivity.deleteDurationPref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent.getAction().equals(ACTION_APPWIDGET_UPDATE) && intent.hasExtra(IS_CLICK)
                    && intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID)) {

                int index_answer_type = (new Random()).nextInt(5);
                int array_id;

                switch (index_answer_type) {
                    case 4:
                        array_id = R.array.answers_neg;
                        break;
                    case 3:
                        array_id = R.array.answers_pos;
                        break;
                    case 2:
                        array_id = R.array.answers_semineg;
                        break;
                    case 1:
                        array_id = R.array.answers_semipos;
                        break;
                    default:
                        array_id = R.array.answers_undef;
                        break;
                }

                int index_answer = (new Random()).nextInt(5);
                String response = context.getResources().getStringArray(array_id)[index_answer];

                int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);

                Boolean is_long = context.getSharedPreferences(PREFS_NAME, 0)
                        .getBoolean(PREF_DURATION_KEY + widgetId, false);

                Log.i("LOL", String.valueOf(PREF_DURATION_KEY + widgetId));

                int duration = is_long ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;

                Toast.makeText(context, response, duration).show();
            }
        } catch (NullPointerException e) {
            Toast.makeText(context, "An error occurred", Toast.LENGTH_LONG).show();
        }
    }
}

