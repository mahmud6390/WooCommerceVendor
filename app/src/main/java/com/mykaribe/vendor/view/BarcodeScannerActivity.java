package com.mykaribe.vendor.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.zxing.Result;
import com.mykaribe.vendor.R;
import com.mykaribe.vendor.controller.RetriveOrderIdController;
import com.mykaribe.vendor.listener.IOrderListUiCallback;
import com.mykaribe.vendor.model.Order;
import com.mykaribe.vendor.utils.Logger;

import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by USER on 17/12/2017.
 */
public class BarcodeScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private ZXingScannerView mScannerView;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QrScanner();
    }
    public void QrScanner(){


        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);

        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();         // Start camera

    }
    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
        if(dialog!=null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    @Override
    public void handleResult(final Result rawResult) {
        dialog=ProgressDialog.show(this,"Fetching information","Order status updating");
        dialog.setCancelable(true);
        dialog.show();
        RetriveOrderIdController controller=new RetriveOrderIdController();
        controller.getOrderId(rawResult.getText(), new IOrderListUiCallback() {
            @Override
            public void onOrderListUpdate(List<Order> orders) {

            }

            @Override
            public void onOrderFailed() {
               String title = getString(R.string.scan_unsuccess_title);
              String  message = getString(R.string.scan_unsuccess_msg);
                AlertDialog.Builder builder = new AlertDialog.Builder(BarcodeScannerActivity.this);
                builder.setTitle(Html.fromHtml(title));
                builder.setMessage(Html.fromHtml(message));
                AlertDialog alert1 = builder.create();
                alert1.setCancelable(true);
                alert1.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
                alert1.show();
            }

            @Override
            public void onOrderUpdate(Order order) {
                dialog.dismiss();
                String title,message;
                if(order!=null && order.getId()!=-1) {
                    title = getString(R.string.scan_success_title);
                    message = getString(R.string.scan_success_msg);
                }else {
                    title = getString(R.string.scan_unsuccess_title);
                    message = getString(R.string.scan_unsuccess_msg);
                }

                    AlertDialog.Builder builder = new AlertDialog.Builder(BarcodeScannerActivity.this);
                    builder.setTitle(Html.fromHtml(title));
                    builder.setMessage(Html.fromHtml(message));
                    AlertDialog alert1 = builder.create();
                    alert1.setCancelable(true);
                    alert1.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    alert1.show();
            }
        });
//       Logger.debugLog("Barcodehandler", rawResult.getText()); // Prints scan results
//        Logger.debugLog("Barcodehandler", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode)

        // show the scanner result into dialog box.

    }
}
