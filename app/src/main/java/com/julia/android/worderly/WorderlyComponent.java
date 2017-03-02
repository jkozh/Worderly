package com.julia.android.worderly;

import com.julia.android.worderly.ui.chat.view.ChatFragment;
import com.julia.android.worderly.ui.game.view.GameFragment;
import com.julia.android.worderly.ui.main.MainActivity;
import com.julia.android.worderly.ui.search.SearchOpponentActivity;
import com.julia.android.worderly.ui.signin.SignInActivity;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {AppModule.class, WorderlyModule.class})
public interface WorderlyComponent {

    void inject(App app);

    void inject(SignInActivity activity);

    void inject(MainActivity activity);

    void inject(SearchOpponentActivity activity);

    void inject(GameFragment fragment);

    void inject(ChatFragment fragment);

    /**
     * An initializer that creates the graph from an application.
     */
    final class Initializer {
        static WorderlyComponent init(App app) {
            return DaggerWorderlyComponent.builder()
                    .appModule(new AppModule(app))
                    .build();
        }
        private Initializer() {} // No instances.
    }

}