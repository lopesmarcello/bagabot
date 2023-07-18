package com.github.xexelo;

import com.github.xexelo.audio.PlayerManager;
import com.github.xexelo.commands.*;
import io.github.cdimascio.dotenv.Dotenv;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.util.logging.ExceptionLogger;
import org.javacord.api.util.logging.FallbackLoggerConfiguration;

public class Bagabot {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("DISCORD_BOT_TOKEN");

        // Setup Loggers
        FallbackLoggerConfiguration.setTrace(true);

        // Initializes the AudioPlayerManager
        PlayerManager.init();

        System.out.println("Ligando o bot...");

        new DiscordApiBuilder()
                .setToken(token)
                .setAllIntentsExcept(Intent.GUILD_MESSAGE_TYPING,  Intent.DIRECT_MESSAGE_TYPING, Intent.GUILD_INTEGRATIONS, Intent.GUILD_WEBHOOKS, Intent.GUILD_INVITES, Intent.GUILD_INTEGRATIONS,
                        Intent.GUILD_BANS, Intent.GUILD_EMOJIS, Intent.DIRECT_MESSAGES, Intent.GUILD_MESSAGE_TYPING, Intent.GUILD_MESSAGE_REACTIONS, Intent.GUILD_PRESENCES, Intent.DIRECT_MESSAGE_TYPING,
                        Intent.DIRECT_MESSAGE_REACTIONS)
                .setRecommendedTotalShards()
                .join()
                .loginAllShards()
                .forEach(shardFuture -> shardFuture.thenAccept(Bagabot::onShardLogin).exceptionally(ExceptionLogger.get()));
    }

    private static void onShardLogin(DiscordApi api){

        System.out.println("Connected to shard " + api.getCurrentShard());

        // Performance tasks
        api.setAutomaticMessageCacheCleanupEnabled(true);
        api.setMessageCacheSize(10, 60 * 5);
        api.setReconnectDelay(attempt -> attempt * 2);

        // Adds all the commands
        api.addListener(new ClearCommand());
        api.addListener(new F1Command());
        api.addListener(new HelpCommand());
        api.addListener(new LeaveCommand());
        api.addListener(new NowPlayingCommand());
        api.addListener(new PlayCommand());
        api.addListener(new QueueCommand());
        api.addListener(new SkipCommand());
        api.addListener(new StopCommand());

        // Prints out the bot is up and running.
        System.out.println("O bot está online!");
    }
}
