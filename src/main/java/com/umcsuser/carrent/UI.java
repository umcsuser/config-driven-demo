package com.umcsuser.carrent;

import com.umcsuser.carrent.models.Vehicle;
import com.umcsuser.carrent.models.VehicleCategoryConfig;
import com.umcsuser.carrent.services.VehicleCategoryConfigService;
import com.umcsuser.carrent.services.VehicleService;

import java.util.Scanner;

public class UI {

    private final VehicleCategoryConfigService configService;
    private final VehicleService vehicleService;
    private final Scanner scanner = new Scanner(System.in);

    public UI(VehicleCategoryConfigService configService, VehicleService vehicleService) {
        this.configService = configService;
        this.vehicleService = vehicleService;
    }

    public void start() {
        System.out.println("=== CONFIG-DRIVEN DEMO ===");
        System.out.println("Dostępne kategorie:");
        configService.findAllCategories().forEach(c -> System.out.println("- " + c.getCategory()));

        try {
            System.out.print("\nPodaj kategorię: ");
            VehicleCategoryConfig config = configService.getByCategory(scanner.nextLine().trim());

            System.out.print("Podaj markę: ");
            String brand = scanner.nextLine().trim();

            System.out.print("Podaj model: ");
            String model = scanner.nextLine().trim();

            System.out.print("Podaj rok: ");
            int year = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Podaj numer rejestracyjny: ");
            String plate = scanner.nextLine().trim();

            System.out.print("Podaj cenę: ");
            double price = Double.parseDouble(scanner.nextLine().trim());

            Vehicle vehicle = Vehicle.builder()
                    .category(config.getCategory())
                    .brand(brand)
                    .model(model)
                    .year(year)
                    .plate(plate)
                    .price(price)
                    .build();

            config.getAttributes().forEach((attrName, attrType) -> {
                System.out.print("Podaj wartość atrybutu " + attrName + " (" + attrType + "): ");
                String rawValue = scanner.nextLine().trim();

                Object value = switch (attrType.toLowerCase()) {
                    case "string" -> rawValue;
                    case "integer" -> Integer.parseInt(rawValue);
                    case "number" -> Double.parseDouble(rawValue);
                    case "boolean" -> Boolean.parseBoolean(rawValue);
                    default -> throw new IllegalArgumentException("Nieobsługiwany typ: " + attrType);
                };
                vehicle.addAttribute(attrName, value);
            });

            Vehicle added = vehicleService.addVehicle(vehicle);
            System.out.println("Pojazd dodany pomyślnie:" + added);
            System.out.println(vehicleService.findAllVehicles());

        } catch (NumberFormatException e) {
            System.out.println("Błąd: Wprowadzono tekst zamiast liczby! Spróbuj ponownie.");
        } catch (Exception e) {
            System.out.println("Błąd: " + e.getMessage());
        }
    }
}