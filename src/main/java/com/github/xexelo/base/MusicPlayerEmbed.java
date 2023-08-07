package com.github.xexelo.base;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.util.concurrent.BlockingQueue;

public class MusicPlayerEmbed {
    public static EmbedBuilder build(AudioTrack audioTrack, BlockingQueue<AudioTrack> queue) {
        if (queue.isEmpty()) {
            return new EmbedBuilder()
                    .setTitle(audioTrack.getInfo().title)
                    .setAuthor(audioTrack.getInfo().author)
                    .addField("URL: ", audioTrack.getInfo().uri);
        } else {
            return new EmbedBuilder()
                    .addField("Faixa adicionada a fila na posicao: ", queue.size() + "")
                    .setTitle(audioTrack.getInfo().title)
                    .setAuthor(audioTrack.getInfo().author)
                    .addField("URL: ", audioTrack.getInfo().uri);
        }
    }
}
