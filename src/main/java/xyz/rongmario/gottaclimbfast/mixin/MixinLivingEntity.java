package xyz.rongmario.gottaclimbfast.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xyz.rongmario.gottaclimbfast.GottaClimbFast;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {

    @Redirect(method = "method_26318", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V"))
    private void redirectMove(LivingEntity entity, MovementType type, Vec3d original) {
        if (!entity.isClimbing() || entity.isSneaking()) {
            entity.move(type, original);
        }
        else {
            if (entity.pitch > 0 && entity.forwardSpeed == 0) {
                entity.move(type, new Vec3d(original.x, absolute(entity.pitch / 90.0) * (GottaClimbFast.config.getDescendFactor() / 10) * -1.0D, original.z));
            }
            else if (entity.pitch < 0 && !GottaClimbFast.config.isForwardRequired() || entity.forwardSpeed > 0) {
                entity.move(type, new Vec3d(original.x, absolute(entity.pitch / 90.0) * (GottaClimbFast.config.getAscendFactor() / 10), original.z));
            }
            else {
                entity.move(type, original);
            }
        }
    }

    private double absolute(double d) {
        double abs = Math.abs(d);
        return abs == 0 ? 1 : abs;
    }

}
