import java.util.Scanner;
import java.util.Random;

class Number {
    int n, n1, guess = 0;

    public Number() {
        Random random = new Random();
        System.out.println("Computer chooses its number");
        System.out.println("You have to predict the correct number");
        System.out.println("You will get 5 chances to guess the correct number....Try to guess it within 5 chances");
        System.out.print("Choose any number between 1-100\n");
        n = random.nextInt(101);
    }

    public void takeUserInput() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Your turn");
        
        n1 = sc.nextInt();

        if (n1 < 0 || n1 > 100) {
            System.out.println("\tNot a Valid Number!\n\tTry Again.\n");
            return;
        }

        if (n > n1) {
            System.out.println("You have chosen a smaller number");
        } else if (n < n1) {
            System.out.println("You have chosen a greater number");
        } else {
            System.out.println("Congratulations! You guessed the correct answer");
            return;
        }

        guess++;
        if (guess == 5) {
            System.out.println("Sorry, you've run out of attempts");
            System.out.println("The corect number was: "+n);
        }
    }

    public void setGuessNumber(int g) {
        guess = g;
    }

    public int getGuessNumber() {
        return guess;
    }

    public void isCorrectValue() {
        if (n == n1) {
            System.out.println("Congratulations! You guessed the correct number ");
        } else {
            System.out.println("Sorry! You couldn't guess the correct number");
        }
    }
}

public class Guess_the_Number {
    public static void main(String[] args) {
        Number game = new Number();
        for (int i = 0; i < 5; i++) {
            game.takeUserInput();
            if (game.getGuessNumber() == 5) {
                break;
            }
        }

        System.out.println("Number of guesses - " + game.getGuessNumber());
    }
}
