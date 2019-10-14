package com.botongglcontroller.activity;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import com.botongglcontroller.Fragment.NewsFragment;
import com.botongglcontroller.Fragment.PersonalFragment;
import com.botongglcontroller.Fragment.GuolulistActivity;
import com.botongglcontroller.R;
import com.botongglcontroller.Intents;
import com.botongglcontroller.utils.ToastUtil;

public class MainActivity extends BaseActivity {
    private long touchTime = 0;
    RadioGroup MyrGroup;
    RelativeLayout rb;
    RadioButton mGuolu, mNews, mSuggestion, mPersonal;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
//        rb = (RelativeLayout) findViewById(R.id.rl_main);
        MyrGroup = (RadioGroup) findViewById(R.id.rg_main);

        mGuolu = (RadioButton) findViewById(R.id.rb_guolu);
        mNews = (RadioButton) findViewById(R.id.rb_news);
        mSuggestion = (RadioButton) findViewById(R.id.rb_suggestion);
        mPersonal = (RadioButton) findViewById(R.id.rb_personal);
    }

    @Override
    public void initData() {

        Intent intent = getIntent();
//        if (intent.getIntExtra("guolu", 1) == 1) {
//            mGuolu.setChecked(true);
//        }
        if (intent.getIntExtra("news", 1) == 2) {
            mNews.setChecked(true);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl, new NewsFragment()).commit();
        }
        if (intent.getIntExtra("suggestion", 1) == 3) {
            mSuggestion.setChecked(true);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl, new PublishSuggestionActivity()).commit();
        }
        if (intent.getIntExtra("personal", 1) == 4) {
            mPersonal.setChecked(true);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl, new PersonalFragment()).commit();
        }


    }

    @Override
    public void initListener() {
        MyrGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub
                FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                        .beginTransaction();
                switch (arg1) {
                    case R.id.rb_home:
                        Intents.getIntents().Intent(MainActivity.this, HomeActivity.class);
                        finish();

                        break;
                    case R.id.rb_guolu:
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.fl, new GuolulistFragment()).commit();
                        Intents.getIntents().Intent(MainActivity.this, GuolulistActivity.class);
                        finish();
                        break;
                    case R.id.rb_news:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fl, new NewsFragment()).commit();
                        break;
                    case R.id.rb_suggestion:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fl, new PublishSuggestionActivity()).commit();
                        break;
                    case R.id.rb_personal:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fl, new PersonalFragment()).commit();
                        break;

                    default:
                        break;
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {


        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - touchTime > 1500) {
                ToastUtil.showToast(MainActivity.this, "再按一次退出应用");
                touchTime = System.currentTimeMillis();
            } else {
//                System.exit(0);
                finish();
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private final static int SCANNIN_GREQUEST_CODE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    //显示扫描到的内容
                    Log.i("扫描", bundle.getString("result"));

                    Intents.getIntents().Intent(MainActivity.this, BindGuoluActivity.class);

                }
                break;
        }
    }
}
