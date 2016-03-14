package audio;

import java.io.IOException;

public class Test {

    public static void main(String args[]) throws IOException, InterruptedException {
        AudioMaster.init();
        AudioMaster.setListenerData(0,0,0);

        int buffer = AudioMaster.loadSound("audio/music.wav");
        AudioSource source = new AudioSource();
        source.setLooping(true);
        source.play(buffer);

        float xPos = 8;
        source.setPosition(xPos, 2, 0);

        char c = ' ';
        while(c != 'q') {
            xPos -= 0.03f;
            source.setPosition(xPos, 2, 0);
            Thread.sleep(10);
        }

        source.delete();
        AudioMaster.cleanUp();

    }

}
