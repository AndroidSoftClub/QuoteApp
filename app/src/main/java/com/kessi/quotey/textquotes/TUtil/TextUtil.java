package com.kessi.quotey.textquotes.TUtil;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import static android.content.Context.CLIPBOARD_SERVICE;

public class TextUtil {

    public static void shareQuote(Context context, String quote) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, quote);
        context.startActivity(share);
    }

    public static void shareQuoteWApp(Context context, String quote){
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.setPackage("com.whatsapp");
        share.putExtra(Intent.EXTRA_TEXT, quote);
        try {
            context.startActivity(share);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "Install WhatsApp to share Quotes.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void copyQuote(Context context, String quote){
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", quote);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "Quote Copied", Toast.LENGTH_SHORT).show();
    }

}
