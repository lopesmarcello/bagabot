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
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

public class PlayCommand extends ServerCommand {

    // We retrieve the AudioPlayerManager from the PlayerManager
    AudioPlayerManager manager = PlayerManager.getManager();

    public PlayCommand() {
        super("play");
    }

    @Override
    protected void runCommand(MessageCreateEvent event, Server server, ServerTextChannel channel, User user, String[] args) {
        // Make sure the message have arguments above one (for example: play kano 2020 remix or play https://youtube.com/...).
        if (args.length > 1) {

            // We first check if the user is in any voice channel.
            event.getMessageAuthor().getConnectedVoiceChannel().ifPresentOrElse(voiceChannel -> {

                // Can we see, connect and speak in the channel?
                if (voiceChannel.canYouConnect() && voiceChannel.canYouSee() && voiceChannel.canYouSpeak()) {

                    // We retrieve the ServerMusicManager from the AudioManager class which will create it if it doesn't exist
                    ServerMusicManager m = AudioManager.get(server.getId());

                    // We retrieve the URL or the query
                    String query = event.getMessageContent().replace(args[0] + " ", "");

                    if (!voiceChannel.isConnected(event.getApi().getYourself()) && server.getAudioConnection().isEmpty()) {

                        voiceChannel.connect().thenAccept(audioConnection -> {
                            // Create an audio source and add to audio connection queue, this is where we use the ServerMusicManager as well
                            AudioSource audio = new LavaplayerAudioSource(event.getApi(), m.player);
                            audioConnection.setAudioSource(audio);
                            audioConnection.setSelfDeafened(true); // Deafens bot

                            // Plays the Music
                            play(query, channel, m);
                        });
                    } else if (server.getAudioConnection().isPresent()) {
                        // Gets the audio connection
                        server.getAudioConnection().ifPresent(audioConnection -> {
                            // Checks if the user is in the same channel as the bot.
                            if (audioConnection.getChannel().getId() == voiceChannel.getId()) {
                                // Create an audio source and add to audio connection queue, this is where we use the ServerMusicManager as well.
                                AudioSource audio = new LavaplayerAudioSource(event.getApi(), m.player);
                                audioConnection.setAudioSource(audio);
                                audioConnection.setSelfDeafened(true); // Deafens bot

                                // Plays the music
                                play(query, channel, m);
                            } else {
                                event.getChannel().sendMessage(TextResponse.notInTheSameChannelMessage);
                            }
                        });
                    }

                } else {
                    // Tell the user that we cannot connect or see or speak in the channel
                    event.getChannel().sendMessage(TextResponse.cannotConnectSeeOrSpeakInTheChannelMessage);
                }
            }, () -> event.getChannel().sendMessage(TextResponse.userNotConnectedInAnyVoiceChannelMessage));
        }
    }

    /**
     * Plays the music and notifies the user that we have successfully played the music
     * @param query the query to search for
     * @param channel channel where the command was sent
     * @param m the server music manager
     */
    private void play(String query, ServerTextChannel channel, ServerMusicManager m){
        // Load the track, we use isUrl to see if the argument is a URl, otherwise if it is not then we use YouTube Search to search the query
        manager.loadItemOrdered(m, isUrl(query) ? query : "ytsearch: " + query, new FunctionalResultHandler(audioTrack -> {
            // This is for track loaded.
            channel.sendMessage(TextResponse.addTrackPrefix + audioTrack.getInfo().title);
            m.scheduler.queue(audioTrack);
        }, audioPlaylist -> {
            // If the playlist is a search result, then we only need to get the first one.
            if (audioPlaylist.isSearchResult()){
                m.scheduler.queue(audioPlaylist.getTracks().get(0));
                channel.sendMessage(TextResponse.addTrackPrefix + audioPlaylist.getTracks().get(0).getInfo().title);
            } else {
                // If it isn`t then simply queue every track
                audioPlaylist.getTracks().forEach(audioTrack -> {
                    m.scheduler.queue(audioTrack);
                    channel.sendMessage(TextResponse.addTrackPrefix + audioTrack.getInfo().title);
                });
            }
        }, () -> {
            // if there are no matches, then we tell the user that we couldn't find any track.
            channel.sendMessage(TextResponse.trackNotFoundMessage);
        }, e-> {
            //in case of exception
            channel.sendMessage("ERROR:\n" + e.getMessage());
        }));
    }

    /**
     * Check if string is URL
     * @param argument the string to validate
     * @return boolean
     */
    private boolean isUrl(String argument){
        return argument.startsWith("https://") || argument.startsWith("http://");
    }
}
