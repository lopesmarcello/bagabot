package com.github.xexelo.commands;

import com.github.xexelo.audio.AudioManager;
import com.github.xexelo.audio.ServerMusicManager;
import com.github.xexelo.base.ServerCommand;
import com.github.xexelo.base.TextResponse;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

public class StopCommand extends ServerCommand {

    public StopCommand(){
        super("stop");
    }
    @Override
    protected void runCommand(MessageCreateEvent event, Server server, ServerTextChannel channel, User user, String[] args) {

        // Checks if there is any audio connection.
        server.getAudioConnection().ifPresentOrElse(audioConnection -> {

            // If there is an audio connection, then we stop the music.
            ServerMusicManager manager = AudioManager.get(server.getId());
            String currentTrackTitle = manager.player.getPlayingTrack().getInfo().title;
            manager.player.stopTrack();
            event.getChannel().sendMessage(currentTrackTitle + TextResponse.stopTrackSufix);

        }, () -> event.getChannel().sendMessage(TextResponse.botIsntPlayingMusicMessage));
    }
}
