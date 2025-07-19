package org.whogames.digitalminaturia;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.io.FileNotFoundException;
import java.nio.file.StandardCopyOption;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.svg.SVGDocument;

public class SVGMapViewer {

    private Element selectedElement = null;
    private static ArrayList<Province> provinceList = new ArrayList<>();
    private static ArrayList<Country> countryList = new ArrayList<>();
    private static ArrayList<Entity> technologyList = new ArrayList<>();
    private static HashMap<String, Entity> entityMap = new HashMap<>();
    private final File dataDir = new File(System.getProperty("user.home"), "MinaturiaData");

    JSVGCanvas svgCanvas = new JSVGCanvas();

    private Province currentProvince = null;
    private int currentProvinceIndex = -1;
    private JButton foodInput;
    private JSlider fuelSlider;
    private JComboBox<String> resourceSelect;

    public void createAndShowGUI() throws Exception {

        // Ensure the data folder exists
if (!dataDir.exists()) {
    dataDir.mkdirs();
}

// Copy default CSVs from resources on first launch
try {
    copyResourceToFile("Minaturia Countries.csv", new File(dataDir, "Minaturia Countries.csv"));
    copyResourceToFile("Minaturia Provinces.csv", new File(dataDir, "Minaturia Provinces.csv"));
    copyResourceToFile("Minaturia Technology.csv", new File(dataDir, "Minaturia Technology.csv"));
    copyResourceToFile("Minaturia Map.svg", new File(dataDir, "Minaturia Map.svg"));
    // You can skip per-country inventory here and create them on demand later
} catch (IOException e) {
    e.printStackTrace();
    JOptionPane.showMessageDialog(null, "Failed to load game data files: " + e.getMessage());
}


countryList = ProvinceParser.parseCountries(
    new FileReader(new File(dataDir, "Minaturia Countries.csv"))
);
provinceList = ProvinceParser.parseProvinces(
    new FileReader(new File(dataDir, "Minaturia Provinces.csv"))
);
technologyList = ProvinceParser.parseItems(
    new FileReader(new File(dataDir, "Minaturia Technology.csv"))
);


        for (Entity tech : technologyList) {
            entityMap.put(tech.getName(), tech);
        }
        for (Country country : countryList) {

File invFile = new File(dataDir, country.getName() + " Inventory.csv");
if (!invFile.exists()) {
    // Optional: copy a default, or create empty file
    copyResourceToFile(invFile.getName(), new File(dataDir, invFile.getName()));
}
ProvinceParser.parseInventory(new FileReader(invFile), country, entityMap);
        }

        JFrame frame = new JFrame("Minaturia");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Top panel setup
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.BLACK);
        topPanel.setPreferredSize(new Dimension(1920, 100));

        JLabel countryNameLabel = new JLabel("COUNTRY: NONE SELECTED");
        countryNameLabel.setFont(new Font("Monospaced", Font.BOLD, 18));
        countryNameLabel.setForeground(Color.WHITE);
        countryNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel capitalLabel = new JLabel("Capital: ");
        capitalLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        capitalLabel.setForeground(Color.LIGHT_GRAY);
        capitalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel populationLabel = new JLabel("Population: ");
        populationLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        populationLabel.setForeground(Color.LIGHT_GRAY);
        populationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        topPanel.add(countryNameLabel);
        topPanel.add(capitalLabel);
        topPanel.add(populationLabel);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        buttonPanel.setBackground(Color.BLACK);

        Font buttonFont = new Font("Monospaced", Font.BOLD, 14);

        JButton mapButton = new JButton("Map");
        JButton economyButton = new JButton("Economy");
        JButton productionButton = new JButton("Production");

        JButton[] buttons = {mapButton, economyButton, productionButton};
        for (JButton btn : buttons) {
            btn.setFont(buttonFont);
            btn.setBackground(Color.DARK_GRAY);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            buttonPanel.add(btn);
        }

        topPanel.add(buttonPanel);

        frame.add(topPanel, BorderLayout.NORTH);

        // Info panel setup
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        infoPanel.setPreferredSize(new Dimension(300, 1080));
        infoPanel.setBackground(Color.BLACK);

        Font font = new Font("Monospaced", Font.PLAIN, 14);

        JTextField nameField = new JTextField(15);
        JTextField languageField = new JTextField(15);
        JTextField populationField = new JTextField(15);
        JTextField terrainField = new JTextField(15);
        JTextField tierField = new JTextField(15);
        JTextField cityTypeField = new JTextField(15);
        JTextField budget1Field = new JTextField(15);
        JTextField budget2Field = new JTextField(15);
        JButton updateButton = new JButton("Save Changes");

        JTextField[] fields = {
            nameField, languageField, populationField,
            terrainField, tierField, cityTypeField, budget1Field, budget2Field
        };

        for (JTextField field : fields) {
            field.setFont(font);
            field.setBackground(Color.BLACK);
            field.setForeground(Color.WHITE);
            field.setCaretColor(Color.WHITE);
        }

        updateButton.setFont(font);
        updateButton.setBackground(Color.DARK_GRAY);
        updateButton.setForeground(Color.WHITE);

        addField(infoPanel, "Name:", nameField, font);
        addField(infoPanel, "Language:", languageField, font);
        addField(infoPanel, "Population:", populationField, font);
        addField(infoPanel, "Terrain:", terrainField, font);
        addField(infoPanel, "Tier:", tierField, font);
        addField(infoPanel, "City Type:", cityTypeField, font);
        addField(infoPanel, "Food Production:", budget1Field, font);
        addField(infoPanel, "Fuel Production:", budget2Field, font);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(updateButton);

        // Setup layered pane for svg and overlays
        JLayeredPane layeredPane = new JLayeredPane();
        // Set preferred size to your SVG native size or a default
        layeredPane.setPreferredSize(new Dimension(2000, 1250));

        svgCanvas.setBounds(0, 0, 2000, 1250);
        layeredPane.add(svgCanvas, JLayeredPane.DEFAULT_LAYER);

        Font consoleFont = new Font("Monospaced", Font.PLAIN, 14);

        foodInput = new JButton("Order");
        foodInput.setBackground(Color.BLACK);
        foodInput.setForeground(Color.WHITE);
        foodInput.setFont(consoleFont);
        foodInput.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        fuelSlider = new JSlider(0, 1000, 300);
        fuelSlider.setBackground(Color.BLACK);
        fuelSlider.setForeground(Color.WHITE);
        fuelSlider.setFont(consoleFont);
        fuelSlider.setPaintTicks(true);
        fuelSlider.setPaintLabels(true);
        fuelSlider.setMajorTickSpacing(250);
        fuelSlider.setMinorTickSpacing(50);

        resourceSelect = new JComboBox<>(new String[]{"Food", "Fuel", "Iron", "Steel"});
        resourceSelect.setBackground(Color.BLACK);
        resourceSelect.setForeground(Color.WHITE);
        resourceSelect.setFont(consoleFont);
        resourceSelect.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        layeredPane.add(fuelSlider, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(resourceSelect, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(foodInput, JLayeredPane.PALETTE_LAYER);

        svgCanvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
        svgCanvas.setBackground(Color.BLACK);

        File svgFile = new File(dataDir, "Minaturia Map.svg");

        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
        Document parsedDoc = factory.createDocument(svgFile.toURI().toString());
        svgCanvas.setDocument(parsedDoc);

        // Initially show map
        setVisibleLayerSafe("Layer_map");

        // Button listeners
        mapButton.addActionListener(e -> setVisibleLayerSafe("Layer_map"));
        economyButton.addActionListener(e -> setVisibleLayerSafe("Layer_economy"));
        productionButton.addActionListener(e -> setVisibleLayerSafe("Layer_production"));
        foodInput.addActionListener(e -> {
            if (currentProvince == null || currentProvince.getCountry() == null) {
                JOptionPane.showMessageDialog(svgCanvas, "No province selected.");
            } else {
                updateInventoryForCountry(currentProvince.getCountry());
                try {
                    ProvinceParser.writeInventoryToCSV(
    getCountryByName(currentProvince.getCountry()),
    new File(dataDir, currentProvince.getCountry() + " Inventory.csv").getAbsolutePath()
);

                                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                updateProductionLayer(getCountryByName(currentProvince.getCountry()));
            }
        });

        // Province click listeners as before...
        svgCanvas.addGVTTreeRendererListener(new GVTTreeRendererAdapter() {
            public void gvtRenderingCompleted(GVTTreeRendererEvent e) {
                svgCanvas.getUpdateManager().getUpdateRunnableQueue().invokeLater(() -> {
                    Document doc = svgCanvas.getSVGDocument();
                    Element layerMap = doc.getElementById("Layer_map");

                    if (layerMap == null) {
                        System.out.println("Layer_map NOT found!");
                        return;
                    }

                    NodeList children = layerMap.getElementsByTagName("*");
                    for (int i = 0; i < children.getLength(); i++) {
                        Element el = (Element) children.item(i);
                        String id = el.getAttribute("id");

                        if (id != null && !id.isEmpty()) {
                            el.setAttribute("pointer-events", "visiblePainted");
                            EventTarget target = (EventTarget) el;

                            target.addEventListener("click", evt -> {
                                int provinceId;
                                try {
                                    provinceId = Integer.parseInt(id.replaceAll("[^\\d]", ""));
                                } catch (NumberFormatException ex) {
                                    System.err.println("Invalid province ID: " + id);
                                    return;
                                }

                                if (selectedElement == el) {
                                    selectedElement.setAttribute("style", "fill:black;stroke:white;stroke-width:1;");
                                    selectedElement = null;
                                    currentProvince = null;
                                    currentProvinceIndex = -1;

                                    clearInfoFields(nameField, languageField, populationField, terrainField,
                                            tierField, cityTypeField, budget1Field, budget2Field,
                                            countryNameLabel, capitalLabel, populationLabel);
                                    return;
                                }

                                currentProvinceIndex = provinceId - 1;
                                currentProvince = provinceList.get(currentProvinceIndex);

                                nameField.setText(currentProvince.getName());
                                languageField.setText(currentProvince.getLanguage());
                                populationField.setText(String.valueOf(currentProvince.getPopulation()));
                                terrainField.setText(currentProvince.getTerrain());
                                tierField.setText(String.valueOf(currentProvince.getTier()));
                                cityTypeField.setText(currentProvince.getCityType());
                                budget1Field.setText(String.valueOf(currentProvince.getBudget1()));
                                budget2Field.setText(String.valueOf(currentProvince.getBudget2()));

                                Country country = getCountryByName(currentProvince.getCountry());

                                if (country != null) {
                                    countryNameLabel.setText("COUNTRY: " + country.getName().toUpperCase());
                                    capitalLabel.setText("Capital: " + country.getCapital());
                                    populationLabel.setText("Population: " + country.getPopulation());

                                    updateResourceOptionsForCountry(country.getName());
                                    updateProductionLayer(country);
                                } else {
                                    countryNameLabel.setText("COUNTRY: " + currentProvince.getCountry().toUpperCase());
                                    capitalLabel.setText("Capital: Unknown");
                                    populationLabel.setText("Population: Unknown");

                                    updateResourceOptionsForCountry(null);
                                }

                                if (selectedElement != null && selectedElement != el) {
                                    selectedElement.setAttribute("style", "fill:black;stroke:white;stroke-width:1;");

                                }

                                el.setAttribute("style", "fill:white;stroke:black;stroke-width:2;");
                                selectedElement = el;
                            }, false);
                        }
                    }
                });
            }
        });

        // Update button action as before...
        updateButton.addActionListener(e -> {
            if (currentProvince != null && currentProvinceIndex != -1) {
                try {
                    currentProvince.setName(nameField.getText());
                    currentProvince.setLanguage(languageField.getText());
                    currentProvince.setPopulation(Integer.parseInt(populationField.getText()));
                    currentProvince.setTerrain(terrainField.getText());
                    currentProvince.setTier(Integer.parseInt(tierField.getText()));
                    currentProvince.setCityType(cityTypeField.getText());
                    currentProvince.setBudget1(Long.parseLong(budget1Field.getText()));
                    currentProvince.setBudget2(Long.parseLong(budget2Field.getText()));

ProvinceParser.writeProvincesToCSV(provinceList, new File(dataDir, "Minaturia Provinces.csv").getAbsolutePath());

                    JOptionPane.showMessageDialog(frame, "Changes saved.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                }
            } else {
                System.out.println("No province selected.");
            }
        });

        frame.add(infoPanel, BorderLayout.WEST);
        frame.add(layeredPane, BorderLayout.CENTER);

        // *** Make frame resizable ***
        frame.setResizable(true);

        // *** Add component listener to reposition overlays on resize ***
        layeredPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension size = layeredPane.getSize();

                // Resize svgCanvas to fill layeredPane
                svgCanvas.setBounds(0, 0, size.width, size.height);

                // Position overlay controls relative to panel size
                int x = (int) (size.width * 0.05);
                int y = (int) (size.height * 0.08);
                int width = 180;
                int height = 30;

                // Increased spacing for better visual separation
                int spacing = 45;

                resourceSelect.setBounds(x, y, width, height);
                fuelSlider.setBounds(x, y + spacing, width + 70, height);
                foodInput.setBounds(x, y + spacing * 2, width, height);
            }
        });

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void updateInventoryForCountry(String country) {
        if (country == null) {
            JOptionPane.showMessageDialog(svgCanvas, "No country selected.");
            return;
        }

        // Example logic to update inventory based on production controls
        String selectedResource = (String) resourceSelect.getSelectedItem();
        int fuelAmount = fuelSlider.getValue();

        Entity resource = entityMap.get(selectedResource);
        Country A = getCountryByName(country);
        if (resource != null && A != null) {
            if (A.getInventory().containsKey(resource)) {
                int currentAmount = A.getInventory().get(resource);
                A.getInventory().put(resource, currentAmount + fuelAmount);
            } else {
                A.getInventory().put(resource, fuelAmount);
            }
            JOptionPane.showMessageDialog(svgCanvas, "Added");
        }
    }

    public void setVisibleLayer(SVGDocument doc, String layerIdToShow) {
        NodeList layers = doc.getElementsByTagName("g");

        for (int i = 0; i < layers.getLength(); i++) {
            Element layer = (Element) layers.item(i);
            String id = layer.getAttribute("id");

            if ("Layer_3".equals(id)) {
                layer.setAttribute("style", "opacity:1; pointer-events: none;");
            } else if (layerIdToShow.equals(id)) {
                layer.setAttribute("style", "opacity:1; pointer-events: all;");
            } else {
                layer.setAttribute("style", "opacity:0; pointer-events: none;");
            }
        }

        // Show/hide production UI controls
        boolean showProductionControls = "Layer_production".equals(layerIdToShow);
        setProductionControlsVisible(foodInput, fuelSlider, resourceSelect, showProductionControls);

        svgCanvas.repaint();
    }

    public void updateProductionLayer(Country country) {
        if (country == null) {
            return;
        }

        SVGDocument svgDoc = svgCanvas.getSVGDocument();
        Element layer = svgDoc.getElementById("Layer_production");

        if (layer == null) {
            System.err.println("Layer_production not found.");
            return;
        }

        // Clear existing <text> nodes
        NodeList children = layer.getChildNodes();
        ArrayList<Node> toRemove = new ArrayList<>();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeName().equals("text")) {
                toRemove.add(node);
            }
        }
        for (Node node : toRemove) {
            layer.removeChild(node);
        }

        // Add updated production text
        int startX = 470;
        int startY = 440;
        int lineHeight = 25;

        Element title = svgDoc.createElementNS("http://www.w3.org/2000/svg", "text");
        title.setAttribute("x", String.valueOf(startX));
        title.setAttribute("y", String.valueOf(startY));
        title.setAttribute("font-family", "monospace");
        title.setAttribute("font-size", "18");
        title.setAttribute("fill", "white");
        title.setTextContent(country.getName() + " Inventory:");
        layer.appendChild(title);

        startY += lineHeight;

        String[] items = {};

        for (Map.Entry<Entity, Integer> entry : country.getInventory().entrySet()) {
            Entity entity = entry.getKey();
            if (entity == null) {
                continue;
            }
            int amount = entry.getValue();
            items = Arrays.copyOf(items, items.length + 1);
            items[items.length - 1] = "- " + entity.getName() + ": " + amount;
        }

        System.out.println(Arrays.toString(items));

        for (String line : items) {
            Element textLine = svgDoc.createElementNS("http://www.w3.org/2000/svg", "text");
            textLine.setAttribute("x", String.valueOf(startX + 10));
            textLine.setAttribute("y", String.valueOf(startY));
            textLine.setAttribute("font-family", "monospace");
            textLine.setAttribute("font-size", "14");
            textLine.setAttribute("fill", "white");
            textLine.setTextContent(line);
            layer.appendChild(textLine);
            startY += lineHeight;
        }

        svgCanvas.repaint();
    }

    private void setVisibleLayerSafe(String layerIdToShow) {
        if (svgCanvas.getUpdateManager() != null) {
            // Run inside Batik's update thread to ensure immediate update
            svgCanvas.getUpdateManager().getUpdateRunnableQueue().invokeLater(() -> {
                setVisibleLayer(svgCanvas.getSVGDocument(), layerIdToShow);
            });
        } else {
            // UpdateManager not ready yet, wait for it after render
            svgCanvas.addGVTTreeRendererListener(new GVTTreeRendererAdapter() {
                public void gvtRenderingCompleted(GVTTreeRendererEvent e) {
                    svgCanvas.getUpdateManager().getUpdateRunnableQueue().invokeLater(() -> {
                        setVisibleLayer(svgCanvas.getSVGDocument(), layerIdToShow);
                    });
                    // Remove listener after use to avoid repeated calls
                    svgCanvas.removeGVTTreeRendererListener(this);
                }
            });
        }
    }

    private void addField(JPanel panel, String labelText, JTextField field, Font font) {
        JLabel label = new JLabel(labelText);
        label.setFont(font);
        label.setForeground(Color.WHITE);
        field.setFont(font);
        panel.add(label);
        panel.add(field);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private void clearInfoFields(JTextField nameField, JTextField languageField, JTextField populationField,
            JTextField terrainField, JTextField tierField, JTextField cityTypeField,
            JTextField budget1Field, JTextField budget2Field,
            JLabel countryNameLabel, JLabel capitalLabel, JLabel populationLabel) {
        nameField.setText("");
        languageField.setText("");
        populationField.setText("");
        terrainField.setText("");
        tierField.setText("");
        cityTypeField.setText("");
        budget1Field.setText("");
        budget2Field.setText("");
        countryNameLabel.setText("COUNTRY: NONE SELECTED");
        capitalLabel.setText("Capital: ");
        populationLabel.setText("Population: ");
    }

    private Country getCountryByName(String name) {
        for (Country country : countryList) {
            if (country.getName().equalsIgnoreCase(name.trim())) {
                return country;
            }
        }
        return null;
    }

    public void setProductionControlsVisible(JButton foodInput, JSlider fuelSlider, JComboBox<String> resourceSelect, boolean visible) {
        foodInput.setVisible(visible);
        fuelSlider.setVisible(visible);
        resourceSelect.setVisible(visible);
    }

    public ArrayList<String> getTechByCountry(String country) {
        ArrayList<String> techList = new ArrayList<>();
        for (Entity item : technologyList) {
            if (item instanceof Vehicle && ((Vehicle) item).getCountry().equalsIgnoreCase(country)) {
                techList.add(item.getName());
            } else if (item instanceof Ammunition && ((Ammunition) item).getCountry().equalsIgnoreCase(country)) {
                techList.add(item.getName());
            } else if (item instanceof Firearm && ((Firearm) item).getCountry().equalsIgnoreCase(country)) {
                techList.add(item.getName());
            }
        }
        return techList;
    }

    private void updateResourceOptionsForCountry(String countryName) {
        resourceSelect.removeAllItems();

        if (countryName == null) {
            // Default or fallback options
            resourceSelect.addItem("Food");
            resourceSelect.addItem("Fuel");
            resourceSelect.addItem("Iron");
            resourceSelect.addItem("Steel");
            return;
        }

        ArrayList<String> countryTech = getTechByCountry(countryName);

        if (countryTech.isEmpty()) {
            // Country has no specific tech/resources
            resourceSelect.addItem("Food");
            resourceSelect.addItem("Fuel");
        } else {
            for (String tech : countryTech) {
                resourceSelect.addItem(tech);
            }
        }
    }

    private void copyResourceToFile(String resourceName, File dest) throws IOException {
    if (dest.exists()) return;

    try (InputStream in = getClass().getResourceAsStream("/" + resourceName);
         FileOutputStream out = new FileOutputStream(dest)) {
        if (in == null) throw new IOException("Resource not found: " + resourceName);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
    }
}


}
