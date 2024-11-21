import java.util.*;

public class Main {

    public static void main(String[] args) {

        createIntro();

        // TESTs
//        testingNumbersArray(new int[]{2, 3, 4, 5}, "2345");
//        testingNumbersArray(new int[]{2, 3, 4, 5}, "5432");
//        testingNumbersArray(new int[]{2, 3, 4, 5}, "2222");
//        testingNumbersArray(new int[]{2, 3, 4, 5}, "2232");
//        testingNumbersArray(new int[]{2, 3, 4, 5}, "5545");

        // Tworzenie obiektu Scanner do wczytywania danych od użytkownika
        try (Scanner userInputObj = new Scanner(System.in)) {

            // Inicjalizacja generatora losowych liczb
            GameCode number = new GameCode();

            // Debug: Wyświetlenie wylosowanej liczby (zakomentowane, aby nie zdradzać wyniku graczowi)
            //System.out.println("Losowa liczba: " + Arrays.toString(number.returnNumber()));

            // Pętla, która pozwala użytkownikowi zgadywać liczby do skutku
            while (true) {

                System.out.println("Zgadnij losową liczbę czterocyfrową:");

                // Wczytanie liczby od użytkownika
                String userInput = userInputObj.nextLine();

                // Analiza wprowadzonej liczby
                boolean wygrana = number.tryNumber(userInput);

                // Sprawdzenie, czy gracz wygrał
                if (wygrana) {
                    System.out.println("Wygrałeś! Losowa liczba to: " + userInput);
                    break; // Wyjście z pętli po wygranej
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

    private static void testingNumbersArray(int[] secret, String userInput) {
        System.out.println("Secret: " + Arrays.toString(secret));
        System.out.println("User input: " + userInput);
        var mm = new GameCode(secret);
        var result = mm.tryNumber(userInput);
    }
}

class GameCode {

    final static int NO_DIGITS = 4;

    Random random = new Random(); // Inicjalizacja obiektu Random do generowania liczb losowych
    int[] secretNumber = new int[NO_DIGITS];
    HashMap<Integer, Integer>secretWithPositions = new HashMap<>();
    int[] playerDigits = new int[NO_DIGITS]; // Tablice cyfr dla komputera i gracza

    // Konstruktor klasy, który generuje losową liczbę z czterech unikalnych cyfr i zapisuje jej cyfry w tablicy
    public GameCode() {

        List<Integer> availableDigits = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            availableDigits.add(i);
        }
        for (int i = 0; i < NO_DIGITS; i++) {
            int index = random.nextInt(availableDigits.size());
            int digit = availableDigits.remove(index);
            secretNumber[i] = digit;
            secretWithPositions.put(digit, i);
        }
    }

    public GameCode(int[] secret) {
        secretNumber = secret;
        for (int i = 0; i < NO_DIGITS; i++) {
            var digit = secretNumber[i];
            secretWithPositions.put(digit, i);
        }
    }

    // Sprawdzenie wprowadzonej liczby przez użytkownika
    boolean tryNumber(String number) {

        try {
            // Sprawdzenie, czy liczba ma dokładnie 4 cyfry
            if (playerDigits.length != NO_DIGITS || !number.matches("\\d{" + NO_DIGITS + "}")) {
                throw new InputMismatchException();
            }

            Set<Integer> exactMatchesState = new HashSet<>();
            Set<Integer> matchesState = new HashSet<>();

            for (int i = 0; i < NO_DIGITS; i++) {
                var digit = Character.getNumericValue(number.charAt(i));

                if (secretWithPositions.containsKey(digit) && secretWithPositions.get(digit).equals(i)) {
                    exactMatchesState.add(digit);
                }
            }

            for (var i = 0; i < NO_DIGITS; i++) {
                var digit = Character.getNumericValue(number.charAt(i));

                if (exactMatchesState.contains(digit)) {
                    continue;
                }

                if (secretWithPositions.containsKey(digit)) {
                    matchesState.add(digit);
                }
            }

            // Wyświetlenie wyników analizy
            System.out.println("Liczba trafionych cyfr na właściwych miejscach: " + exactMatchesState.size());
            System.out.println("Liczba trafionych cyfr, ale na niewłaściwych miejscach: " + matchesState.size());
            System.out.println();

            return exactMatchesState.size() == NO_DIGITS; // Zwraca prawdę, jeśli gracz odgadł całą liczbę

        } catch (InputMismatchException e) {
            System.out.println("Wpisana liczba musi zawierać cztery cyfry!");
            return false;
        } catch (Exception e) {
            System.out.printf("Error: %s.%n", e);
            return false;
        }

    }

    // Zwraca wylosowaną liczbę (pomocne do debugowania)
    public int[] returnNumber() {
        return secretNumber;
    }
}
