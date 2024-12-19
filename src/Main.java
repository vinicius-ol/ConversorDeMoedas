import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Main {

    private static final String API_URL = "https://v6.exchangerate-api.com/v6/d9d10866e1f9af4c48adaa97/latest/BRL";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite o valor em BRL que deseja converter: ");
        double amountInBRL = scanner.nextDouble();

        System.out.println("Escolha uma moeda para converter:");
        System.out.println("1. ARS - Peso argentino");
        System.out.println("2. BOB - Boliviano boliviano");
        System.out.println("3. CLP - Peso chileno");
        System.out.println("4. COP - Peso colombiano");
        System.out.println("5. USD - Dólar americano");

        String targetCurrency = "";
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                targetCurrency = "ARS";
                break;
            case 2:
                targetCurrency = "BOB";
                break;
            case 3:
                targetCurrency = "CLP";
                break;
            case 4:
                targetCurrency = "COP";
                break;
            case 5:
                targetCurrency = "USD";
                break;
            default:
                System.out.println("Escolha inválida.");
                return;
        }

        try {
            convertCurrency(amountInBRL, targetCurrency);
        } catch (Exception e) {
            System.out.println("Erro ao converter a moeda: " + e.getMessage());
        }
    }

    public static void convertCurrency(double amountInBRL, String targetCurrency) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(API_URL)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonObject conversionRates = jsonResponse.getAsJsonObject("conversion_rates");

        if (conversionRates.has(targetCurrency)) {
            double rate = conversionRates.get(targetCurrency).getAsDouble();
            double convertedAmount = amountInBRL * rate;
            System.out.printf("O valor de %.2f BRL em %s é: %.2f %s%n", amountInBRL, targetCurrency, convertedAmount, targetCurrency);
        } else {
            System.out.println("Moeda alvo não encontrada nas taxas de conversão.");
        }
    }
}
