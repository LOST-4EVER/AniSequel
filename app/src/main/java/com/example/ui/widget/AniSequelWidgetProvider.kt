package com.example.ui.widget

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.appwidget.AppWidgetManager
import androidx.appwidget.AppWidgetProvider
import androidx.appwidget.AppWidgetProviderInfo
import com.example.MainActivity
import com.example.R

/**
 * Classic AppWidget provider that renders the profile-activity widget with
 * RemoteViews and opens the app on the activity screen when tapped.
 *
 * This intentionally avoids the unreleased Glance APIs that the earlier draft
 * referenced. The widget is small and glanceable: a title, a count of recent
 * list activity, and a tap target that routes to the profile activity screen.
 */
class AniSequelWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { widgetId ->
            updateWidget(context, appWidgetManager, widgetId)
        }
    }

    override fun onDisabled(context: Context) {
        // Nothing to tear down; the next re-enable will recreate instances.
    }

    companion object {
        private const val ACTION_WIDGET_TAP = "com.example.ui.widget.ACTION_WIDGET_TAP"

        fun updateWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            widgetId: Int
        ) {
            val views = RemoteViews(context.packageName, R.layout.widget_anisequel_preview).apply {
                setTextViewText(R.id.widget_preview_count, demoActivityCount().toString())
                setOnClickPendingIntent(
                    R.id.widget_preview_root,
                    PendingIntent.getActivity(
                        context,
                        widgetId,
                        Intent(context, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            putExtra(MainActivity.EXTRA_WIDGET_OPEN_ACTIVITY, true)
                        },
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                )
            }

            appWidgetManager.updateAppWidget(widgetId, views)
        }

        private fun demoActivityCount(): Int =
            com.example.data.repository.DemoProfileProvider.getDemoActivity().size
    }
}
