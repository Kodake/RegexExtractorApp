import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Programa para extraer información de un texto usando expresiones regulares o regex
 */
public class RegexExtractorApp {

    /**
     * Representa el patrón regex y su descripción.
     */
    private static class RegexPattern {
        private String description;
        private String regex;

        /**
         * Construye un nuevo patrón regex.
         *
         * @param description descripción del patrón regex
         * @param regex patrón regex
         */
        public RegexPattern(String description, String regex) {
            this.description = description;
            this.regex = regex;
        }

        /**
         * Obtiene la descripción del patrón regex.
         *
         * @return returna la descripción
         */
        public String getDescription() {
            return description;
        }

        /**
         * Obtiene el patrón regex.
         *
         * @return retorna el patrón regex
         */
        public String getRegex() {
            return regex;
        }
    }

    /**
     * Método main que comienza el programa.
     *
     * @param args línea de comando args (no utilizado)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean success = false;

        while (!success) {
            System.out.println();
            mostrarMenu();
            int opcion = scanner.nextInt();
            switch (opcion) {
                case 1:
                    extraerInformacion(scanner);
                    break;
                case 2:
                    success = true;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, seleccione una opción válida.");
            }
        }
        scanner.close();
    }

    /**
     * Muestra opciones del menú.
     */
    private static void mostrarMenu() {
        System.out.println("""
                ***** Extraer Patrones de Texto *****
                1. Extraer
                2. Salir
                """);
        System.out.print("Opción proporcionada: ");
    }

    /**
     * Extrae información del texto del contrato utilizando patrones de expresiones regulares predefinidos.
     *
     * @param scanner Se utiliza Scanner para leer la entrada o el texto
     */
    private static void extraerInformacion(Scanner scanner) {
        // Se definen los patrones Regex
        List<RegexPattern> regexPatterns = new ArrayList<>();
        regexPatterns.add(new RegexPattern("Entidad", "De una parte,\\s*(.*?),"));
        regexPatterns.add(new RegexPattern("RNC", "\\bRNC\\s(\\d{9})\\b"));
        regexPatterns.add(new RegexPattern("Representante", "Ministro,\\s*(.*?),"));
        regexPatterns.add(new RegexPattern("Cédula del Representante", "\\bCédula de Identidad y Electoral No\\.\\s(\\d{3}-\\d{7}-\\d{1})\\b"));
        regexPatterns.add(new RegexPattern("Otra Parte", "a otra parte,\\s*(.*?),"));
        regexPatterns.add(new RegexPattern("Cédula de la Otra Parte", "\\bcédula de identidad y electoral No\\.\\s(\\d{3}-\\d{7}-\\d{1})\\b"));

        System.out.println();
        System.out.println("Favor pegar el texto del contrato:");
        scanner.nextLine();
        String text = scanner.nextLine();

        List<String> failedPatterns = new ArrayList<>();
        for (RegexPattern pattern : regexPatterns) {
            ExtractionResult result = extraerRegex(text, pattern.getRegex());
            if (result.isSuccess()) {
                System.out.println(pattern.getDescription() + ": " + result.getExtractedText());
            } else {
                failedPatterns.add(pattern.getDescription());
            }
        }

        if (!failedPatterns.isEmpty()) {
            System.out.println("Uno o más errores fueron encontrados. Fallaron los patrones de: ");
            for (String failedPattern : failedPatterns) {
                System.out.println(failedPattern);
            }
            System.out.println("Favor corregirlo en el documento y vuelva a pegarlo.");
        }
    }

    /**
     * Extrae el patrón de un texto usando regex.
     *
     * @param text texto a extraer
     * @param regex patrón regex
     * @return retorna el texto limpio, o nulo si no se encuentra
     */
    private static ExtractionResult extraerRegex(String text, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return new ExtractionResult(true, matcher.group(1).trim());
        }
        return new ExtractionResult(false, null);
    }

    private static class ExtractionResult {
        private boolean success;
        private String extractedText;

        public ExtractionResult(boolean success, String extractedText) {
            this.success = success;
            this.extractedText = extractedText;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getExtractedText() {
            return extractedText;
        }
    }
}
