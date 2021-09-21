package esoterum.content;

import arc.Core;
import arc.assets.AssetDescriptor;
import arc.assets.loaders.MusicLoader;
import arc.audio.Music;
import mindustry.Vars;

// no more "musics"
public class EsoMusic {
    protected static Music loadMusic(String musicName) {
        if (!Vars.headless) {
            String name = "music/" + musicName;
            String path = Vars.tree.get(name + ".mp3").exists() ? name + ".mp3" : name + ".ogg";

            Music music = new Music();

            AssetDescriptor<?> desc = Core.assets.load(path, Music.class, new MusicLoader.MusicParameter(music));
            desc.errored = Throwable::printStackTrace;

            return music;
        } else {
            return new Music();
        }
    }
    public static Music
        Eso1;

    public void load() {
        Eso1 = loadMusic("Eso1");
    }

}
