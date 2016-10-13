package com.zmx.youguibao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.duanqu.qupai.sdk.android.QupaiManager;
import com.duanqu.qupai.sdk.android.QupaiService;
import com.duanqu.qupai.utils.AppGlobalSetting;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.jauker.widget.BadgeView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zmx.youguibao.fragment.FindFragment;
import com.zmx.youguibao.fragment.FollowFragment;
import com.zmx.youguibao.qupai.util.RecordResult;
import com.zmx.youguibao.qupai.util.RequestCode;
import com.zmx.youguibao.ui.FeedbackActivity;
import com.zmx.youguibao.ui.LoginActivity;
import com.zmx.youguibao.ui.MessageActivity;
import com.zmx.youguibao.ui.NearbyActivity;
import com.zmx.youguibao.ui.PersonalCenterActivity;
import com.zmx.youguibao.ui.PublishActivity;
import com.zmx.youguibao.ui.SetUpActivity;
import com.zmx.youguibao.utils.UrlConfig;
import com.zmx.youguibao.utils.view.ImageLoadOptions;
import com.zmx.youguibao.utils.view.ImageViewUtil;
import com.zmx.youguibao.utils.view.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener{

    private Context context = this;
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private List<Fragment> list_fragment;
    private FollowFragment ff;//关注的fragment
    private FindFragment fd;//发现的fragment
//    private NearbyFragment nf;//同城的fragment

    //tablayout的标题
    private String[] mTitles = new String[]{"发现","关注"};

    private Toolbar toolbar;//头部

    private String accessToken;//趣拍

    //tab
    private FloatingActionButton fab_1;
    private FloatingActionsMenu fam;

    //左侧头部
    private NavigationView mNavigationView;//左侧头部
    private View headerLayout;//左侧头部
    private ImageViewUtil head;//左侧头像
    private RelativeLayout message_layout;//消息
    private TextView name;//左侧名称
    private BadgeView badgeView;//消息提示红点

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        StatusBarUtil.setColorForDrawerLayout(this, drawerLayout,255);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tabLayout = (TabLayout) toolbar.findViewById(R.id.sliding_tabs);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        headerLayout = mNavigationView.inflateHeaderView(R.layout.nav_header_main);
        initView();


    }

    private void initView(){

        head = (ImageViewUtil) headerLayout.findViewById(R.id.nav_head);
        head.setOnClickListener(this);
        message_layout = (RelativeLayout) headerLayout.findViewById(R.id.message_layout);
        message_layout.setOnClickListener(this);
        badgeView = new BadgeView(this);
        badgeView.setTargetView(message_layout);
        //设置相对位置
        badgeView.setBadgeMargin(0, 5, 15, 0);
        //设置显示未读消息条数
        badgeView.setBadgeCount(2);
        name = (TextView) headerLayout.findViewById(R.id.nav_name);
        UpdateMessage();

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        ff = new FollowFragment();
        fd = new FindFragment();
//        nf = new NearbyFragment();
        list_fragment = new ArrayList<>();
//        list_fragment.add(nf);
        list_fragment.add(fd);
        list_fragment.add(ff);
        /*viewPager通过适配器与fragment关联*/
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return list_fragment.get(position);
            }

            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }
        });
        //TabLayout和ViewPager的关联
        tabLayout.setupWithViewPager(mViewPager);
        fab_1 = (FloatingActionButton) findViewById(R.id.fab_1);
        fab_1.setOnClickListener(this);
        fam = (FloatingActionsMenu) findViewById(R.id.fab_menu);

        //注册监听广播
        IntentFilter filter = new IntentFilter(LoginActivity.action);
        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {

            drawer.closeDrawer(GravityCompat.START);

        } else {

            super.onBackPressed();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_pubish) {

            QupaiService qupaiService = QupaiManager
                    .getQupaiService(context);
            //引导，只显示一次，这里用SharedPreferences记录
            final AppGlobalSetting sp = new AppGlobalSetting(getApplicationContext());
            Boolean isGuideShow = sp.getBooleanGlobalItem(
                    "Qupai_has_video_exist_in_user_list_pref", true);

            qupaiService.showRecordPage(MainActivity.this, RequestCode.RECORDE_SHOW, isGuideShow);


        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {


        } else if (id == R.id.nav_gallery) {

            if(!SharePreferenceUtil.getInstance(this).getString(SharePreferenceUtil.u_id,"").equals("")){



            }else{

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

            }
        } else if (id == R.id.nav_slideshow) {

            if(!SharePreferenceUtil.getInstance(this).getString(SharePreferenceUtil.u_id,"").equals("")){

                Intent intent = new Intent(this, SetUpActivity.class);
                startActivity(intent);

            }else{

                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);

            }


        }else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }


    String videoFile;
    String [] thum;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            RecordResult result =new RecordResult(data);
            //得到视频地址，和缩略图地址的数组，返回十张缩略图
            videoFile = result.getPath();
            thum = result.getThumbnail();
            result.getDuration();

            Intent intent = new Intent(this,PublishActivity.class);
            intent.putExtra("url",videoFile);
            intent.putExtra("img",thum[0]);
            startActivity(intent);

        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.fab_1:

                if(!SharePreferenceUtil.getInstance(this).getString(SharePreferenceUtil.u_id,"").equals("")){
                    Intent i = new Intent(this, NearbyActivity.class);
                    startActivity(i);

                }else{
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                fam.toggle();
                break;

            case R.id.nav_head:

                Intent intent = new Intent(this, PersonalCenterActivity.class);
                intent.putExtra("uid",SharePreferenceUtil.getInstance(this).getString(SharePreferenceUtil.u_id,""));
                startActivity(intent);

                break;

            case R.id.message_layout:

                if(!SharePreferenceUtil.getInstance(this).getString(SharePreferenceUtil.u_id,"").equals("")){
                    Intent i = new Intent(this, MessageActivity.class);
                    startActivity(i);

                }else{
                    Intent intents = new Intent(this, LoginActivity.class);
                    startActivity(intents);
                }

                break;

        }

    }

    //注册广播监听是否登录
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            UpdateMessage();

        }
    };

    /**
     * 更新用户资料
     */
    public void UpdateMessage(){

        if(!SharePreferenceUtil.getInstance(this).getString(SharePreferenceUtil.u_id,"").equals("")){

            ImageLoader.getInstance().displayImage(UrlConfig.HEAD+SharePreferenceUtil.getInstance(this).getString(SharePreferenceUtil.u_headurl,""),head,
                    ImageLoadOptions.getOptions());
            name.setText(SharePreferenceUtil.getInstance(this).getString(SharePreferenceUtil.u_name,""));

        }else{

            head.setBackgroundResource(R.mipmap.ic_launcher);
            name.setText("点击头像登录...");

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

    }
}
