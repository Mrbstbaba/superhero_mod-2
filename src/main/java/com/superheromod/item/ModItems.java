package com.superheromod.item;

import com.superheromod.SuperheroMod;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item IRON_MAN_COSTUME = register("iron_man_costume",
            new IronManItem(new Item.Settings().maxCount(1)));

    public static final Item HULK_ITEM = register("hulk_power",
            new HulkItem(new Item.Settings().maxCount(1)));

    public static final Item SPIDERMAN_EGG = register("spiderman_egg",
            new SpidermanEggItem(new Item.Settings().maxCount(1)));

    public static final Item SPIDER_INFECTION = register("spider_infection",
            new SpiderInfectionItem(new Item.Settings().maxCount(1)));

    private static Item register(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(SuperheroMod.MOD_ID, name), item);
    }

    public static void register() {
        // Yukaridaki static alanlarin calismasini tetikler (kayit islemi icin).
        // Ek olarak esyalari yaratici moddaki envanter sekmesine ekleyelim.
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(new ItemStack(IRON_MAN_COSTUME));
            entries.add(new ItemStack(HULK_ITEM));
            entries.add(new ItemStack(SPIDERMAN_EGG));
            entries.add(new ItemStack(SPIDER_INFECTION));
        });
    }
}
