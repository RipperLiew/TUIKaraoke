package com.tencent.liteav.tuikaraoke.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.liteav.tuikaraoke.R;
import com.tencent.liteav.tuikaraoke.ui.utils.Toast;

/**
 * Module:   InputTextMsgDialog
 * <p>
 * Function: 听众、主播的弹幕或普通文本的输入框
 */
public class InputTextMsgDialog extends Dialog {
    private static final String TAG = InputTextMsgDialog.class.getSimpleName();

    private TextView           mTextConfirm;
    private EditText           mEditMessage;
    private RelativeLayout     mRelativeLayout;
    private LinearLayout       mConfirmArea;
    private Context            mContext;
    private InputMethodManager mInputMethodManager;
    private OnTextSendListener mOnTextSendListener;

    public InputTextMsgDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
        setContentView(R.layout.trtckaraoke_dialog_input_text);

        mEditMessage = (EditText) findViewById(R.id.et_input_message);
        mEditMessage.setInputType(InputType.TYPE_CLASS_TEXT);
        //修改下划线颜色
        mEditMessage.getBackground().setColorFilter(context.getResources()
                .getColor(R.color.trtckaraoke_transparent), PorterDuff.Mode.CLEAR);

        mTextConfirm = (TextView) findViewById(R.id.confrim_btn);
        mInputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        mTextConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = mEditMessage.getText().toString().trim();
                if (!TextUtils.isEmpty(msg)) {

                    mOnTextSendListener.onTextSend(msg);
                    mInputMethodManager.showSoftInput(mEditMessage, InputMethodManager.SHOW_FORCED);
                    mInputMethodManager.hideSoftInputFromWindow(mEditMessage.getWindowToken(), 0);
                    mEditMessage.setText("");
                    dismiss();
                } else {
                    Toast.show(R.string.trtckaraoke_warning_not_empty, Toast.LENGTH_LONG);
                }
                mEditMessage.setText(null);
            }
        });

        mEditMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case KeyEvent.KEYCODE_ENDCALL:
                    case KeyEvent.KEYCODE_ENTER:
                        if (mEditMessage.getText().length() > 0) {
                            mInputMethodManager.hideSoftInputFromWindow(mEditMessage.getWindowToken(), 0);
                            dismiss();
                        } else {
                            Toast.show(R.string.trtckaraoke_warning_not_empty, Toast.LENGTH_LONG);
                        }
                        return true;
                    case KeyEvent.KEYCODE_BACK:
                        dismiss();
                        return false;
                    default:
                        return false;
                }
            }
        });

        mConfirmArea = (LinearLayout) findViewById(R.id.confirm_area);
        mConfirmArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = mEditMessage.getText().toString().trim();
                if (!TextUtils.isEmpty(msg)) {
                    mOnTextSendListener.onTextSend(msg);
                    mInputMethodManager.showSoftInput(mEditMessage, InputMethodManager.SHOW_FORCED);
                    mInputMethodManager.hideSoftInputFromWindow(mEditMessage.getWindowToken(), 0);
                    mEditMessage.setText("");
                    dismiss();
                } else {
                    Toast.show(R.string.trtckaraoke_warning_not_empty, Toast.LENGTH_LONG);
                }
                mEditMessage.setText(null);
            }
        });

        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl_outside_view);
        mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() != R.id.rl_inputdlg_view) {
                    dismiss();
                }
            }
        });

        final LinearLayout rldlgview = (LinearLayout) findViewById(R.id.rl_inputdlg_view);
        rldlgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputMethodManager.hideSoftInputFromWindow(mEditMessage.getWindowToken(), 0);
                dismiss();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEditMessage.requestFocus();
    }

    public void setOnTextSendListener(OnTextSendListener onTextSendListener) {
        this.mOnTextSendListener = onTextSendListener;
    }

    public interface OnTextSendListener {
        void onTextSend(String msg);
    }
}
