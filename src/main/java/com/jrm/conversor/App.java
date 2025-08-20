package com.jrm.conversor;

import com.jrm.conversor.service.CurrencyConverter;

import java.util.Locale;
import java.util.Scanner;

public class App {

    private static void imprimirMenu() {
        System.out.println("\n=== Conversor de Monedas ===");
        System.out.println("1) USD  -> ARS");
        System.out.println("2) ARS  -> USD");
        System.out.println("3) USD  -> BRL");
        System.out.println("4) BRL  -> USD");
        System.out.println("5) USD  -> CLP");
        System.out.println("6) CLP  -> USD");
        System.out.println("0) Salir");
        System.out.print("Elija una opción: ");
    }

    private static double pedirMonto(Scanner sc) {
        while (true) {
            System.out.print("Monto a convertir: ");
            if (sc.hasNextDouble()) {
                double amount = sc.nextDouble();
                if (amount > 0) return amount;
            } else {
                sc.next(); // descarta token inválido
            }
            System.out.println("Monto inválido. Ingrese un número mayor que cero.");
        }
    }

    private static void convertir(CurrencyConverter converter, String from, String to, double amount) {
        try {
            double rate = converter.getRate(from, to);
            double result = amount * rate;
            System.out.printf(Locale.US,
                    "%,.2f %s  x  %,.6f (%s/%s)  =  %,.2f %s%n",
                    amount, from, rate, to, from, result, to);
        } catch (Exception e) {
            System.err.println("Error al convertir " + from + "->" + to + ": " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            CurrencyConverter converter = new CurrencyConverter();

            while (true) {
                imprimirMenu();

                String opcionStr = sc.next().trim();
                if (!opcionStr.matches("\\d+")) {
                    System.out.println("Ingrese un número de opción.");
                    continue;
                }
                int opcion = Integer.parseInt(opcionStr);

                if (opcion == 0) {
                    System.out.println("¡Hasta luego!");
                    break;
                }

                double amount;
                switch (opcion) {
                    case 1: // USD -> ARS
                        amount = pedirMonto(sc);
                        convertir(converter, "USD", "ARS", amount);
                        break;
                    case 2: // ARS -> USD
                        amount = pedirMonto(sc);
                        convertir(converter, "ARS", "USD", amount);
                        break;
                    case 3: // USD -> BRL
                        amount = pedirMonto(sc);
                        convertir(converter, "USD", "BRL", amount);
                        break;
                    case 4: // BRL -> USD
                        amount = pedirMonto(sc);
                        convertir(converter, "BRL", "USD", amount);
                        break;
                    case 5: // USD -> CLP
                        amount = pedirMonto(sc);
                        convertir(converter, "USD", "CLP", amount);
                        break;
                    case 6: // CLP -> USD
                        amount = pedirMonto(sc);
                        convertir(converter, "CLP", "USD", amount);
                        break;
                    default:
                        System.out.println("Opción inválida. Elija entre 0 y 6.");
                }
            }
        }
    }
}
