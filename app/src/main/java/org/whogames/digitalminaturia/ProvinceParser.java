package org.whogames.digitalminaturia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AddSheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.ClearValuesRequest;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

public class ProvinceParser {

    private static final File DATA_DIR = new File(System.getProperty("user.home"), "MinaturiaData");
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    public static final String SPREADSHEET_ID = "1zdxiifVs-smN-kDVvuUSdTdnfzJYnJkxKQo4BCWI9uY";
    private static final Map<String, Object> fileLocks = new ConcurrentHashMap<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
                System.out.println("Uploading CSVs before exit...");
                new ProvinceParser().uploadAllCSVsToGoogleSheet();
            } catch (Exception e) {
                System.err.println("Shutdown upload error:");
                e.printStackTrace();
            }
        }));

        scheduler.scheduleAtFixedRate(() -> {
            try {
                checkPendingCSVWrites();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public Sheets getSheetsService() throws Exception {
        String credentialsPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
        System.out.println(credentialsPath);
        if (credentialsPath == null || credentialsPath.isEmpty()) {
            throw new IllegalStateException("Env var GOOGLE_APPLICATION_CREDENTIALS is not set.");
        }

        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        try (InputStream in = new FileInputStream(credentialsPath)) {
            GoogleCredentials creds = GoogleCredentials.fromStream(in)
                    .createScoped(Collections.singletonList(SheetsScopes.SPREADSHEETS));
            return new Sheets.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    jsonFactory,
                    new HttpCredentialsAdapter(creds)
            ).setApplicationName("Digital Minaturia Uploader").build();
        }
    }

    public void uploadCSVToSheet(String csvPath, String sheetName) throws Exception {
        Sheets svc = getSheetsService();
        Spreadsheet ss = svc.spreadsheets().get(SPREADSHEET_ID).execute();

        boolean exists = ss.getSheets().stream()
                .map(Sheet::getProperties)
                .map(SheetProperties::getTitle)
                .anyMatch(title -> title.equalsIgnoreCase(sheetName));

        if (!exists) {
            AddSheetRequest add = new AddSheetRequest()
                    .setProperties(new SheetProperties().setTitle(sheetName));
            BatchUpdateSpreadsheetRequest req = new BatchUpdateSpreadsheetRequest()
                    .setRequests(Collections.singletonList(new Request().setAddSheet(add)));
            svc.spreadsheets().batchUpdate(SPREADSHEET_ID, req).execute();
            System.out.println("Created sheet " + sheetName);
        }

        List<List<Object>> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<Object> row = new ArrayList<>();
                for (String tok : line.split(",")) row.add(tok.trim());
                data.add(row);
            }
        }

        svc.spreadsheets().values()
                .clear(SPREADSHEET_ID, sheetName + "!A:Z", new ClearValuesRequest())
                .execute();

        svc.spreadsheets().values()
                .update(SPREADSHEET_ID, sheetName + "!A1", new ValueRange().setValues(data))
                .setValueInputOption("RAW")
                .execute();

        System.out.println("Uploaded CSV to sheet: " + sheetName);
    }

    public void uploadAllCSVsToGoogleSheet() {
        File[] files = DATA_DIR.listFiles((d, name) -> name.endsWith(".csv"));
        if (files == null) return;
        for (File f : files) {
            String sheetName = f.getName().replace(".csv", "");
            try {
                uploadCSVToSheet(f.getAbsolutePath(), sheetName);
            } catch (Exception e) {
                System.err.println("Failed upload: " + f.getName());
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<Province> parseProvinces(Reader reader) throws IOException {
        ArrayList<Province> provinces = new ArrayList<>();
        BufferedReader br = new BufferedReader(reader);
        String line;

        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty() || line.startsWith("ID,")) {
                continue;
            }

            String[] tokens = line.split(",");

            if (tokens.length < 10) {
                System.err.println("Skipping malformed line: " + line);
                continue;
            }

            try {
                int id = Integer.parseInt(tokens[0].trim());
                String name = tokens[1].trim();
                String country = tokens[2].trim();
                String language = tokens[3].trim();
                int population = Integer.parseInt(tokens[4].trim());
                String terrain = tokens[5].trim();
                int tier = Integer.parseInt(tokens[6].trim());
                String cityType = tokens[7].trim();
                long budget1 = Long.parseLong(tokens[8].trim());
                long budget2 = Long.parseLong(tokens[9].trim());

                Province province = new Province(id, name, country, language, population,
                        terrain, tier, cityType, budget1, budget2);
                provinces.add(province);

            } catch (NumberFormatException e) {
                System.err.println("Number format error in line: " + line);
            }
        }

        return provinces;
    }

    public static ArrayList<Country> parseCountries(Reader reader) throws IOException {
        ArrayList<Country> countries = new ArrayList<>();
        BufferedReader br = new BufferedReader(reader);
        String line;

        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty() || line.startsWith("ID,")) {
                continue;
            }

            String[] tokens = line.split(",");

            if (tokens.length < 16) {
                System.err.println("Skipping malformed country line: " + line);
                continue;
            }

            try {
                String name = tokens[0].trim();
                String capital = tokens[1].trim();
                int population = Integer.parseInt(tokens[2].trim());
                int cities = Integer.parseInt(tokens[3].trim());
                int universities = Integer.parseInt(tokens[4].trim());
                double gdp = Double.parseDouble(tokens[5].trim());
                String taxRateStr = tokens[6].trim().replace("%", "");
                double taxRate = Double.parseDouble(taxRateStr) / 100.0;
                double gdpGrowth = Double.parseDouble(tokens[7].trim());
                long baseBudget = Long.parseLong(tokens[8].trim());
                double debtRatio = Double.parseDouble(tokens[9].trim());
                long militarySpending = Long.parseLong(tokens[10].trim());
                double inflationRate = Double.parseDouble(tokens[11].trim());
                double gdpPerCapita = Double.parseDouble(tokens[12].trim());
                double incomePerCapita = Double.parseDouble(tokens[13].trim());
                double medianIncome = Double.parseDouble(tokens[14].trim());
                double happiness = Double.parseDouble(tokens[15].trim());

                Country country = new Country(name, capital, population, cities, universities, gdp, taxRate,
                        gdpGrowth, baseBudget, debtRatio, militarySpending, inflationRate,
                        gdpPerCapita, incomePerCapita, medianIncome, happiness);

                countries.add(country);

            } catch (NumberFormatException e) {
                System.err.println("Number format error in country line: " + line);
            }
        }

        return countries;
    }

    public static ArrayList<Entity> parseItems(Reader reader) throws IOException {
        ArrayList<Entity> technology = new ArrayList<>();
        BufferedReader br = new BufferedReader(reader);
        String line;

        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) {
                continue;
            }

            String[] tokens = line.split(",");

            if (tokens.length < 5) {
                System.err.println("Skipping malformed technology line: " + line);
                continue;
            }

            try {
                Entity item = null;
                String name = tokens[0].trim();
                String type = tokens[1].trim();
                String origin = tokens[2].trim();
                int quantity = Integer.parseInt(tokens[3].trim());
                double cost = Double.parseDouble(tokens[4].trim());
                for (String i : Vehicle.types) {
                    if (i.equalsIgnoreCase(type)) {
                        item = new Vehicle(name, type, origin, quantity, cost);
                        technology.add(item);
                        break;
                    }
                }
                if (item != null) {
                    continue;
                }
                for (String i : Ammunition.types) {
                    if (i.equalsIgnoreCase(type)) {
                        item = new Ammunition(name, type, origin, quantity, cost);
                        technology.add(item);
                        break;
                    }
                }
                if (item != null) {
                    continue;
                }
                for (String i : Firearm.types) {
                    if (i.equalsIgnoreCase(type)) {
                        item = new Firearm(name, type, origin, quantity, cost);
                        technology.add(item);
                        break;
                    }
                }

            } catch (NumberFormatException e) {
                System.err.println("Number format error in technology line: " + line);
            }
        }

        return technology;
    }

    public static void parseInventory(Reader reader, Country country, Map<String, Entity> entityMap) throws IOException {
        BufferedReader br = new BufferedReader(reader);
        String line;

        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) {
                continue;
            }

            String[] tokens = line.split(",");

            if (tokens.length < 2) {
                System.err.println("Skipping malformed inventory line: " + line);
                continue;
            }

            String itemName = tokens[0].trim();
            System.out.println("Processing item: " + itemName);
            int quantity;

            try {
                quantity = Integer.parseInt(tokens[1].trim());
            } catch (NumberFormatException e) {
                System.err.println("Invalid quantity in inventory line: " + line);
                continue;
            }

            Entity entity = entityMap.get(itemName);
            country.getInventory().put(entity, quantity);
        }
    }

    public static void writeProvincesToCSV(List<Province> provinces, String filePath) throws IOException {
        File outFile = new File(filePath);
        System.out.println("Attempting to write to: " + outFile.getAbsolutePath());

        try (PrintWriter writer = new PrintWriter(new FileWriter(outFile))) {
            for (Province p : provinces) {
                writer.printf("%d,%s,%s,%s,%d,%s,%d,%s,%d,%d%n",
                        p.getId(),
                        p.getName(),
                        p.getCountry(),
                        p.getLanguage(),
                        p.getPopulation(),
                        p.getTerrain(),
                        p.getTier(),
                        p.getCityType(),
                        p.getBudget1(),
                        p.getBudget2());
            }
            System.out.println("CSV file successfully updated.");
        } catch (IOException e) {
            System.err.println("Failed to write CSV: " + e.getMessage());
            throw e;
        }
    }

    public static void writeInventoryToCSV(Country country, String filePath) throws IOException {
        Object lock = fileLocks.computeIfAbsent(filePath, k -> new Object());

        synchronized (lock) {
            File file = new File(filePath);
            System.out.println("Reading and updating: " + file.getAbsolutePath());

            Map<String, Integer> currentInventory = new LinkedHashMap<>();

            // Step 1: Read existing data
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",", -1);
                        if (parts.length >= 2) {
                            String name = parts[0].trim();
                            int quantity;
                            try {
                                quantity = Integer.parseInt(parts[1].trim());
                            } catch (NumberFormatException e) {
                                quantity = 0;
                            }
                            currentInventory.put(name, quantity);
                        }
                    }
                }
            }

            // Step 2: Update inventory with the new country data
            System.out.println(country.getInventory().toString());
            for (Map.Entry<Entity, Integer> entry : country.getInventory().entrySet()) {
                String itemName = entry.getKey().getName();
                int newQuantity = entry.getValue();
                currentInventory.put(itemName, newQuantity); // replace or add
            }

            // Step 3: Write the updated data back to the CSV
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                for (Map.Entry<String, Integer> entry : currentInventory.entrySet()) {
                    writer.printf("%s,%d%n", entry.getKey(), entry.getValue());
                }
            }

            System.out.println("CSV successfully updated with inventory changes.");
        }
    }

    public static void scheduleCSVWrite(String countryName) throws IOException {
        long ts = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1);
        File f = new File(DATA_DIR, countryName + "_write_time.txt");
        synchronized (fileLocks.computeIfAbsent(countryName, k -> new Object())) {
            try (PrintWriter w = new PrintWriter(new FileWriter(f, true))) {
                w.println(ts);
            }
        }
    }

    public static void checkPendingCSVWrites() {
        File[] files = DATA_DIR.listFiles((d, name) -> name.endsWith("_write_time.txt"));
        if (files == null) return;
        for (File f : files) {
            String cn = f.getName().replace("_write_time.txt", "");
            Country country = SVGMapViewer.getCountryByName(cn);
            if (country == null) {
                System.err.println("No country: " + cn);
                continue;
            }
            List<Long> times = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String l;
                while ((l = br.readLine()) != null) {
                    try {
                        times.add(Long.parseLong(l.trim()));
                    } catch (NumberFormatException ignore) {
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (Long ts : times) {
                long delay = ts - System.currentTimeMillis();
                Runnable task = () -> {
                    try {
                        writeInventoryToCSV(country,
                                new File(DATA_DIR, cn + " Inventory.csv").getAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                };
                if (delay <= 0) scheduler.execute(task);
                else scheduler.schedule(task, delay, TimeUnit.MILLISECONDS);
            }
            f.delete();
        }
    }
}
