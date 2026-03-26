package com.skytendo.novaultlimit.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.VaultBlockEntity;
import net.minecraft.block.vault.VaultConfig;
import net.minecraft.block.vault.VaultServerData;
import net.minecraft.block.vault.VaultSharedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(VaultBlockEntity.Server.class)
public abstract class VaultBlockEntityServerMixin {

    /**
     * tryUnlock() injector (HEAD)
     *
     * Overwrites the vanilla tryUnlock implementation to remove any checks regarding the open-once-per-player logic.
     */
    @Inject(method = "tryUnlock(" +
            "Lnet/minecraft/server/world/ServerWorld;" +
            "Lnet/minecraft/util/math/BlockPos;" +
            "Lnet/minecraft/block/BlockState;" +
            "Lnet/minecraft/block/vault/VaultConfig;" +
            "Lnet/minecraft/block/vault/VaultServerData;" +
            "Lnet/minecraft/block/vault/VaultSharedData;" +
            "Lnet/minecraft/entity/player/PlayerEntity;" +
            "Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
    private static void tryUnlock(ServerWorld world, BlockPos pos, BlockState state, VaultConfig config, VaultServerData serverData, VaultSharedData sharedData, PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        if (!VaultBlockEntityServerInvoker.isValidKey(config, stack)) {
            VaultBlockEntityServerInvoker.playFailedUnlockSound(world, serverData, pos, SoundEvents.BLOCK_VAULT_INSERT_ITEM_FAIL);
        } else {
            List<ItemStack> list = VaultBlockEntityServerInvoker.invokeGenerateLoot(world, config, pos, player, stack);
            if (!list.isEmpty()) {
                player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                stack.decrementUnlessCreative(1, player);
                VaultBlockEntityServerInvoker.invokeUnlock(world, state, pos, config, serverData, sharedData, list);
                ci.cancel();
            }
        }
    }
}
