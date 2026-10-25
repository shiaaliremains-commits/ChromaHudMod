package my.chromahud.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class ChromahudClient implements ClientModInitializer {

    private float hue = 0f;

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(this::onHudRender);
    }

    private void onHudRender(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();

        // تحريك اللون بسلاسة
        hue = (hue + 0.003f) % 1.0f;

        drawRainbowHotbar(context, width, height);
        drawRainbowXPBar(context, width, height, client.player.experienceProgress);
    }

    // ─────────────────────────────────────────────
    //  رسم الـ Hotbar بألوان قوس قزح
    // ─────────────────────────────────────────────
    private void drawRainbowHotbar(DrawContext context, int width, int height) {
        int hotbarWidth = 182;
        int hotbarHeight = 22;
        int x = (width - hotbarWidth) / 2;
        int y = height - 23;

        // خلفية قوس قزح شفافة
        for (int i = 0; i < hotbarWidth; i++) {
            float h = (hue + (float) i / hotbarWidth * 0.5f) % 1.0f;
            int rgb = hsvToRgb(h, 0.7f, 0.8f);
            int color = 0x50000000 | (rgb & 0x00FFFFFF);
            context.fill(x + i, y, x + i + 1, y + hotbarHeight, color);
        }

        // إطار علوي وسفلي لامع
        for (int i = 0; i < hotbarWidth; i++) {
            float h = (hue + (float) i / hotbarWidth) % 1.0f;
            int rgb = hsvToRgb(h, 1.0f, 1.0f);
            context.fill(x + i, y - 1, x + i + 1, y, rgb);
            context.fill(x + i, y + hotbarHeight, x + i + 1, y + hotbarHeight + 1, rgb);
        }

        // إطار جانبي
        for (int i = 0; i < hotbarHeight; i++) {
            float h = (hue + (float) i / hotbarHeight) % 1.0f;
            int rgb = hsvToRgb(h, 1.0f, 1.0f);
            context.fill(x - 1, y + i, x, y + i + 1, rgb);
            context.fill(x + hotbarWidth, y + i, x + hotbarWidth + 1, y + i + 1, rgb);
        }

        // فواصل بين الـ Slots
        for (int slot = 1; slot < 9; slot++) {
            int slotX = x + slot * 20;
            for (int i = 0; i < hotbarHeight; i++) {
                float h = (hue + 0.5f + (float) i / hotbarHeight) % 1.0f;
                int rgb = hsvToRgb(h, 0.9f, 1.0f);
                context.fill(slotX - 1, y + i, slotX, y + i + 1, rgb);
            }
        }
    }

    // ─────────────────────────────────────────────
    //  رسم الـ XP Bar بألوان قوس قزح
    // ─────────────────────────────────────────────
    private void drawRainbowXPBar(DrawContext context, int width, int height, float progress) {
        int barWidth = 182;
        int barHeight = 5;
        int x = (width - barWidth) / 2;
        int y = height - 36;

        int filledWidth = Math.max(1, (int) (barWidth * progress));

        // تعبئة قوس قزح
        for (int i = 0; i < filledWidth; i++) {
            float h = (hue + 0.3f + (float) i / barWidth * 0.7f) % 1.0f;
            int rgb = hsvToRgb(h, 1.0f, 1.0f);
            context.fill(x + i, y, x + i + 1, y + barHeight, rgb);
        }

        // تأثير التوهج فوق وتحت
        for (int i = 0; i < filledWidth; i++) {
            float h = (hue + 0.3f + (float) i / barWidth * 0.7f) % 1.0f;
            int rgb = hsvToRgb(h, 0.8f, 1.0f);
            int color = 0x60000000 | (rgb & 0x00FFFFFF);
            context.fill(x + i, y - 2, x + i + 1, y, color);
            context.fill(x + i, y + barHeight, x + i + 1, y + barHeight + 2, color);
        }

        // إطار الـ XP Bar
        for (int i = 0; i < barWidth; i++) {
            float h = (hue + (float) i / barWidth) % 1.0f;
            int rgb = hsvToRgb(h, 1.0f, 1.0f);
            context.fill(x + i, y - 1, x + i + 1, y, rgb);
            context.fill(x + i, y + barHeight, x + i + 1, y + barHeight + 1, rgb);
        }
    }

    // ─────────────────────────────────────────────
    //  تحويل HSV إلى RGB
    // ─────────────────────────────────────────────
    private int hsvToRgb(float h, float s, float v) {
        int i = (int) (h * 6);
        float f = h * 6 - i;
        float p = v * (1 - s);
        float q = v * (1 - f * s);
        float t = v * (1 - (1 - f) * s);

        float r, g, b;
        switch (i % 6) {
            case 0: r = v; g = t; b = p; break;
            case 1: r = q; g = v; b = p; break;
            case 2: r = p; g = v; b = t; break;
            case 3: r = p; g = q; b = v; break;
            case 4: r = t; g = p; b = v; break;
            case 5: r = v; g = p; b = q; break;
            default: r = v; g = t; b = p; break;
        }

        return ((int)(r * 255) << 16) | ((int)(g * 255) << 8) | (int)(b * 255);
    }
}
