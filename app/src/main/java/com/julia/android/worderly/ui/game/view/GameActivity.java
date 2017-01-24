package com.julia.android.worderly.ui.game.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.julia.android.worderly.R;
import com.julia.android.worderly.ui.chat.view.ChatFragment;
import com.julia.android.worderly.ui.game.adapter.GamePagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameActivity extends AppCompatActivity implements GameActivityView,
        ViewPager.OnPageChangeListener {

    private static final String TAG = GameActivity.class.getSimpleName();
    private final int GAME_TAB = 0;
    private final int CHAT_TAB = 1;
    @BindView(R.id.toolbar_game_activity) Toolbar mToolbar;
    @BindView(R.id.viewpager) ViewPager mViewPager;
    @BindView(R.id.tabs) TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate CALLED");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        if (mViewPager != null) {
            setupViewPager(mViewPager);
            mViewPager.addOnPageChangeListener(this);
        }
        mTabLayout.setupWithViewPager(mViewPager);
        setupTabIcons();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart CALLED");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume CALLED");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause CALLED");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop CALLED");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy CALLED");
        super.onDestroy();
    }

    /**
     * Method used to indicate the number of new chat messages received.
     */
    @Override
    public void setChatTabNewMessageTitle() {
        TabLayout.Tab chatTab = mTabLayout.getTabAt(CHAT_TAB);
        if (chatTab != null) {
            chatTab.setText(getString(R.string.title_chat_new));
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        // TODO: Remove viewed messages notification in tab layout
//        if (position == CHAT_TAB)
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private void setupTabIcons() {
        TabLayout.Tab chatTab = mTabLayout.getTabAt(CHAT_TAB);
        if (chatTab != null) {
            chatTab.setIcon(R.drawable.ic_tab_chat);
        }
    }

    /**
     * This method will call Adapter for ViewPager.
     */
    private void setupViewPager(ViewPager viewPager) {
        GamePagerAdapter adapter = new GamePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GameFragment(), getString(R.string.title_game));
        adapter.addFragment(new ChatFragment(), getString(R.string.title_chat));
        viewPager.setAdapter(adapter);
//        mChatFragment = (ChatFragment) adapter.getItem(CHAT_TAB);
    }
}
