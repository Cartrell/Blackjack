package com.gameplaycoder.thunderjack;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;

import com.gameplaycoder.thunderjack.databinding.ActivityAboutBinding;

public class AboutActivity extends AppCompatActivity {
  //=========================================================================
  // members
  //=========================================================================
  private ActivityAboutBinding m_binding;

  //=========================================================================
  // protected
  //=========================================================================

  //-------------------------------------------------------------------------
  // onCreate
  //-------------------------------------------------------------------------
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    m_binding = DataBindingUtil.setContentView(this,  R.layout.activity_about);
    setVersionText();
    setDeveloperText();
    setWebsiteText();
    initSendButton();
    initCloseButton();
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // initCloseButton
  //-------------------------------------------------------------------------
  private void initCloseButton() {
    m_binding.btnClose.setOnClickListener(new View.OnClickListener() {

      //-------------------------------------------------------------------------
      // onClick
      //-------------------------------------------------------------------------
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }

  //-------------------------------------------------------------------------
  // initSendButton
  //-------------------------------------------------------------------------
  private void initSendButton() {
    m_binding.btnSend.setOnClickListener(new View.OnClickListener() {

      //-------------------------------------------------------------------------
      // onClick
      //-------------------------------------------------------------------------
      @Override
      public void onClick(View v) {
        openEmailAppForSend();
      }
    });
  }

  //-------------------------------------------------------------------------
  // openEmailAppForSend
  //-------------------------------------------------------------------------
  private void openEmailAppForSend() {
    Intent intent = new Intent(Intent.ACTION_SENDTO);
    intent.setData(Uri.parse("mailto:"));

    String[] addresses = { getResources().getString(R.string.developerEmail) };
    intent.putExtra(Intent.EXTRA_EMAIL, addresses);
    intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.feedbackEmailSubject));

    if (intent.resolveActivity(getPackageManager()) != null) {
      startActivity(intent);
    } else {
      presentNoEmailAppDialog();
    }
  }

  //-------------------------------------------------------------------------
  // presentNoEmailAppDialog
  //-------------------------------------------------------------------------
  private void presentNoEmailAppDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(R.string.noEmailAppDialogHeader);
    builder.setMessage(R.string.noEmailAppDialogMessage);
    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

      //-------------------------------------------------------------------------
      // onClick
      //-------------------------------------------------------------------------
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });

    AlertDialog alertDialog = builder.create();
    alertDialog.show();
  }

  //-------------------------------------------------------------------------
  // setDeveloperText
  //-------------------------------------------------------------------------
  private void setDeveloperText() {
    String text = getResources().getString(R.string.developetName);
    m_binding.txtDeveloperValue.setText(text);
  }

  //-------------------------------------------------------------------------
  // setVersionText
  //-------------------------------------------------------------------------
  private void setVersionText() {
    try {
      PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
      String version = pInfo.versionName;
      m_binding.txtVersionValue.setText(version);
    } catch (PackageManager.NameNotFoundException exception) {
      m_binding.txtVersionLabel.setVisibility(View.GONE);
      m_binding.txtVersionValue.setVisibility(View.GONE);
    }
  }

  //-------------------------------------------------------------------------
  // setWebsiteText
  //-------------------------------------------------------------------------
  private void setWebsiteText() {
    String formattedText = getString(R.string.developetWebSite);
    Spanned result = Html.fromHtml(formattedText);
    m_binding.txtWebsiteValue.setText(result);

    m_binding.txtWebsiteValue.setOnClickListener(new View.OnClickListener() {
      //-------------------------------------------------------------------------
      // onClick
      //-------------------------------------------------------------------------
      @Override
      public void onClick(View v) {
        String url = getString(R.string.developetWebSiteUrl);
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
          startActivity(intent);
        }
      }
    });
  }
}
