package com.superheromod.power;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Her oyuncu icin hangi gucleri kazandigini ve hangilerinin su an
 * aktif (acik) oldugunu tutan basit, sunucu-tarafi bellek-ici kayit.
 *
 * NOT: Bu veri su anki haliyle sunucu yeniden baslatildiginda sifirlanir
 * (kalici degildir). Kalicilik istenirse PersistentState / NBT ile
 * genisletilebilir - istersen bir sonraki adimda bunu ekleyebilirim.
 */
public class PlayerPowers {
    private static final Map<UUID, PlayerPowers> DATA = new HashMap<>();

    // Oyuncu bu gucu hic kazandi mi?
    public boolean hasIronMan = false;
    public boolean hasHulk = false;
    public boolean hasSpiderman = false;
    public boolean infected = false;

    // Oyuncu bu gucu su an ACIK mi kullaniyor?
    public boolean ironManActive = false;
    public boolean hulkActive = false;
    public boolean spidermanActive = false;

    public static PlayerPowers get(UUID uuid) {
        return DATA.computeIfAbsent(uuid, id -> new PlayerPowers());
    }

    public boolean hasAllThree() {
        return hasIronMan && hasHulk && hasSpiderman;
    }
}
