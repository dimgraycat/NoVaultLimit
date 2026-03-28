package com.skytendo.novaultlimit.mixin;

import net.minecraft.world.level.block.entity.vault.VaultServerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VaultServerData.class)
public abstract class VaultServerDataMixin {

    /**
     * hasRewardedPlayer() injection (HEAD)
     *
     * Makes sure the server NEVER thinks of a player as "already rewarded"
     * This injection basically removes the entire only-open-once-per-player-mechanic, nearly everything else is for the visible representation
     */
    @Inject(method = "hasRewardedPlayer(Lnet/minecraft/world/entity/player/Player;)Z", at = @At("HEAD"), cancellable = true)
    private void hasRewardedPlayer(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
