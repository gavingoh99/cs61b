package lab14;

import lab14lib.Generator;
import lab14lib.GeneratorAudioVisualizer;

public class Main {
	public static void main(String[] args) {
		Generator generator = new StrangeBitwiseGenerator(200);
		GeneratorAudioVisualizer gp = new GeneratorAudioVisualizer(generator);
		gp.drawAndPlay(4096, 1000000);
	}
} 