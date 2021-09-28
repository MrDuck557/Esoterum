package esoterum.content;

import arc.*;
import arc.assets.*;
import arc.assets.loaders.SoundLoader.*;
import arc.audio.*;
import mindustry.*;

public class EsoSounds{
    protected static Sound loadSound(String soundName){
        String name = "sounds/" + soundName;
        String path = Vars.tree.get(name + ".ogg").exists() ? name + ".ogg" : name + ".mp3";

        Sound sound = new Sound();

        AssetDescriptor<?> desc = Core.assets.load(path, Sound.class, new SoundParameter(sound));
        desc.errored = Throwable::printStackTrace;

        return sound;
    }

    public static Sound[] loadNotes(String soundName, int minOct, int maxOct){
        int len = maxOct + 1 - minOct;
        Sound[] out = new Sound[len];
        for(int i = 0; i < len; i++){
            out[i] = loadSound(soundName + (minOct + i));
        }
        return out;
    }

    public static Sound[] loadNotes(String soundName){
        return loadNotes(soundName, 2, 6);
    }

    public static Sound[]
        bells;

    public static void load(){
        bells = loadNotes("bells/bellC");
    }
}
