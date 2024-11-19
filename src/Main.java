import java.util.*;

public class Main {

    public static void main(String[] args) {

        createIntro();

        // TESTs
//        testingNumbers("1234", "1234");
//        testingNumbers("1234", "4321");
//        testingNumbers("1234", "1111");
//        testingNumbers("1234", "1111");
//        testingNumbers("1234", "1221");
//        testingNumbers("1234", "1231");

        // Tworzenie obiektu Scanner do wczytywania danych od użytkownika
        try (Scanner userInputObj = new Scanner(System.in)) {

            // Inicjalizacja generatora losowych liczb
            GameCode number = new GameCode();

            // Debug: Wyświetlenie wylosowanej liczby (zakomentowane, aby nie zdradzać wyniku graczowi)
            //System.out.println("Losowa liczba: " + number.returnNumber());

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

    private static void testingNumbers(String secret, String userInput) {
        System.out.println("Secret: " + secret);
        System.out.println("User input: " + userInput);
        var mm = new GameCode(secret);
        var result = mm.tryNumber(userInput);
        //System.out.println("Wynik testu: " + result);
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

    final static int NO_DIGITS = 4;

    Random random = new Random(); // Inicjalizacja obiektu Random do generowania liczb losowych
    StringBuilder numberAsString; // Liczba losowa jako ciąg znaków
    int[] computerDigits, playerDigits; // Tablice cyfr dla komputera i gracza

    // Konstruktor klasy, który generuje losową liczbę i zapisuje jej cyfry w tablicy
    public GameCode() {

        numberAsString = new StringBuilder();
        List<Integer> availableDigits = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            availableDigits.add(i);
        }
        for (int i = 0; i < NO_DIGITS; i++) {
            int index = random.nextInt(availableDigits.size());
            int digit = availableDigits.remove(index);
            numberAsString.append(digit);
        }
        // Inicjalizacja obiektu ArrayGen do generowania tablicy z wygenerowanymi liczbami komputera
        ArrayGen digits = new ArrayGen(numberAsString);
        computerDigits = digits.getDigits();
    }

    public GameCode(String secret) {
        ArrayGen digits = new ArrayGen(secret);
        computerDigits = digits.getDigits();
    }

    // Sprawdzenie wprowadzonej liczby przez użytkownika
    boolean tryNumber(String number) {

        int samePosition = 0;
        int differentPosition = 0;

        // Inicjalizacja obiektu ArrayGen do generowania tablicy z wygenerowanymi liczbami użytkownika
        ArrayGen digits = new ArrayGen(number);
        playerDigits = digits.getDigits();

        // Sprawdzenie, czy liczba ma dokładnie 4 cyfry
        if (playerDigits.length != NO_DIGITS || !number.matches("\\d{" + NO_DIGITS + "}")) {
            System.out.println("Wpisana liczba musi zawierać cztery cyfry!");
            return false; // Zwraca fałsz, jeśli warunek nie jest spełniony
        }

        var computerMap = new HashMap<Integer, Integer>();
        var playerMap = new HashMap<Integer, Integer>();

        for (int i = 0; i < NO_DIGITS; i++) {
            computerMap.put(computerDigits[i], i);
        }

        // Sprawdzanie, czy liczby są w tym samym miejscu, jeśli nie to tworzymy mapę tych liczb oraz pozycji
        for (int i = 0; i < NO_DIGITS; i++) {
            if(computerDigits[i] == playerDigits[i]) {
                samePosition++;
            }
            else {
                playerMap.put(playerDigits[i], i);
            }
        }

        // Sprawdzanie ile liczb jest nie na miejscu
        for (var entry : playerMap.entrySet()) {
            int digit = entry.getKey();
            if(computerMap.containsKey(digit) && !Objects.equals(computerMap.get(digit), entry.getValue())) {
                differentPosition++;
            }
        }

        // Wyświetlenie wyników analizy
        System.out.println("Liczba trafionych cyfr na właściwych miejscach: " + samePosition);
        System.out.println("Liczba trafionych cyfr, ale na niewłaściwych miejscach: " + differentPosition);
        System.out.println();

        // Warunek zwycięstwa (wszystkie cyfry trafione na właściwych miejscach)
        // Zwraca fałsz w przeciwnym przypadku
        return samePosition == NO_DIGITS; // Zwraca prawdę, jeśli gracz odgadł całą liczbę
    }

    // Zwraca wylosowaną liczbę (pomocne do debugowania)
    StringBuilder returnNumber() {
        return numberAsString;
    }
}
