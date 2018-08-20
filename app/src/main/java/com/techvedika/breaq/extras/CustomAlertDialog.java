package com.techvedika.breaq.extras;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.techvedika.breaq.R;
import com.techvedika.breaq.interfaces.DialogClickInterface;

/**
 * Created by ibrar on 26/12/17.
 */

public class CustomAlertDialog {

    private static CustomAlertDialog myAlertClass;
    private Dialog mDialog;

    private DialogClickInterface mDialogClickInterface;

    public static CustomAlertDialog getInstance() {

        if (myAlertClass == null)
            myAlertClass = new CustomAlertDialog();

        return myAlertClass;

    }

    /**
     * Show confirmation dialog with two buttons
     *
     * @param mMessage
     * @param mPositiveButton
     * @param mNegativeButton
     * @param mContext
     */
    public void showCustomDialog(Context mContext, String mTitle, String mMessage, String mPositiveButton, String mNegativeButton, final int resultCode) {

        Activity activity = (Activity)mContext;

        this.mDialogClickInterface = (DialogClickInterface) mContext;

        Button btnPositiove, btnNegative = null;

        if(Utilities.has(mDialog) && mDialog.isShowing()) {
            try {

                mDialog.dismiss();
                mDialog = null;
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        mDialog = new Dialog(mContext,R.style.Theme_AppCompat_Light_Dialog_Alert);

        if(Utilities.has(mNegativeButton)) {
            mDialog.setContentView(R.layout.confirm_message);

            btnPositiove = (Button) mDialog.findViewById(R.id.btnPositive);
            btnPositiove.setText(mPositiveButton);

            btnNegative = (Button) mDialog.findViewById(R.id.btnNegative);
            btnNegative.setText(mNegativeButton);

        } else {
            mDialog.setContentView(R.layout.alert_message);

            btnPositiove = (Button) mDialog.findViewById(R.id.btnPositive);
            btnPositiove.setText(mPositiveButton);
        }

        TextView tvTextTitle = (TextView) mDialog.findViewById(R.id.textTitle);

        if(!mTitle.equals("")) {
            tvTextTitle.setText(mTitle);
            tvTextTitle.setVisibility(View.VISIBLE);
        } else {
            tvTextTitle.setVisibility(View.GONE);
        }

        TextView tvMessageText = (TextView) mDialog.findViewById(R.id.textMessage);
        tvMessageText.setText(mMessage);

        mDialog.setCanceledOnTouchOutside(false);

        mDialog.setCancelable(false);

        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        //Grab the window of the dialog, and change the width
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        Window window = dialog.getWindow();
//        lp.copyFrom(window.getAttributes());
//        //This makes the dialog take up the full width
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        window.setAttributes(lp);
        try {
            if(!activity.isFinishing()) {
                mDialog.show();
            }
        } catch (WindowManager.BadTokenException ex){
            ex.printStackTrace();
        }

        btnPositiove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                mDialogClickInterface.onClickPositiveButton(mDialog, resultCode);
            }
        });

        if(Utilities.has(btnNegative)) {
            btnNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Close dialog
                    mDialogClickInterface.onClickNegativeButton(mDialog,resultCode);
                }
            });
        }
    }
}
