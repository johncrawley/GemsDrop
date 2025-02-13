package com.jcrawleydev.gemsdrop.service.game.gem;

import androidx.annotation.NonNull;

import com.jcrawleydev.gemsdrop.R;

public enum GemColor {
    BLUE("B", R.drawable.jewel_blue),
    RED("R", R.drawable.jewel_red),
    GREEN("G", R.drawable.jewel_green),
    YELLOW("Y", R.drawable.jewel_yellow),
    PURPLE("P", R.drawable.jewel_purple),
    GREY("x", R.drawable.jewel_grey),
    WONDER("W", R.drawable.jewel_wonder_1);

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
