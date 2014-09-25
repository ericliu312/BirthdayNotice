package com.liubo.birthdaynotice;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

/**
 * <p>Title: CustomDialog</p>
 * <p>Description:自定义Dialog（参数传入Dialog样式文件，Dialog布局文件） </p>
 * <p>Copyright: Copyright (c) 2013</p>
 * @author archie
 * @version 1.0
 */
public class Dialog1 extends Dialog {
        int layoutRes;//布局文件
        Context context;
        private Button button;
        private EditText name;
        private EditText dayet;
        public Dialog1(Context context) {
            super(context);
            this.context = context;
        }
        /**
         * 自定义布局的构造方法
         * @param context
         * @param resLayout
         */
        public Dialog1(Context context,int resLayout){
            super(context);
            this.context = context;
            this.layoutRes=resLayout;
        }
        /**
         * 自定义主题及布局的构造方法
         * @param context
         * @param theme
         * @param resLayout
         */
        public Dialog1(Context context, int theme,int resLayout){
            super(context, theme);
            this.context = context;
            this.layoutRes=resLayout;
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.setContentView(layoutRes);
            init();
        }
        
        private void init()
        {
            getWindow().setBackgroundDrawableResource(R.drawable.transparent);
            button =  (Button)findViewById(R.id.btn1);
            name = (EditText)findViewById(R.id.name);
            dayet = (EditText)findViewById(R.id.day);
            dayet.setInputType(InputType.TYPE_NULL);
            dayet.setFocusable(false);
            dayet.setOnClickListener(new android.view.View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    showDataPicker(dayet);
                }
            });
        }
        
        private void showDataPicker(final EditText edit)
        {
            int year = 0, month = 0, day = 0;
            if (edit != null)
            {
                String editText = edit.getText().toString();
                if (editText.length() == 8)
                {
                    try
                    {
                        year = Integer.parseInt(editText.substring(0, 4));
                        month = Integer.parseInt(editText.substring(4, 6)) - 1;
                        day = Integer.parseInt(editText.substring(6));
                    }
                    catch (Exception e)
                    {
                        year = 0;
                    }
                }
            }
            if (year == 0)
            {
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
            }

            new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener()
            {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                {
                    String text = String.valueOf(year);
                    monthOfYear++;
                    if (monthOfYear < 10)
                        text += "0";
                    text += monthOfYear;
                    if (dayOfMonth < 10)
                        text += "0";
                    text += dayOfMonth;

                    edit.setText(text);
                }
            }, year, month, day).show();
        }
        
        public void setPositiveClickListener(android.view.View.OnClickListener listener)
        {
            button.setOnClickListener(listener);
        }
        
        public void setPositiveClickListener()
        {
            button.setOnClickListener(new android.view.View.OnClickListener()
            {
                
                @Override
                public void onClick(View v)
                {
                    if(Checkable())
                    {
                        dismiss();
                        String value =name.getText().toString()+":"+dayet.getText().toString();
                        String defaultStr = ShPrefUtils.getInstance(context).get(UserValue.infoKey, "");
                        if(!Tool.isTrimEmpty(defaultStr))
                        {
                            value = value+";"+defaultStr;
                        }
                        ShPrefUtils.getInstance(context).put(UserValue.infoKey, value);
                        if(null != passinterface)
                        {
                            passinterface.pass();
                        }
                    }
                }
            });
        }
        
        private boolean Checkable()
        {
            if(Tool.isTrimEmpty(name.getText().toString()))
            {
                Toast toast = Toast.makeText(context, "请输入姓名", Toast.LENGTH_SHORT);
                toast.show();
                return false;
            }
            if(Tool.isTrimEmpty(dayet.getText().toString()))
            {
                Toast toast = Toast.makeText(context, "请输入姓名", Toast.LENGTH_SHORT);
                toast.show();
                return false;
            }
            return true;
        }
        
        DialogPassinterface passinterface;
        
        public void setDialogPassinterface(DialogPassinterface passinterface)
        {
            this.passinterface = passinterface;
        }
        
        public interface DialogPassinterface
        {
            public void pass();
            
        }
        
    }