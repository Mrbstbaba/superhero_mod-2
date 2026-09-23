package com.superheromod;

import com.superheromod.item.ModItems;
import com.superheromod.power.PlayerPowers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class SuperheroMod implements ModInitializer {
    public static final String MOD_ID = "superheromod";

    // Efektleri her tickte degil, ~1 saniyede bir uygulamak icin sayac
    private int tickCounter = 0;

    @Override
    public void onInitialize() {
        ModItems.register();
        registerLootInjections();
        registerCommands();
        registerPowerEffectLoop();
    }

    // ---------------------------------------------------------------
    // 1) Dusme mantigi: Demir Golem, Creeper (Hulk aktifken), Spider
    // ---------------------------------------------------------------
    private void registerLootInjections() {
        // Demir Golem -> %50 Iron Man kostumu
        LootTableEvents.MODIFY_DROPS.register((key, tableBuilder, source, context) -> {
            if (key.equals(EntityType.IRON_GOLEM.getLootTableId())) {
                addChanceDrop(tableBuilder, ModItems.IRON_MAN_COSTUME, 0.5f);
            }
        });

        // Spider -> %1 Spiderman yumurtasi, yoksa dusuk ihtimalle enfeksiyon
        LootTableEvents.MODIFY_DROPS.register((key, tableBuilder, source, context) -> {
            if (key.equals(EntityType.SPIDER.getLootTableId())) {
                addChanceDrop(tableBuilder, ModItems.SPIDERMAN_EGG, 0.01f);
                addChanceDrop(tableBuilder, ModItems.SPIDER_INFECTION, 0.05f);
            }
        });

        // Creeper, Hulk modu ACIK bir oyuncu tarafindan oldurulursen -> %1 Hulk esyasi
        LootTableEvents.MODIFY_DROPS.register((key, tableBuilder, source, context) -> {
            if (key.equals(EntityType.CREEPER.getLootTableId())) {
                var killer = context.get(LootContextParameters.KILLER_ENTITY);
                if (killer instanceof ServerPlayerEntity player) {
                    PlayerPowers powers = PlayerPowers.get(player.getUuid());
                    if (powers.hulkActive) {
                        addChanceDrop(tableBuilder, ModItems.HULK_ITEM, 0.01f);
                    }
                }
            }
        });
    }

    private void addChanceDrop(net.minecraft.loot.LootTable.Builder tableBuilder, net.minecraft.item.Item item, float chance) {
        LootPool.Builder pool = LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .with(ItemEntry.builder(item))
                .conditionally(RandomChanceLootCondition.builder(chance));
        tableBuilder.pool(pool);
    }

    // ---------------------------------------------------------------
    // 2) Komutlar: /superhero <guc> toggle, /superhero status, /superhero cure
    // ---------------------------------------------------------------
    private void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(CommandManager.literal("superhero")
                        .then(powerToggleCommand("ironman"))
                        .then(powerToggleCommand("hulk"))
                        .then(powerToggleCommand("spiderman"))
                        .then(CommandManager.literal("cure").executes(ctx -> {
                            ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();
                            PlayerPowers p = PlayerPowers.get(player.getUuid());
                            if (!p.infected) {
                                ctx.getSource().sendFeedback(() -> Text.literal("Zaten enfekte degilsin."), false);
                                return 0;
                            }
                            p.infected = false;
                            ctx.getSource().sendFeedback(() -> Text.literal("Orumcek enfeksiyonundan kurtuldun!"), false);
                            return 1;
                        }))
                        .then(CommandManager.literal("status").executes(ctx -> {
                            ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();
                            PlayerPowers p = PlayerPowers.get(player.getUuid());
                            ctx.getSource().sendFeedback(() -> Text.literal(
                                    "Iron Man: " + statusText(p.hasIronMan, p.ironManActive) +
                                    " | Hulk: " + statusText(p.hasHulk, p.hulkActive) +
                                    " | Spiderman: " + statusText(p.hasSpiderman, p.spidermanActive) +
                                    " | Enfekte: " + (p.infected ? "EVET" : "hayir")
                            ), false);
                            return 1;
                        }))
                )
        );
    }

    private String statusText(boolean has, boolean active) {
        if (!has) return "yok";
        return active ? "ACIK" : "kapali";
    }

    private com.mojang.brigadier.builder.LiteralArgumentBuilder<net.minecraft.server.command.ServerCommandSource> powerToggleCommand(String powerName) {
        return CommandManager.literal(powerName).then(CommandManager.literal("toggle").executes(ctx -> {
            ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();
            PlayerPowers p = PlayerPowers.get(player.getUuid());

            boolean has;
            switch (powerName) {
                case "ironman" -> has = p.hasIronMan;
                case "hulk" -> has = p.hasHulk;
                case "spiderman" -> has = p.hasSpiderman;
                default -> has = false;
            }

            if (!has) {
                ctx.getSource().sendFeedback(() -> Text.literal("Bu gucu henuz kazanmadin!"), false);
                return 0;
            }

            switch (powerName) {
                case "ironman" -> p.ironManActive = !p.ironManActive;
                case "hulk" -> p.hulkActive = !p.hulkActive;
                case "spiderman" -> p.spidermanActive = !p.spidermanActive;
            }

            ctx.getSource().sendFeedback(() -> Text.literal(powerName + " modu degisti."), false);
            return 1;
        }));
    }

    // ---------------------------------------------------------------
    // 3) Aktif guclerin efektleri (yaklasik saniyede bir uygulanir)
    // ---------------------------------------------------------------
    private void registerPowerEffectLoop() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            tickCounter++;
            if (tickCounter < 20) return;
            tickCounter = 0;

            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                PlayerPowers p = PlayerPowers.get(player.getUuid());

                if (p.ironManActive) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 40, 1, true, false));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 40, 0, true, false));
                }
                if (p.hulkActive) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 40, 1, true, false));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 40, 0, true, false));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 40, 2, true, false));
                }
                if (p.spidermanActive) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 40, 2, true, false));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 40, 0, true, false));
                }
                if (p.infected) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 60, 0, true, false));
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 60, 0, true, false));
                }
            }
        });
    }
}
