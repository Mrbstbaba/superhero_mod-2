package com.superheromod.item;

import com.superheromod.power.PlayerPowers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class SpidermanEggItem extends Item {
    public SpidermanEggItem(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (!world.isClient && entity instanceof PlayerEntity player) {
            PlayerPowers.get(player.getUuid()).hasSpiderman = true;
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("Orumcek oldurunce %1 ihtimalle dusar").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("Menuden veya /superhero spiderman toggle ile ac/kapat").formatted(Formatting.DARK_GRAY));
    }
}
