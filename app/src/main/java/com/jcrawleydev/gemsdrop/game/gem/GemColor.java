package com.jcrawleydev.gemsdrop.game.gem;

import androidx.annotation.NonNull;

import com.jcrawleydev.gemsdrop.R;

public enum GemColor {
    BLUE("B", R.drawable.jewel_blue),
    RED("R", R.drawable.jewel_red),
    GREEN("G", R.drawable.jewel_green),
    YELLOW("Y", R.drawable.jewel_yellow),
    PURPLE("P", R.drawable.jewel_purple),
    DEEP_BLUE("N", R.drawable.jewel_deep_blue),
    GREY("x", R.drawable.jewel_grey),
    WONDER("W", R.drawable.jewel_wonder_1),
    LIGHT_PINK("P", R.drawable.jewel_wonder_3),
    DARK_RED("D", R.drawable.jewel_temp),
    ORANGE("D", R.drawable.jewel_orange),
    NULL("_", R.drawable.jewel_grey),
    EMPTY("=", R.drawable.jewel_empty);

    GemColor(String str, int resourceId) {
        this.str = str;
        this.resourceId = resourceId;
    }

    public final String str;
    public final int resourceId;

    @NonNull
    public String toString() {
        return str;
    }
}
