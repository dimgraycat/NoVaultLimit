package com.skytendo.novaultlimit.mixin;

import net.minecraft.block.vault.VaultConfig;
import net.minecraft.block.vault.VaultServerData;
import net.minecraft.block.vault.VaultSharedData;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Mixin(VaultSharedData.class)
public class VaultSharedDataMixin {

    @Shadow
    private Set<UUID> connectedPlayers;
    @Shadow
    boolean dirty;

    /**
     * updateConnectedPlayers() injection (HEAD)
     *
     * Changes the connected players to also contain
     * already rewarded players
     */
    @Inject(method = "updateConnectedPlayers(" +
            "Lnet/minecraft/server/world/ServerWorld;" +
            "Lnet/minecraft/util/math/BlockPos;" +
            "Lnet/minecraft/block/vault/VaultServerData;" +
            "Lnet/minecraft/block/vault/VaultConfig;" +
            "D)V", at = @At("TAIL"))
    void updateConnectedPlayers(ServerWorld world, BlockPos pos, VaultServerData serverData, VaultConfig config, double radius, CallbackInfo ci) {
        Set<UUID> set = new HashSet<>(config.playerDetector()
                .detect(world, config.entitySelector(), pos, radius, false));
        if (!this.connectedPlayers.equals(set)) {
            this.connectedPlayers = set;
            this.dirty = true;
        }
    }
}
