package dev.komp15.utils;

import java.util.Collection;
import java.util.stream.Collectors;

public class PlayerCollectionUtils {
    public static Collection<String> filterPlayerNames(Collection<String> playerNames, String invokerName){
        playerNames = playerNames.stream()
                .filter(name -> name.length() > 1 && name.charAt(0) != '!')
                .collect(Collectors.toList());
        playerNames.remove(invokerName);
        return playerNames;
    }
}
