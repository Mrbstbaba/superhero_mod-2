package com.superheromod;

import com.superheromod.gui.SuperheroMenuScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class SuperheroClient implements ClientModInitializer {

    // Bos duran bir tus: varsayilan olarak INSERT tusu.
    // Cakisma yasarsan oyun ici Kontroller menusunden degistirebilirsin.
    public static KeyBinding OPEN_MENU_KEY;

    @Override
    public void onInitializeClient() {
        OPEN_MENU_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.superheromod.open_menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_INSERT,
                "category.superheromod.general"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (OPEN_MENU_KEY.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new SuperheroMenuScreen());
                }
            }
        });
    }
}
