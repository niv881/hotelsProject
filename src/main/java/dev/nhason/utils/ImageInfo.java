package dev.nhason.utils;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageInfo {
    Long id;
    byte[] image;
}
