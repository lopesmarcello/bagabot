package com.github.xexelo.audio;

import java.util.HashMap;
import java.util.Map;

public class AudioManager {
    /*
     * A custom class used to store all th GuildMusicManagers
     */

    private static final Map<Long, ServerMusicManager> managers = new HashMap<>();

    /**
     * Retrieves the server music manager dedicated for the server
     * @param server the server's(guild) id number
     * @return ServerMusicManager
     */
    public static ServerMusicManager get(long server){
        // If doesn't exist then creates
        if(!managers.containsKey(server)){
            managers.put(server, new ServerMusicManager(PlayerManager.getManager()));
        }

        return managers.get(server);
    }
}
