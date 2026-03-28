package com.skytendo.novaultlimit.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.vault.VaultConfig;
import net.minecraft.world.level.block.entity.vault.VaultServerData;
import net.minecraft.world.level.block.entity.vault.VaultSharedData;
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
    boolean isDirty;

    /**
     * updateConnectedPlayers() injection (HEAD)
     *
     * Changes the connected players to also contain
     * already rewarded players
     */
    @Inject(method = "updateConnectedPlayersWithinRange(" +
            "Lnet/minecraft/server/level/ServerLevel;" +
            "Lnet/minecraft/core/BlockPos;" +
            "Lnet/minecraft/world/level/block/entity/vault/VaultServerData;" +
            "Lnet/minecraft/world/level/block/entity/vault/VaultConfig;" +
            "D)V", at = @At("TAIL"))
    void updateConnectedPlayersWithinRange(ServerLevel world, BlockPos pos, VaultServerData serverData, VaultConfig config, double radius, CallbackInfo ci) {
        Set<UUID> set = new HashSet<>(config.playerDetector()
                .detect(world, config.entitySelector(), pos, radius, false));
        if (!this.connectedPlayers.equals(set)) {
            this.connectedPlayers = set;
            this.isDirty = true;
        }
    }
}
