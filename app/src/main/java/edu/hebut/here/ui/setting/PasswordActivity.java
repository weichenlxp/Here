package edu.hebut.here.ui.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.king.view.splitedittext.SplitEditText;

import edu.hebut.here.R;

public class PasswordActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int userID;
    String password;
    SplitEditText splitEditText;
    ImageView imageView;
    TextView step, label, done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("here", Context.MODE_PRIVATE);
        userID = sharedPreferences.getInt("userID", -1);
        password = sharedPreferences.getString("password", null);
        editor = sharedPreferences.edit();
        setContentView(R.layout.activity_password);
        imageView = findViewById(R.id.password_img);
        step = findViewById(R.id.step);
        label = findViewById(R.id.label_set_password);
        done = findViewById(R.id.label_done);
        splitEditText = findViewById(R.id.set_password);
        if (password != null) {
            getSupportActionBar().setTitle("输入密码");
            step.setText("Step 1/1");
            label.setText("请输入当前密码");
            splitEditText = findViewById(R.id.set_password);
            splitEditText.setOnTextInputListener(new SplitEditText.OnTextInputListener() {
                @Override
                public void onTextInputChanged(String text, int length) {
                    //TODO 文本输入改变
                }

                @Override
                public void onTextInputCompleted(String text) {
                    String old_password = String.valueOf(splitEditText.getText());
                    if (!password.equals(old_password)) {
                        editor.putInt("chance", sharedPreferences.getInt("chance", 5) - 1);
                        editor.apply();
                        label.setText("密码错误！您还有" + sharedPreferences.getInt("chance", 5) + "次机会");
                        label.setTextColor(Color.RED);
                    } else {
                        editor.putInt("chance", 5);
                        editor.apply();
                        getSupportActionBar().setTitle("请选择操作");
                        setContentView(R.layout.activity_password_item);
                        ImageView closePassword = findViewById(R.id.close_password);
                        ImageView resetPassword = findViewById(R.id.reset_password);
                        closePassword.setOnClickListener(v -> {
                            editor.putString("password", null);
                            editor.apply();
                            getSupportActionBar().setTitle("完成");
                            setContentView(R.layout.activity_password_done);
                            done = findViewById(R.id.label_done);
                            done.setText("密码删除成功！");
                        });
                        resetPassword.setOnClickListener(v -> {
                            getSupportActionBar().setTitle("输入新密码");
                            setContentView(R.layout.activity_password);
                            step.findViewById(R.id.step);
                            step.setVisibility(View.VISIBLE);
                            step.setText("Step 1/1");
                            label.setText("请输入新密码");
                            splitEditText = findViewById(R.id.set_password);
                            splitEditText.setOnTextInputListener(new SplitEditText.OnTextInputListener() {
                                @Override
                                public void onTextInputChanged(String text, int length) {
                                    //TODO 文本输入改变
                                }

                                @Override
                                public void onTextInputCompleted(String text) {
                                    String re_password = String.valueOf(splitEditText.getText());
                                    editor.putString("password", re_password);
                                    editor.apply();
                                    getSupportActionBar().setTitle("完成");
                                    setContentView(R.layout.activity_password_done);
                                    done = findViewById(R.id.label_done);
                                    done.setText("密码修改成功！");
                                }
                            });
                        });
                    }
                }
            });
        } else {
            getSupportActionBar().setTitle("创建密码");
            step.setText("Step 1/2");
            label.setText("请输入新密码");
            splitEditText.setOnTextInputListener(new SplitEditText.OnTextInputListener() {
                @Override
                public void onTextInputChanged(String text, int length) {
                    //TODO 文本输入改变
                }

                @Override
                public void onTextInputCompleted(String text) {
                    String password = String.valueOf(splitEditText.getText());
                    step.setText("Step 2/2");
                    label.setText("请再输入一次新密码");
                    splitEditText.setText("");
                    splitEditText.setOnTextInputListener(new SplitEditText.OnTextInputListener() {
                        @Override
                        public void onTextInputChanged(String text, int length) {
                            //TODO 文本输入改变
                        }

                        @Override
                        public void onTextInputCompleted(String text) {
                            String re_password = String.valueOf(splitEditText.getText());
                            if (!re_password.equals(password)) {
                                label.setText("密码输入不一致，请重新输入");
                                label.setTextColor(Color.RED);
                            } else {
                                editor.putString("password", re_password);
                                editor.apply();
                                getSupportActionBar().setTitle("完成");
                                setContentView(R.layout.activity_password_done);
                                done = findViewById(R.id.label_done);
                                done.setText("密码创建成功！");
                            }
                        }
                    });
                }
            });
        }
    }
}
