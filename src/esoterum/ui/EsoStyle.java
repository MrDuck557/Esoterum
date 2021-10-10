package esoterum.ui;

import arc.scene.ui.TextButton;
import mindustry.ui.Fonts;

import static arc.Core.atlas;

public class EsoStyle {
    public static TextButton.TextButtonStyle pianoBlack, pianoWhite;

    public static void init(){
        // ninepatches by @sk7725
        pianoBlack = new TextButton.TextButtonStyle(
            atlas.drawable("esoterum-keyb"),
            atlas.drawable("esoterum-keyb-down"),
            atlas.drawable("esoterum-keyb-checked"),
            Fonts.def
        );
        pianoWhite = new TextButton.TextButtonStyle(
            atlas.drawable("esoterum-keyw"),
            atlas.drawable("esoterum-keyw-down"),
            atlas.drawable("esoterum-keyw-checked"),
            Fonts.outline
        );
    }
}
