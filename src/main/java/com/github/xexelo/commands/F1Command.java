package com.github.xexelo.commands;

import com.github.xexelo.audio.AudioManager;
import com.github.xexelo.audio.LavaplayerAudioSource;
import com.github.xexelo.audio.PlayerManager;
import com.github.xexelo.audio.ServerMusicManager;
import com.github.xexelo.base.ServerCommand;
import com.github.xexelo.base.TextResponse;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.FunctionalResultHandler;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

public class F1Command extends ServerCommand {
    public F1Command(){
        super("f1");
    }

    private final static String videoUrl = "https://www.youtube.com/watch?v=NYvpK1-P2JA";

    AudioPlayerManager manager = PlayerManager.getManager();
    @Override
    protected void runCommand(MessageCreateEvent event, Server server, ServerTextChannel channel, User user, String[] args) {
        // Check if user is in any voice channel
        event.getMessageAuthor().getConnectedVoiceChannel().ifPresentOrElse(serverVoiceChannel -> {

            // Can we see, connect and speak in the channel?
            if (serverVoiceChannel.canYouConnect() && serverVoiceChannel.canYouSpeak() && serverVoiceChannel.canYouSee()){

                // We retrieve the ServerMusicManager from the AudioManager class
                ServerMusicManager m = AudioManager.get(server.getId());

                if (!serverVoiceChannel.isConnected(event.getApi().getYourself()) && server.getAudioConnection().isEmpty()){

                    serverVoiceChannel.connect().thenAccept(audioConnection -> {

                        AudioSource audio = new LavaplayerAudioSource(event.getApi(), m.player);
                        audioConnection.setAudioSource(audio);
                        audioConnection.setSelfDeafened(true);
                        play(videoUrl, channel, m, event);
                    });
                } else if (server.getAudioConnection().isPresent()){
                    server.getAudioConnection().ifPresent(audioConnection -> {
                        if(audioConnection.getChannel().getId() == serverVoiceChannel.getId()){
                            AudioSource audio = new LavaplayerAudioSource(event.getApi(), m.player);
                            audioConnection.setAudioSource(audio);
                            audioConnection.setSelfDeafened(true);

                            play(videoUrl, channel, m, event);
                        } else {
                            event.getChannel().sendMessage("Você precisa estar no mesmo canal que eu pra me passar a bufa.");
                        }
                    });
                }
            } else {
                event.getChannel().sendMessage("Esse servidor é brecado parça, tenta me chamar em outro...");
            }
        }, () -> event.getChannel().sendMessage("Entre em um canal de voz antes de legalizar."));
    }


    private void play(String query, ServerTextChannel channel, ServerMusicManager m, MessageCreateEvent event){
        EmbedBuilder embed = new EmbedBuilder().setTitle( event.getMessage().getAuthor().getName() + " LEGALIZOU A BOCA \uD83D\uDC80");
        m.player.stopTrack();
        m.scheduler.getQueue().clear();
        manager.loadItemOrdered(m, query, new FunctionalResultHandler(audioTrack -> {
            m.player.startTrack(audioTrack, true);
            channel.sendMessage(embed);
        }, playlist -> channel.sendMessage("Ops, algo deu errado" + playlist.getTracks().toString()),
         () -> {
            // if there are no matches, then we tell the user that we couldn't find any track.
            channel.sendMessage(TextResponse.trackNotFoundMessage);
        }, e -> {//in case of exception
            channel.sendMessage("ERROR:\n" + e.getMessage());
        }));

    }


}
