package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, int seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        this.rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        StringBuilder sb = new StringBuilder();
        for (int index = 0; index < n; index++) {
            sb.append(CHARACTERS[rand.nextInt(26)]);
        }
        return sb.toString();
    }

    public void drawFrame(String s) {
        StdDraw.clear(Color.black);
        StdDraw.setPenColor(Color.WHITE);
        if (!gameOver) {
            StdDraw.setFont(new Font("Monaco", Font.PLAIN, 20));
            StdDraw.text(3.5, 39, "Round: " + round);
            if (!playerTurn) {
                StdDraw.text(20, 39, "Watch!");
            } else {
                StdDraw.text(20, 39, "Type!");
            }
            String encouragement = ENCOURAGEMENT[rand.nextInt(7)];
            StdDraw.text(33, 39, encouragement);
            StdDraw.line(0, 38, 40, 38);
        }
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 30));
        StdDraw.text(width / 2, height / 2, s);
        StdDraw.show();
    }

    public void flashSequence(String letters) {
//        String[] lettersArray = letters.split("");
//        for (String letter: lettersArray) {
//            StdDraw.pause(750);
//            drawFrame(letter);
//            StdDraw.pause(750);
//            StdDraw.clear(Color.black);
//            StdDraw.show();
//        }
        for (int index = 0; index < letters.length(); index++) {
            StdDraw.pause(750);
            drawFrame(letters.substring(index, index + 1));
            StdDraw.pause(750);
            drawFrame("");
        }
    }

    public String solicitNCharsInput(int n) {
        String input = "";
        while (n > 0) {
            if (StdDraw.hasNextKeyTyped()) {
                char character = StdDraw.nextKeyTyped();
                input += character;
                drawFrame(input);
                n--;
            }
        }
        StdDraw.pause(750);
        return input;
    }

    public void startGame() {
        this.round = 1;
        gameOver = false;
        while (!gameOver) {
            playerTurn = false;
            String roundNumber = "Round: " + round;
            drawFrame(roundNumber);
            String randomString = generateRandomString(round);
            flashSequence(randomString);
            playerTurn = true;
            String userInput = solicitNCharsInput(round);
            if (userInput.equals(randomString)) {
                round++;
                continue;
            }
            gameOver = true;
        }
        drawFrame("Game Over! You made it to round: " + round);
    }

}
