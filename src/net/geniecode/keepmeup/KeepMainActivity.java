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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class KeepMainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showAddWidgetDialog();
	}
	
	private void showAddWidgetDialog() {
		AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(this)
		.setTitle(getString(R.string.app_name));
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			mDialogBuilder.setMessage(getString(R.string.keep_message_add_widget_11up));
		} else {
			mDialogBuilder.setMessage(getString(R.string.keep_message_add_widget_pre11));
		}
		mDialogBuilder.setCancelable(false)
		.setNeutralButton(android.R.string.ok,
		new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface d, int w) {
				finish();
			}
		}).show();
 	}
}