package com.julia.android.worderly.ui.game.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.julia.android.worderly.R;
import com.julia.android.worderly.ui.chat.view.ChatFragment;
import com.julia.android.worderly.ui.game.adapter.GamePagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameActivity extends AppCompatActivity implements GameActivityView {

    private static final String TAG = GameActivity.class.getSimpleName();
    private final int GAME_TAB = 0;
    private final int CHAT_TAB = 1;
    @BindView(R.id.toolbar_game_activity) Toolbar mToolbar;
    @BindView(R.id.viewpager) ViewPager mViewPager;
    @BindView(R.id.tabs) TabLayout mTabLayout;
    GameFragment mGameFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        if (mViewPager != null) {
            setupViewPager(mViewPager);
        }
        mTabLayout.setupWithViewPager(mViewPager);
        setupTabIcons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_resign:
                mGameFragment.resign();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * When back button pressed - don't close the current game, just minimize it.
     */
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    /**
     * Method used to indicate the new message received.
     */
    @Override
    public void setChatTabNewMessageTitle() {
        TabLayout.Tab chatTab = mTabLayout.getTabAt(CHAT_TAB);
        if (chatTab != null) {
            chatTab.setText(getString(R.string.title_chat_new));
        }
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
        mGameFragment = (GameFragment) adapter.getItem(GAME_TAB);
    }
}
