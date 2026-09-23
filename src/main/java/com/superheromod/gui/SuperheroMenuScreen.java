package com.superheromod.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

/**
 * Basit ac/kapat menusu. Sunucu tarafinda hangi guclerin
 * kazanilmis oldugu zaten kontrol ediliyor (bkz. SuperheroMod#powerToggleCommand),
 * bu yuzden butona her zaman basilabilir; guc kazanilmamissa
 * oyuncuya sohbet ekraninda uyari mesaji dusuyor.
 */
public class SuperheroMenuScreen extends Screen {

    public SuperheroMenuScreen() {
        super(Text.literal("Superkahraman Menusu"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = this.height / 2 - 70;

        addToggleButton("Iron Man Modu Ac/Kapat", "ironman", centerX, startY);
        addToggleButton("Hulk Modu Ac/Kapat", "hulk", centerX, startY + 25);
        addToggleButton("Spiderman Modu Ac/Kapat", "spiderman", centerX, startY + 50);

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Durumu Goster"), b -> {
            if (this.client != null && this.client.player != null) {
                this.client.player.networkHandler.sendChatCommand("superhero status");
            }
        }).dimensions(centerX - 75, startY + 85, 150, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Enfeksiyonu Tedavi Et"), b -> {
            if (this.client != null && this.client.player != null) {
                this.client.player.networkHandler.sendChatCommand("superhero cure");
            }
        }).dimensions(centerX - 75, startY + 110, 150, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Kapat"), b -> this.close())
                .dimensions(centerX - 75, startY + 145, 150, 20).build());
    }

    private void addToggleButton(String label, String power, int centerX, int y) {
        this.addDrawableChild(ButtonWidget.builder(Text.literal(label), b -> {
            if (this.client != null && this.client.player != null) {
                this.client.player.networkHandler.sendChatCommand("superhero " + power + " toggle");
            }
        }).dimensions(centerX - 75, y, 150, 20).build());
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
