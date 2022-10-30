package xyz.rongmario.gottaclimbfast;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GottaClimbFast implements ModInitializer {

    public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();

    public static Configuration config;

    @Override
    public void onInitialize() {
        prepareAndReadConfig();
    }

    protected static void prepareAndReadConfig() {
        File file = new File(FabricLoader.getInstance().getConfigDirectory(), "gotta_climb_fast.json"); // Fuck your deprecation
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            config = GSON.fromJson(reader, Configuration.class);
        }
        catch (FileNotFoundException e1) {
            String jsonString = GSON.toJson(new Configuration());
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(jsonString);
                fileWriter.close();
                BufferedReader reader = new BufferedReader(new FileReader(file));
                config = GSON.fromJson(reader, Configuration.class);
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    public static class Configuration {

        private final String factorComment = "Speed factor of ladder ascending/descending. Default: 4.0F";
        private float ascendFactor = 4.0F;
        private float descendFactor = 4.0F;

        private final String forwardToAscendComment = "True = Lookup and go forward to ascend. False = Lookup and no need to go forward to ascend. Default: True";
        private boolean forwardToAscend = true;

        public float getAscendFactor() {
            return ascendFactor;
        }

        public float getDescendFactor() {
            return descendFactor;
        }

        public boolean isForwardRequired() {
            return forwardToAscend;
        }

    }

}
