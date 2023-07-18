package com.github.xexelo.commands;

import com.github.xexelo.audio.AudioManager;
import com.github.xexelo.audio.ServerMusicManager;
import com.github.xexelo.base.ServerCommand;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

public class ClearCommand extends ServerCommand {

    public ClearCommand(){
        super("clear");
    }
    @Override
    protected void runCommand(MessageCreateEvent event, Server server, ServerTextChannel channel, User user, String[] args) {
        // First we check if queue is empty
        ServerMusicManager manager = AudioManager.get(server.getId());
        if(manager.scheduler.getQueue().isEmpty()){
            channel.sendMessage("Fila está vazia");
            return;
        }

        int currentQueueSize = manager.scheduler.getQueue().size();
        manager.scheduler.getQueue().clear();
        channel.sendMessage("Foram removidos " +  currentQueueSize +" itens da fila!");
    }
}
