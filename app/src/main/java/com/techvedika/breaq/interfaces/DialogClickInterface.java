package com.techvedika.breaq.interfaces;

import android.content.DialogInterface;

/**
 * Created by ibrar on 26/12/17.
 */

public interface DialogClickInterface {
    void onClickPositiveButton(DialogInterface pDialog, int resultCode);
    void onClickNegativeButton(DialogInterface pDialog, int resultCode);
}
