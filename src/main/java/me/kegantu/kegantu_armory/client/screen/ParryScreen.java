package me.kegantu.kegantu_armory.client.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ParryScreen extends Screen {

    public ParryScreen() {
        super(Text.literal(""));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, getColorWithAlpha(0.5f, 16777215));
        super.render(context, mouseX, mouseY, delta);
    }

    public static int getColorWithAlpha(float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }
}
