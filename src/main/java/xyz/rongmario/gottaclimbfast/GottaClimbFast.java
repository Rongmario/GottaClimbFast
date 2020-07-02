package xyz.rongmario.gottaclimbfast;

import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;

@Mod("gottaclimbfast")
public class GottaClimbFast {

    public static final Configuration config = new Configuration();

    private static final CommonConfig COMMON_CONFIG;
    private static final ForgeConfigSpec COMMON_SPEC;

    static {
        final Pair<CommonConfig, ForgeConfigSpec> COMMON_PAIR = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        COMMON_CONFIG = COMMON_PAIR.getLeft();
        COMMON_SPEC = COMMON_PAIR.getRight();
    }

    public GottaClimbFast() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_SPEC, "gottaclimbfast.toml");
        FMLJavaModLoadingContext.get().getModEventBus().addListener(Configuration::onModConfigEvent);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerTick);
    }

    private void onPlayerTick(LivingEvent.LivingUpdateEvent event) {
        if (!(event.getEntity() instanceof PlayerEntity)) {
            return;
        }
        PlayerEntity player = (PlayerEntity) event.getEntity();
        if (player.isSneaking() || !player.isOnLadder()) {
            return;
        }
        if (player.rotationPitch > 0 && player.moveForward == 0) {
            player.move(MoverType.PLAYER, new Vector3d(0, absolute(player.rotationPitch / 90.0) * (GottaClimbFast.config.getDescendFactor() / 20) * -1.0D, 0));
        }
        else if (player.rotationPitch < 0 && !GottaClimbFast.config.isForwardRequired() || player.moveForward > 0) {
            player.move(MoverType.PLAYER, new Vector3d(0, absolute(player.rotationPitch / 90.0) * (GottaClimbFast.config.getAscendFactor() / 20), 0));
        }
    }

    private double absolute(double d) {
        double abs = Math.abs(d);
        return abs == 0 ? 1 : abs;
    }

    public static class Configuration {

        private static double ascendFactor;
        private static double descendFactor;

        private static boolean forwardToAscend;

        private static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent) {
            ascendFactor = COMMON_CONFIG.ASCEND_FACTOR.get();
            descendFactor = COMMON_CONFIG.ASCEND_FACTOR.get();
            forwardToAscend = COMMON_CONFIG.FORWARD_TO_ASCEND.get();
        }

        public double getAscendFactor() {
            return ascendFactor;
        }

        public double getDescendFactor() {
            return descendFactor;
        }

        public boolean isForwardRequired() {
            return forwardToAscend;
        }

    }

    public static class CommonConfig {

        public final ForgeConfigSpec.DoubleValue ASCEND_FACTOR, DESCEND_FACTOR;
        public final ForgeConfigSpec.BooleanValue FORWARD_TO_ASCEND;

        public CommonConfig(ForgeConfigSpec.Builder builder) {
            builder.push("Speeds");
            ASCEND_FACTOR = builder.comment("Speed factor of ladder ascending. Default: 4.0D")
                    .translation("gottaclimbfast.config.ascend_factor")
                    .defineInRange("ASCEND_FACTOR", 4.0D, 0.0D, Double.MAX_VALUE);
            DESCEND_FACTOR = builder.comment("Speed factor of ladder descending. Default: 4.0D")
                    .translation("gottaclimbfast.config.descend_factor")
                    .defineInRange("DESCEND_FACTOR", 4.0D, 0.0D, Double.MAX_VALUE);
            builder.pop();
            builder.push("Behaviours");
            FORWARD_TO_ASCEND = builder.comment("True = Lookup and go forward to ascend. False = Lookup and no need to go forward to ascend. Default: True")
                    .translation("gottaclimbfast.config.forward_to_ascend")
                    .define("FORWARD_TO_ASCEND", true);
            builder.pop();
        }

    }

}
