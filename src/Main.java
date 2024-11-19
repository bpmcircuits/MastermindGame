import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {
    public static void main(String[] args) {

        createIntro();

        // Tworzenie obiektu Scanner do wczytywania danych od użytkownika
        try (Scanner userInputObj = new Scanner(System.in)) {

            // Inicjalizacja generatora losowych liczb
            GameCode number = new GameCode();

            // Debug: Wyświetlenie wylosowanej liczby (zakomentowane, aby nie zdradzać wyniku graczowi)
            //System.out.println("Losowa liczba: " + number.returnNumber());

            // Pętla, która pozwala użytkownikowi zgadywać liczby do skutku
            while (true) {
                try {
                    System.out.println("Zgadnij losową liczbę czterocyfrową:");

                    // Wczytanie liczby od użytkownika
                    String userInput = userInputObj.nextLine();

                    // Analiza wprowadzonej liczby
                    boolean wygrana = number.analyzeNumber(userInput);

                    // Sprawdzenie, czy gracz wygrał
                    if (wygrana) {
                        System.out.println("Wygrałeś! Losowa liczba to: " + userInput);
                        break; // Wyjście z pętli po wygranej
                    }
                } catch (InputMismatchException e) {
                    // Obsługa sytuacji, gdy użytkownik wprowadzi niepoprawne dane (np. litery)
                    System.out.println("Wpisano złe znaki, wpisz liczbę czterocyfrową!");
                    userInputObj.next(); // Odrzucenie nieprawidłowego wejścia
                }
            }
        }
    }

    static void createIntro() {

        System.out.println("**********************************************************************************************");
        System.out.println("*                                                                                            *");
        System.out.println("*                                       WELCOME TO                                           *");
        System.out.println("*                                                                                            *");
        System.out.println("  8888b   d8888                   888                                  Y8P               888  ");
        System.out.println("  88888b.d88888                   888                                                    888  ");
        System.out.println("  888Y88888P888  8888b.  .d8888b  888888 .d88b.  888d888 88888b.d88b.  888 88888b.   .d88888  ");
        System.out.println("  888 Y888P 888     '88b 88K      888   d8P  Y8b 888P'   888 '888 '88b 888 888 '88b d88' 888  ");
        System.out.println("  888  Y8P  888 .d888888 'Y8888b. 888   88888888 888     888  888  888 888 888  888 888  888  ");
        System.out.println("  888   '   888 888  888      X88 Y88b. Y8b.     888     888  888  888 888 888  888 Y88b 888  ");
        System.out.println("  888       888 'Y888888  88888P'  'Y888 'Y8888  888     888  888  888 888 888  888  'Y88888  ");
        System.out.println("*                                                                                            *");
        System.out.println("*                                GUESS THE 4-DIGIT NUMBER!                                   *");
        System.out.println("*                                                                                            *");
        System.out.println("**********************************************************************************************");
        System.out.println();

    }
}

class ArrayGen {

    private final int[] digits; // Tablica cyfr

    // Konstruktor klasy, który zapisuje otrzymany ciąg znaków do tablicy
    public ArrayGen(String number) {

        digits = new int[number.length()];

        for (int i = 0; i < number.length(); i++) {
            digits[i] = Character.getNumericValue(number.charAt(i));
        }

    }

    // Konstruktor overload ze zmienną typu StringBuilder
    public ArrayGen(StringBuilder number) { this(number.toString()); }

    // Metoda zwracająca zawartość tablicy digits
    int[] getDigits() { return digits; }
}

class GameCode {

    Random random = new Random(); // Inicjalizacja obiektu Random do generowania liczb losowych
    StringBuilder numberAsString; // Liczba losowa jako ciąg znaków
    int[] computerDigits, playerDigits; // Tablice cyfr dla komputera i gracza

    // Konstruktor klasy, który generuje losową liczbę i zapisuje jej cyfry w tablicy
    public GameCode() {

        numberAsString = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int digit = random.nextInt(10); // Losowa cyfra od 0 do 9
            numberAsString.append(digit);
        }

        // Inicjalizacja obiektu ArrayGen do generowania tablicy z wygenerowanymi liczbami komputera
        ArrayGen digits = new ArrayGen(numberAsString);
        computerDigits = digits.getDigits();
    }

    // Analiza wprowadzonej liczby przez użytkownika
    boolean analyzeNumber(String number) {

        // Listy przechowujące trafienia na odpowiednich i nieodpowiednich pozycjach
        List<Integer> samePosition = new ArrayList<>();
        List<Integer> differentPosition = new ArrayList<>();

        // Inicjalizacja obiektu ArrayGen do generowania tablicy z wygenerowanymi liczbami użytkownika
        ArrayGen digits = new ArrayGen(number);
        playerDigits = digits.getDigits();

        // Sprawdzenie, czy liczba ma dokładnie 4 cyfry
        if (playerDigits.length != 4) {
            System.out.println("Wpisana liczba musi zawierać cztery cyfry!");
            return false; // Zwraca fałsz, jeśli warunek nie jest spełniony
        }

        // Sprawdzenie cyfr na tych samych pozycjach
        for (int i = 0; i < computerDigits.length; i++) {
            if (computerDigits[i] == playerDigits[i]) {
                samePosition.add(computerDigits[i]); // Dodanie trafionej cyfry do listy
                // Debug: Wyświetlenie trafień na właściwych miejscach
                // System.out.println(samePosition);
            }
        }

        // Sprawdzenie cyfr na różnych pozycjach
        for (int i = 0; i < computerDigits.length; i++) {
            for (int j = 0; j < playerDigits.length; j++) {
                if (computerDigits[i] == playerDigits[j] && i != j) {
                    differentPosition.add(computerDigits[i]); // Dodanie trafionej cyfry do listy
                }
            }
        }

        // Wyświetlenie wyników analizy
        System.out.println("Liczba trafionych cyfr na właściwych miejscach: " + samePosition.size());
        System.out.println("Liczba trafionych cyfr, ale na niewłaściwych miejscach: " + differentPosition.size());

        // Warunek zwycięstwa (wszystkie cyfry trafione na właściwych miejscach)
        // Zwraca fałsz w przeciwnym przypadku
        return samePosition.size() == 4; // Zwraca prawdę, jeśli gracz odgadł całą liczbę
    }

    // Zwraca wylosowaną liczbę (pomocne do debugowania)
    StringBuilder returnNumber() {
        return numberAsString;
    }
}
