
import com.google.gson.Gson;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {


        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n--- Conversor de Monedas ---");
            System.out.println("1. Convertir moneda");
            System.out.println("2. Salir");
            System.out.print("Seleccione una opción: ");

            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    convert(scanner);
                    break;
                case 2:
                    exit = true;
                    System.out.println("¡Adiós!");
                    break;
                default:
                    System.out.println("Opción no válida. Inténtelo de nuevo.");
            }
        }
        scanner.close();


    }

    private static void convert(Scanner scanner) {
        System.out.print("Ingrese el monto: ");
        double amount = scanner.nextDouble();

        System.out.print("Ingrese la moneda de origen (ej: USD, EUR, GBP): ");
        String fromCurrency = scanner.next().toUpperCase();

        System.out.print("Ingrese la moneda de destino (ej: USD, EUR, GBP): ");
        String toCurrency = scanner.next().toUpperCase();

        try {
            double result = convertCurrency(amount, fromCurrency, toCurrency);
            System.out.printf("Resultado: %.2f %s = %.2f %s\n", amount, fromCurrency, result, toCurrency);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }




    public static double convertCurrency(double amount, String fromCurrency, String toCurrency) throws Exception {


        String urlStr = String.format("https://v6.exchangerate-api.com/v6/%s/latest/%s", "361794ba5b548c5441774a0a", fromCurrency);
        URL url = new URL(urlStr);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        Gson gson = new Gson();
        ExchangeRate response = gson.fromJson(new InputStreamReader((java.io.InputStream) request.getContent()), ExchangeRate.class);

        Map<String, Double> rates = response.getConversionRates();
        if (!rates.containsKey(toCurrency)) {
            throw new IllegalArgumentException("Moneda no soportada: " + toCurrency);
        }

        double rate = rates.get(toCurrency);
        return amount * rate;


    }











}