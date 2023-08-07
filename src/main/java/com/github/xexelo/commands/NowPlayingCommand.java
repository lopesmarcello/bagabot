package com.github.xexelo.commands;

import com.github.xexelo.audio.AudioManager;
import com.github.xexelo.audio.ServerMusicManager;
import com.github.xexelo.base.MusicPlayerEmbed;
import com.github.xexelo.base.ServerCommand;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

public class NowPlayingCommand extends ServerCommand {

    public NowPlayingCommand(){
        super("nowplaying");
    }
    @Override
    protected void runCommand(MessageCreateEvent event, Server server, ServerTextChannel channel, User user, String[] args) {
        ServerMusicManager manager = AudioManager.get(server.getId());

        AudioTrack track = manager.player.getPlayingTrack();

        if(track == null){
            channel.sendMessage("Seems like the bot isn't playing anything.");
            return;
        }

        channel.sendMessage(MusicPlayerEmbed.build(track, manager.scheduler.getQueue()));
    }
}
