package audio;

import org.lwjgl.openal.AL10;

public class AudioSource {
    private int sourceId;

    public AudioSource() {
        sourceId = AL10.alGenSources();
        AL10.alSourcef(sourceId, AL10.AL_GAIN, 1);
        AL10.alSourcef(sourceId, AL10.AL_PITCH, 1.1f);
        AL10.alSource3f(sourceId, AL10.AL_POSITION, 0, 0, 0);
    }

   public void play(int buffer) {
        AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer);
        AL10.alSourcePlay(sourceId);
    }

    public void delete() {
        AL10.alDeleteSources(sourceId);
    }
}
