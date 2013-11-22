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

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.widget.RemoteViews;

public class KeepWidgetReceiver extends BroadcastReceiver {
	private static int clickCount = 0;
	private static PowerManager.WakeLock sCpuWakeLock;
	private NotificationManager mNotificationManager;
	private String ScrollingText;
	private String NotificationText;
	
	// Notification constant
	public static final int NOTIFICATION_ID = 010110111;

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals("net.geniecode.intent.action.KEEP_SCREEN_ON")){
			updateWidget(context);
		}
	}

	private void updateWidget(Context context) {
		final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		remoteViews.setImageViewResource(R.id.image_button, changeImage());
		
		//REMEMBER TO ALWAYS REFRESH YOUR BUTTON CLICK LISTENERS!!!
		remoteViews.setOnClickPendingIntent(R.id.image_button,
				KeepWidgetProvider.buildButtonPendingIntent(context));

		KeepWidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
		
		// Now acquire or release wake lock on screen
		setWakeLock(context);
	}

	private int changeImage() {
		clickCount++;
		return clickCount % 2 == 0 ? R.drawable.bulb_off : R.drawable.bulb_on;
	}
	
	@SuppressLint("Wakelock")
	@SuppressWarnings("deprecation")
	private void setWakeLock(Context context) {
		if (sCpuWakeLock != null) {
			sCpuWakeLock.release();
			sCpuWakeLock = null;
			
			// Clear notification
			mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.cancel(NOTIFICATION_ID);
		} else {
			PowerManager pm = (PowerManager) context
					.getSystemService(Context.POWER_SERVICE);

			sCpuWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
					| PowerManager.ACQUIRE_CAUSES_WAKEUP
					| PowerManager.ON_AFTER_RELEASE, "Released.");
			sCpuWakeLock.acquire();
			
			setNotification(context);
		}
	}
	
	// Show notification when the WakeLock has been acquired
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void setNotification(Context context) {
		ScrollingText = context.getString(R.string.keep_activated_scroll);
		NotificationText = context.getString(R.string.keep_activated_notify);
		
		// Trigger a notification that, when clicked, will release the wake lock
		mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		PendingIntent contentIntent = KeepWidgetProvider.buildButtonPendingIntent(context);
		
		Notification notification;
		
		if(android.os.Build.VERSION.SDK_INT >= 11) {
			Notification.Builder builder = new Notification.Builder(context);
			builder.setContentIntent(contentIntent)
				.setSmallIcon(R.drawable.ic_launcher)
				.setTicker(ScrollingText)
				.setWhen(System.currentTimeMillis())
				.setAutoCancel(true)
				.setOngoing(true)
				.setContentTitle(ScrollingText)
				.setContentText(NotificationText);
			if(android.os.Build.VERSION.SDK_INT >= 16) {
				notification = builder.build();
			} else {
				notification = builder.getNotification();
			}
		} else {
			notification = new Notification(R.drawable.ic_launcher,
	        		ScrollingText, System.currentTimeMillis());
	        notification.setLatestEventInfo(context, ScrollingText,
	        		NotificationText, contentIntent);
	        notification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_AUTO_CANCEL;
		}
		
		mNotificationManager.notify(NOTIFICATION_ID, notification);
	}
}
