package io.github.hsyyid.kits.cmds;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.spongepowered.api.Game;

import static com.google.common.base.Preconditions.checkNotNull;

public class DefaultModule extends AbstractModule {

    private final Game game;

    public DefaultModule(Game game) {
        checkNotNull(game);
        this.game = game;
    }

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    Game provideGame() {
        return game;
    }

}