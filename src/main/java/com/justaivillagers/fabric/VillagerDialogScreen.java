package com.justaivillagers.fabric;

import java.util.ArrayList;
import java.util.List;

import com.justaivillagers.core.Lang;
import com.justaivillagers.core.LangData;
import com.justaivillagers.core.LangPack;
import com.justaivillagers.core.VillagerProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

/**
 * The dialog screen.
 *
 * <p>It can address one villager (looking straight at them) or a whole group of nearby villagers
 * (pressing V while looking at nothing), in which case the same message is sent to everyone in the
 * group. Enter sends and closes the screen so the player is back in the world immediately; the
 * answers still arrive in chat.
 */
public final class VillagerDialogScreen extends Screen {

    private static final int MAX_INPUT = 200;

    private final List<VillagerEntity> villagers;
    private final List<VillagerProfile> profiles;
    private final List<String> transcript = new ArrayList<>();
    private TextFieldWidget inputField;
    private int pending;

    public VillagerDialogScreen(List<VillagerEntity> villagers, List<VillagerProfile> profiles) {
        super(Text.literal(profiles.isEmpty() ? "" : profiles.get(0).name));
        this.villagers = villagers;
        this.profiles = profiles;
    }

    @Override
    protected void init() {
        int fieldWidth = Math.min(320, this.width - 40);
        int x = (this.width - fieldWidth) / 2;
        int y = this.height - 52;

        this.inputField = new TextFieldWidget(this.textRenderer, x, y, fieldWidth - 60, 20,
                Text.literal(pack().t("screen.message")));
        this.inputField.setMaxLength(MAX_INPUT);
        addDrawableChild(this.inputField);

        // setFocused alone does not give the field the keyboard: the screen tracks its own initial
        // focus and hands it out after init(), so without this the player has to click first.
        this.setInitialFocus(this.inputField);

        addDrawableChild(ButtonWidget.builder(Text.literal(pack().t("screen.send")), b -> sendAndClose())
                .dimensions(x + fieldWidth - 56, y, 56, 20)
                .build());
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (input.key() == GLFW.GLFW_KEY_ENTER || input.key() == GLFW.GLFW_KEY_KP_ENTER) {
            sendAndClose();
            return true;
        }
        return super.keyPressed(input);
    }

    private void sendAndClose() {
        send();
        // Closing here is what the player expects from Enter: the message is already on its way and
        // the answers are delivered to chat, so keeping the screen open only steals control.
        close();
    }

    private void send() {
        if (inputField == null) {
            return;
        }
        String message = inputField.getText().trim();
        if (message.isEmpty() || villagers.isEmpty()) {
            return;
        }
        inputField.setText("");

        MinecraftClient client = MinecraftClient.getInstance();
        ModRuntime runtime = ModRuntime.get();
        String language = GameBridge.language(runtime.config().languageOverride);

        pending = villagers.size();
        for (int i = 0; i < villagers.size(); i++) {
            VillagerEntity villager = villagers.get(i);
            VillagerProfile profile = profiles.get(i);
            addLine(pack().t("screen.you") + message);

            var ctx = VillagerContextReader.read(villager);
            boolean sleeping = villager.isSleeping();
            String target = profile.name;
            runtime.service().askReply(villager.getUuid(), villager.isBaby(), sleeping, language, profile, ctx,
                    message,
                    text -> {
                        pending--;
                        addLine(target + ": " + text);
                        say(target, text);
                    },
                    (kind, error) -> {
                        pending--;
                        addLine("§8[" + target + ": " + error + "]");
                    });
        }
        if (client.currentScreen == null) {
            client.setScreen(this);
        }
    }

    private void addLine(String line) {
        transcript.add(line);
        while (transcript.size() > 14) {
            transcript.remove(0);
        }
    }

    private void say(String name, String text) {
        // No extra pause here: the dialog transcript already shows the line, and the chat copy should
        // not lag behind it. Pacing belongs to unprompted speech.
        MinecraftClient client = MinecraftClient.getInstance();
        client.execute(() -> {
            if (client.player != null) {
                client.player.sendMessage(Text.literal("§e[" + name + "] §f" + text), false);
            }
        });
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        Text heading = villagers.size() == 1
                ? this.title
                : Text.literal(pack().t("screen.nearby"));
        context.drawCenteredTextWithShadow(this.textRenderer, heading, this.width / 2, 16, 0xFFFFFF);

        String subtitle = villagers.size() == 1
                ? singleSubtitle()
                : String.join(", ", profiles.stream().map(p -> p.name).toList());
        context.drawCenteredTextWithShadow(this.textRenderer, Text.literal(subtitle).formatted(Formatting.GRAY),
                this.width / 2, 30, 0xAAAAAA);

        int y = 56;
        for (String line : transcript) {
            context.drawTextWithShadow(this.textRenderer, Text.literal(trim(line)), 20, y, 0xFFFFFF);
            y += 12;
        }
        if (pending > 0) {
            context.drawTextWithShadow(this.textRenderer,
                    Text.literal(pack().t("screen.thinking")).formatted(Formatting.DARK_GRAY),
                    20, this.height - 66, 0x808080);
        }
    }

    private String singleSubtitle() {
        VillagerProfile profile = profiles.get(0);
        return profile.child
                ? pack().profession("child")
                : profile.personality + ", " + pack().style(profile.styleKey());
    }

    private static String trim(String line) {
        return line.length() > 160 ? line.substring(0, 157) + "..." : line;
    }

    private static LangPack pack() {
        return LangData.of(Lang.normalise(GameBridge.language(ModRuntime.get().config().languageOverride)));
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
