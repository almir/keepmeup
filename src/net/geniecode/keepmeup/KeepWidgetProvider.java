/*
 * Copyright (C) 2013 GenieCode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.geniecode.keepmeup;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class KeepWidgetProvider extends AppWidgetProvider {
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// Get the layout for the App Widget and attach an on-click listener
        // to the button
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        remoteViews.setOnClickPendingIntent(R.id.layout, buildButtonPendingIntent(context));
        
        pushWidgetUpdate(context, remoteViews);
	}
	
	public static PendingIntent buildButtonPendingIntent(Context context) {
		Intent intent = new Intent();
		intent.setAction("net.geniecode.intent.action.KEEP_SCREEN_ON");
		return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
		ComponentName mWidget = new ComponentName(context, KeepWidgetProvider.class);
	    AppWidgetManager mAppWidgetManager = AppWidgetManager.getInstance(context);
	    mAppWidgetManager.updateAppWidget(mWidget, remoteViews);
	}
}