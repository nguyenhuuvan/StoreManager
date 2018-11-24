package poly.vannhph06247.storemanager;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ajts.androidmads.library.SQLiteToExcel;

import java.io.File;
import java.util.Objects;

public abstract class Library extends AppCompatActivity {
    protected final String format_phone = "0\\d{9}";
    protected final String format_email = "\\w+@\\w+(\\.\\w+){1,2}";

    protected boolean checkPrimaryKey(String name) {
        return name.split("\\w+").length > 1;
    }

    protected void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(
                Objects.requireNonNull(activity.getCurrentFocus()).getWindowToken(), 0);
    }

    protected boolean checkString(String name) {
        try {
            String[] strArray = name.split(" ");
            StringBuilder builder = new StringBuilder();
            for (String s : strArray) {
                String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
                builder.append(cap).append(" ");
            }
            Log.e("Full name", builder.toString().trim());
            String lastName;
            if (name.split("\\w+").length > 1) {
                lastName = name.substring(name.lastIndexOf(" ") + 1);
            } else {
                lastName = name;
            }
            Log.e("Last Name", lastName);
        } catch (Exception e) {
            return true;
        }
        return false;
    }
    protected String getTextRadioGroup(RadioGroup radioGroup, RadioButton radioMale,RadioButton radioFeMale) {
        int selected = radioGroup.getCheckedRadioButtonId();
        if (selected == radioMale.getId())
            return "Nam";
        if (selected == radioFeMale.getId())
            return "Nữ";
        return "Nam";
    }
    protected String changeString(String string) {
        String[] strArray = string.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : strArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            builder.append(cap).append(" ");
        }
        return builder.toString().trim();
    }

    protected int checkAge(String number) {
        try {
            int result = Integer.parseInt(number);
            if (result >= 150)
                return 1;
            if (result <= 0)
                return 0;
        } catch (Exception e) {
            return 1;
        }
        return -1;
    }

    protected void runLayoutAnimationLeft(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_left);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    protected void runLayoutAnimationRight(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_right);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    protected boolean checkInteger(String number) {
        try {
            int result = Integer.parseInt(number);
            Log.e("result", String.valueOf(result));
            return false;
        } catch (Exception e) {
            return true;
        }

    }
    protected void sqlToExel(){

    }
}
