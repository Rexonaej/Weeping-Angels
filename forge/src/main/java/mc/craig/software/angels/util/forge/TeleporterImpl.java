package mc.craig.software.angels.util.forge;

import com.google.common.collect.Lists;
import mc.craig.software.angels.WAConfiguration;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.RandomSource;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.ArrayList;

public class TeleporterImpl {

    public static ServerLevel getRandomDimension(RandomSource rand, ServerLevel serverLevel) {
        Iterable<ServerLevel> dimensions = ServerLifecycleHooks.getCurrentServer().getAllLevels();
        ArrayList<ServerLevel> allowedDimensions = Lists.newArrayList(dimensions);

        for (ServerLevel dimension : dimensions) {
            for (String dimName : WAConfiguration.CONFIG.bannedDimensions.get()) {
                if (dimension.dimension().location().toString().equalsIgnoreCase(dimName)) {
                    allowedDimensions.remove(dimension);
                }
            }
        }
        return allowedDimensions.get(rand.nextInt(allowedDimensions.size()));
    }

}
