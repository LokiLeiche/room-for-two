package sircow.roomfortwo.mixin;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Shadow private AABB bb;

    @Inject(method = "getBoundingBox", at = @At("HEAD"), cancellable = true)
    private void roomfortwo$getExpandedSleepingBox(CallbackInfoReturnable<AABB> cir) {
        if ((Object) this instanceof Villager villager) {
            if (!villager.isSleeping()) return;

            AABB modifiedBox = this.bb.inflate(0.175D, 0.1D, 0.175D);

            Direction sleepingDirection = villager.getBedOrientation();
            if (sleepingDirection == null) return;

            cir.setReturnValue(modifiedBox.move(sleepingDirection.getStepX() * 0.2D, 0.0D, sleepingDirection.getStepZ() * 0.2D));
        }
    }
}
