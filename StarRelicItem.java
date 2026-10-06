package com.estrellamagica.fugaz.item;

import com.estrellamagica.fugaz.EstrellaMagicaFugaz;
import com.estrellamagica.fugaz.StarColor;
import com.estrellamagica.fugaz.event.StarFx;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/** Objeto unico especial: clic derecho = poder segun el color de la estrella. Cooldown 3 min. */
public class StarRelicItem extends Item {
    private final StarColor color;

    public StarRelicItem(Properties props, StarColor color) {
        super(props);
        this.color = color;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && level instanceof ServerLevel sl) {
            switch (color) {
                case BLUE -> {
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 90, 1));
                    player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 20 * 90, 0));
                }
                case PINK -> {
                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 20, 2));
                    player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 20 * 120, 2));
                }
                case GREEN -> {
                    player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 20 * 300, 0));
                    player.addEffect(new MobEffectInstance(MobEffects.LUCK, 20 * 180, 2));
                }
                case YELLOW -> {
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 60, 1));
                    player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 20 * 60, 1));
                }
            }
            sl.playSound(null, player.blockPosition(), SoundEvents.AMETHYST_BLOCK_CHIME,
                    SoundSource.PLAYERS, 1.5f, 1.2f);
            StarFx.burst(sl, player.position().add(0, 1.0, 0), color, 30, 1.6);
        }
        player.getCooldowns().addCooldown(this, 20 * 180);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item." + EstrellaMagicaFugaz.MOD_ID + "." + color.relicId + ".tooltip"));
    }
}
