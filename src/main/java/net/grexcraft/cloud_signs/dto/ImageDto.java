package net.grexcraft.cloud_signs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDto {
    Long id;
    String name;
    String tag;
//    PoolDto defaultPool;
//    Set<ImageMountDto> mounts;
}
