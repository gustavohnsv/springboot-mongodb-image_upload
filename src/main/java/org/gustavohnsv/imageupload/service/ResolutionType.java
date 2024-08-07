package org.gustavohnsv.imageupload.service;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public enum ResolutionType {

    /*
        960    x 540        (qHD)
        1280   x 720        (HD)
        1920   x 1080       (FHD/Full HD)
        2560   x 1440       (QHD/2K)
        3840   x 2160       (UHD/4K)
        7680   x 4320       (FUHD/8K)
     */

    qHD(540),
    HD(720),
    FHD(1080),
    QHD(1440),
    UHD(2160),
    FUHD(4320);

    private final int resolution;

    ResolutionType(int resolution) {
        this.resolution = resolution;
    }

    @NotNull
    public static String getResolutionType(int resolution) {
        for (ResolutionType resolutionType : ResolutionType.values()) {
            if (resolution <= resolutionType.getResolution()) {
                return resolutionType.name();
            }
        }
        return "FUHD+";
    }

}
