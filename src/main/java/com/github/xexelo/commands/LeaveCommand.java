package com.github.xexelo.commands;

import com.github.xexelo.audio.AudioManager;
import com.github.xexelo.base.ServerCommand;
import com.github.xexelo.base.TextResponse;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

public class LeaveCommand extends ServerCommand {
    public LeaveCommand(){
        super("leave");
    }

    @Override
    protected void runCommand(MessageCreateEvent event, Server server, ServerTextChannel channel, User user, String[] args) {
        // Checks if bot is connected to any voice channel
        server.getConnectedVoiceChannel(event.getApi().getYourself()).ifPresentOrElse(voiceChannel -> {
            // Leave the voice channel and cut off any music
            server.getAudioConnection().ifPresentOrElse(audioConnection -> {
                AudioManager.get(server.getId()).player.stopTrack();
                audioConnection.close();
            }, () -> event.getChannel().sendMessage(TextResponse.botIsntInAnyVoiceChannelMessage));
        }, () -> event.getChannel().sendMessage(TextResponse.botIsntInAnyVoiceChannelMessage));
    }
}
