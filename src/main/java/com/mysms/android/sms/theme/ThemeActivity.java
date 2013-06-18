package com.mysms.android.sms.theme;

import com.mysms.android.sms.theme.system.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ThemeActivity extends Activity {
	
	private static final String MYSMS_PACKAGE_NAME = "com.mysms.android.sms";
	
	/**
	 * Intent extra to activate a theme from a installed theme package. Requires the theme package name as parameter.
	 */
	private static final String INTENT_EXTRA_THEME_ACTIVATE = "com.mysms.android.sms.intent.extra.THEME_ACTIVATE";
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTheme(android.R.style.Theme_Translucent_NoTitleBar);

		final LinearLayout layout = new LinearLayout(getApplicationContext());
		layout.setVisibility(View.GONE);
		setContentView(layout);

		showDialog(0);
	}
	
	@Override
	protected Dialog onCreateDialog(final int id) {
		
		final View messageView = LayoutInflater.from(this).inflate(R.layout.theme_dialog, null);
		final TextView primaryMessageTextView = (TextView) messageView.findViewById(R.id.primary_message);
		final TextView secondaryMessageTextView = (TextView) messageView.findViewById(R.id.secondary_message);

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(messageView);
		builder.setTitle(R.string.theme_dialog_title);

		/* check if mysms is installed */
		ApplicationInfo applicationInfo = null;
		try {
			applicationInfo = getPackageManager().getApplicationInfo(MYSMS_PACKAGE_NAME, 0);
		} catch (final Exception e) {
		}
		
		if (applicationInfo != null) {
			final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
				public void onClick(final DialogInterface dialog, final int which) {
					try {
						Intent i = getPackageManager().getLaunchIntentForPackage(MYSMS_PACKAGE_NAME);
						i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						i.putExtra(INTENT_EXTRA_THEME_ACTIVATE, getPackageName());
						startActivity(i);
					} catch (Exception e) {}
					finish();
				}
			};
			primaryMessageTextView.setText(R.string.theme_dialog_activate_primary_text);
			secondaryMessageTextView.setText(R.string.theme_dialog_activate_secondary_text);
			builder.setPositiveButton(R.string.theme_dialog_activate_text, listener);
		} else {
			
			primaryMessageTextView.setText(R.string.theme_dialog_install_primary_text);
			secondaryMessageTextView.setText(R.string.theme_dialog_install_secondary_text);

			final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
				public void onClick(final DialogInterface dialog, final int which) {
					try {
						final Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.market_url))).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(i);
					} catch (final Exception e) {
					}
					finish();
				}
			};
			builder.setPositiveButton(R.string.theme_dialog_install_text, listener);
		}

		builder.setNegativeButton(android.R.string.cancel, null);

		final Dialog dialog = builder.create();
		dialog.setOnDismissListener(new Dialog.OnDismissListener() {
			public void onDismiss(final DialogInterface arg0) {
				finish();
			}
		});
		return dialog;
	}
}
