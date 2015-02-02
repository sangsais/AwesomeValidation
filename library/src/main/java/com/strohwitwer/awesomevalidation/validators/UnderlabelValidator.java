package com.strohwitwer.awesomevalidation.validators;

import android.content.Context;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strohwitwer.awesomevalidation.ValidationHolder;
import com.strohwitwer.awesomevalidation.utility.ValidationCallback;
import com.strohwitwer.awesomevalidation.utility.ViewsInfo;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class UnderlabelValidator extends Validator {

    private Context mContext;
    private ArrayList<ViewsInfo> mViewsInfos = new ArrayList<>();

    public void setContext(Context context) {
        mContext = context;
    }

    @Override
    public boolean trigger() {
        halt();
        return checkFields(new ValidationCallback() {
            @Override
            public void execute(ValidationHolder validationHolder, Matcher matcher) {
                EditText editText = validationHolder.getEditText();
                ViewGroup parent = (ViewGroup) editText.getParent();
                int index = parent.indexOfChild(editText);
                LinearLayout newContainer = new LinearLayout(mContext);
                newContainer.setLayoutParams(editText.getLayoutParams());
                newContainer.setOrientation(LinearLayout.VERTICAL);
                TextView textView = new TextView(mContext);
                textView.setText(validationHolder.getErrMsg());
                textView.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_light));
                textView.startAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in));
                parent.removeView(editText);
                newContainer.addView(editText);
                newContainer.addView(textView);
                parent.addView(newContainer, index);
                mViewsInfos.add(new ViewsInfo(index, parent, newContainer, editText));
            }
        });
    }

    @Override
    public void halt() {
        for (ViewsInfo viewsInfo : mViewsInfos) {
            viewsInfo.restoreViews();
        }
        mViewsInfos.clear();
    }

}