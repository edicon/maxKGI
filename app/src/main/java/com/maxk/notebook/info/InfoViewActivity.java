package com.maxk.notebook.info;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.maxk.notebook.member.division.MemberDivData;
import com.maxk.notebook.misc.MaxkInfo;
import com.maxk.notebook.misc.MaxkUtils;
import com.maxk.notebook.maxkgi.R;

public class InfoViewActivity extends FragmentActivity {

	private Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.info_view);

		context = this;

		TextView infoTitle = (TextView) findViewById(R.id.infoTitle);
		ImageView infoImage = (ImageView) findViewById(R.id.infoImage);

		infoTitle.setOnClickListener(new mainMenuOnClickListener());
		setTopTitle();
		infoTitle.setVisibility(View.GONE);

		String url;
		if ( MaxkInfo.ASSETS_IMAGE )
			url = MaxkInfo.ASSET_MAKX_UNIV1_IMAGE
					+ "index" + MaxkInfo.infoType
					+ ".html";
		else
			url = MaxkInfo.PUB_ON_WEB_MAKX_UNIV1_HTML 
					+ "index" + MaxkInfo.infoType
					+ ".html";
		ViewWeb( url );

		MaxkUtils.initPopupMenu((Activity) context);
		MaxkUtils.initMenu((Activity) context);
	}

	private class mainMenuOnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
		}
	}

	private Dialog dlgLoading = null;

	private void ViewWeb( String url ) {

		dlgLoading = new Dialog(this);
		dlgLoading.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dlgLoading.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		dlgLoading.setContentView(new ProgressBar(this));

		WebView wv;
		wv = (WebView) findViewById(R.id.webView1);
		wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setSupportZoom(true);
		wv.getSettings().setBuiltInZoomControls(true);
		/**
		 * How to set the initial zoom/width for a webview
		 * http://stackoverflow.com
		 * /questions/3808532/how-to-set-the-initial-zoom-width-for-a-webview
		 **/
		wv.getSettings().setLoadWithOverviewMode(true);
		wv.getSettings().setUseWideViewPort(true);

		wv.setWebViewClient(new NewsWebViewClient());
		wv.setWebChromeClient(new WebChromeClient());

		wv.loadUrl(url);
	}

	class NewsWebViewClient extends WebViewClient {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			dlgLoading.show();
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);

			try {
				dlgLoading.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

		/*
		 * @ERROR
		 * 
		 * @see
		 * http://www.catchingtales.com/android-webview-shouldoverrideurlloading
		 * -and-redirect/416/
		 */
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			Intent intent = null;

			if (false && url.startsWith("http:")) {
				try {
					intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					intent.setDataAndType(Uri.parse(url), "text/html");
					context.startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(context,
							"No Browser, Please install Web browser.",
							Toast.LENGTH_SHORT).show();
				}
				return true;
			}
			return false;
		}
	}

	private void setTopTitle() {
		String title;
		TextView topTitle = (TextView) findViewById(R.id.topTitle);

		switch (MaxkInfo.infoType) {
		default:
		case 1:
			title = getString(R.string.book_info_title);
			break;
		case 2:
			title = getString(R.string.book_info_title);
			break;
		case 3:
			title = getString(R.string.book_info_title);
			break;
		case 4:
			title = getString(R.string.book_info_title);
			break;
		case 5:
			title = getString(R.string.book_info_title);
			break;
		case 6:
			title = getString(R.string.book_info_title);
			break;
		case MaxkInfo.INFO_TYPE_HELP:
			title = getString(R.string.bInfoButton20Txt);
			break;
		case MaxkInfo.INFO_TYPE_USE:
			title = getString(R.string.bInfoButton21Txt);
			break;
		}

		topTitle.setText(title);
	}

	private void setImage() {
		ImageView infoImage = (ImageView) findViewById(R.id.infoImage);
		infoImage.setImageResource(MemberDivData.InfoGroupImage[MaxkInfo.infoType - 1]);
	}
}