package net.grexcraft.cloud.signs.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignData implements ConfigurationSerializable {
    Location location;
    String slot;
    String title;
    boolean clicked = false;

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialized = new HashMap<>();
        serialized.put("location", location);
        serialized.put("slot", slot);
        serialized.put("title", title);
        return serialized;
    }

    public static SignData deserialize(Map<String, Object> deserialize) {
        return new SignData(
                (Location) deserialize.get("location"),
                deserialize.get("slot").toString(),
                deserialize.get("title").toString(),
                false
        );
    }
}
