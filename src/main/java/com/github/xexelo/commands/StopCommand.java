package com.github.xexelo.commands;

import com.github.xexelo.audio.AudioManager;
import com.github.xexelo.base.ServerCommand;
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
            AudioManager.get(server.getId()).player.stopTrack();
            event.getChannel().sendMessage("The track stopped.");

        }, () -> event.getChannel().sendMessage("The bot isn't playing music."));
    }
}
