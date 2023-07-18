package com.github.xexelo.base;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.javacord.api.entity.message.embed.EmbedBuilder;

public class MusicPlayerEmbed {
    public static EmbedBuilder build(AudioTrack audioTrack){
        return new EmbedBuilder()
                .setTitle(audioTrack.getInfo().title)
                .setAuthor(audioTrack.getInfo().author)
                .addField("URL: ", audioTrack.getInfo().uri);
    }

}
