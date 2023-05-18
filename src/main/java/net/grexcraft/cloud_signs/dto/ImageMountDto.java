package net.grexcraft.cloud_signs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageMountDto {
    Long id;
    String pathLocal;
    String pathContainer;
    ImageDto image;

}
