package com.github.xexelo.commands;

import com.github.xexelo.base.ServerCommand;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

public class HelpCommand extends ServerCommand {
    public HelpCommand(){
        super("help");
    }
    @Override
    protected void runCommand(MessageCreateEvent event, Server server, ServerTextChannel channel, User user, String[] args) {

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Bagabot")
                .setDescription("lista de comandos do bot:")
                .addField("b.play", "+ nome ou URl da música para tocar no canal que você está conectado.\n")
                .addField("b.stop", "para de tocar a música.\n")
                .addField("b.leave", "remove o bot do canal.\n")
                .addField("b.nowplaying", "mostra a música tocando no momento.\n")
                .addField("b.queue", "mostra as músicas que estão na fila.\n")
                .addField("b.skip", "pula para a próxima da fila.\n")
                .addField("b.clear", "limpa a fila de músicas.\n")
                .addField("b.f1", "descubra.");

        event.getChannel().sendMessage(embed);
    }

}
