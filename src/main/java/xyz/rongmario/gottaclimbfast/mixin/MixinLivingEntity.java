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

    /**
     * @author Rongmario
     */
    /*
    @Overwrite
    public Vec3d method_26318(Vec3d vec3d, float f) {
        LivingEntity entity = (LivingEntity) (Object) this;
        entity.updateVelocity(this.getMovementSpeed(f), vec3d);
        entity.setVelocity(this.applyClimbingSpeed(entity.getVelocity()));
        if (entity.isClimbing()) {
            return getElevationChangeUpdate(entity);
        }
        entity.move(MovementType.PLAYER, entity.getVelocity());
        return entity.getVelocity();
    }

    private Vec3d getElevationChangeUpdate(LivingEntity entity) {
        double base = Math.abs(entity.pitch / 90.0) * (8.0F / 10);
        Vec3d vec3d = entity.getVelocity();
        if ((entity.horizontalCollision || jumping)) {
            vec3d = new Vec3d(vec3d.x, base, vec3d.z);
            entity.move(MovementType.PLAYER, vec3d);
        }
        else if (entity.pitch > 0) {
            vec3d = new Vec3d(vec3d.x, -base, vec3d.z);
            entity.move(MovementType.PLAYER, vec3d);
        }
        return vec3d;
    }
    */

    /**
     * @author Rongmario
     */
    /*
    @Overwrite
    public Vec3d method_26318(Vec3d vec3d, float f) {
        LivingEntity entity = (LivingEntity) (Object) this;
        entity.updateVelocity(this.getMovementSpeed(f), vec3d);
        entity.setVelocity(this.applyClimbingSpeed(entity.getVelocity()));
        entity.move(MovementType.SELF, entity.getVelocity());
        Vec3d vec3d2 = entity.getVelocity();
        if (entity.isClimbing()) {
            vec3d2 = new Vec3d(vec3d2.x, getElevationChangeUpdate(entity), vec3d2.z);
        }
        return vec3d2;
    }

    private double getElevationChangeUpdate(LivingEntity entity) {
        double base = Math.abs(entity.pitch / 90.0) * (8.0F / 10);
        if ((entity.horizontalCollision || jumping)) {
            return base;
        }
        else if (entity.pitch > 0) {
            entity.move(MovementType.PLAYER, new Vec3d(0, -base, 0));
        }
        return 0.2D;
    }
     */

}
