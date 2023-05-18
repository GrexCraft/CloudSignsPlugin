package net.grexcraft.cloud_signs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.grexcraft.cloud_signs.enums.ServerState;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerDto {
    private Long id;
    private ImageDto image;
    private String name;
    private String address;
    private ServerState state;
}
