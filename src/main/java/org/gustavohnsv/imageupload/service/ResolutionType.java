package org.gustavohnsv.imageupload.service;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public enum ResolutionType {

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
