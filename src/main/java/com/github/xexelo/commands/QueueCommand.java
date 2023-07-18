package com.github.xexelo.commands;

import com.github.xexelo.audio.AudioManager;
import com.github.xexelo.audio.ServerMusicManager;
import com.github.xexelo.base.ServerCommand;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.concurrent.BlockingQueue;

public class QueueCommand extends ServerCommand {

    public QueueCommand(){
        super("queue");
    }
    @Override
    protected void runCommand(MessageCreateEvent event, Server server, ServerTextChannel channel, User user, String[] args) {
        // First we check if queue is empty
        ServerMusicManager manager = AudioManager.get(server.getId());
        if (manager.scheduler.getQueue().isEmpty()) {
            channel.sendMessage("Fila está vazia");
            return;
        }

        // Then we build an Embed with all relevant information
        BlockingQueue<AudioTrack> queue = manager.scheduler.getQueue();
        EmbedBuilder embed = new EmbedBuilder().setTitle("Fila que ta teno:").setDescription(queue.size() + "faixas.");
        int index = 1;
        for (AudioTrack track : queue){
            embed.addField(index + " - ", track.getInfo().title);
            index++;
        }

        // Reply with that Embed
        channel.sendMessage(embed);
    }

}
