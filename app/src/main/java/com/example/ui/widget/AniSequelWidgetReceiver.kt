package com.example.ui.widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidgetManager
import com.example.data.repository.DemoProfileProvider

/**
 * Glance app widget receiver for the profile-activity widget.
 *
 * This is the system-visible entry point that the appwidget-provider XML references.
 * It handles the widget lifecycle broadcast and seeds each new widget instance with
 * the demo activity count until the updater runs again.
 */
class AniSequelWidgetReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_APPWIDGET_UPDATE -> {
                val widgetId = intent.getIntExtra(
                    androidx.glance.appwidget.GlanceAppWidget.EXTRA_APPWIDGET_ID,
                    GlanceAppWidgetManager.INVALID_APPWIDGET_ID
                )
                if (widgetId != GlanceAppWidgetManager.INVALID_APPWIDGET_ID) {
                    seedWidget(context, widgetId)
                } else {
                    // Update every instance if the system asked without an id.
                    AniSequelWidgetUpdater.updateAll(context)
                }
            }
            android.appwidget.AppWidgetManager.ACTION_APPWIDGET_DISABLED,
            android.appwidget.AppWidgetManager.ACTION_APPWIDGET_DELETED -> {
                // Nothing to clean up; Glance owns the per-instance state lifecycle.
            }
            else -> {}
        }
    }

    private fun seedWidget(context: Context, widgetId: Int) {
        val activityCount = DemoProfileProvider.getDemoActivity().size
        val subtitle = "list activity"
        AniSequelWidgetProvider.updateWidget(
            context = context,
            widgetId = widgetId,
            activityCount = activityCount,
            subtitle = subtitle
        )
    }
}
