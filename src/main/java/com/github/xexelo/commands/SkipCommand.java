package com.github.xexelo.commands;

import com.github.xexelo.audio.AudioManager;
import com.github.xexelo.audio.ServerMusicManager;
import com.github.xexelo.base.ServerCommand;
import com.github.xexelo.base.TextResponse;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

public class SkipCommand extends ServerCommand {

    public SkipCommand(){
        super("skip");
    }

    @Override
    protected void runCommand(MessageCreateEvent event, Server server, ServerTextChannel channel, User user, String[] args) {
        // Checks if there are any audio connection
        server.getAudioConnection().ifPresentOrElse(audioConnection -> {

            // If there is an audio connection then we skip the track.
            ServerMusicManager audioManager = AudioManager.get(server.getId());
            String currentTrackTitle = audioManager.player.getPlayingTrack().getInfo().title;
            audioManager.scheduler.nextTrack();
            event.getChannel().sendMessage(TextResponse.skipTrackPrefix + currentTrackTitle);
        }, () -> event.getChannel().sendMessage(TextResponse.botIsntPlayingMusicMessage));
    }
}
