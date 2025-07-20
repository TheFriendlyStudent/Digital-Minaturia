package org.whogames.digitalminaturia;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;

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
    private final String[] SvgFiles = new String[]{"Minaturia Map", "Economy Layer", "Production Layer", "Research Layer"}; 

    JSVGCanvas svgCanvas = new JSVGCanvas();

    private Province currentProvince = null;
    private int currentProvinceIndex = -1;
    private JButton foodInput;
    private JSlider fuelSlider;
    private JComboBox<String> resourceSelect;

    private double zoomFactor = 1.0;
    private final double zoomStep = 0.1;  // zoom increment
    private final double zoomMin = 0.5;   // 50% min zoom
    private final double zoomMax = 3.0;   // 300% max zoom

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
        copyResourceToFile("Production Layer.svg", new File(dataDir, "Production Layer.svg"));
        copyResourceToFile("Production Layer.svg", new File(dataDir, "Research Layer.svg"));
        copyResourceToFile("Production Layer.svg", new File(dataDir, "Economy Layer.svg"));
    } catch (IOException e) {
        e.printStackTrace();
        showStyledDialog("Failed to load game data files: " + e.getMessage());
    }

    countryList = ProvinceParser.parseCountries(new FileReader(new File(dataDir, "Minaturia Countries.csv")));
    provinceList = ProvinceParser.parseProvinces(new FileReader(new File(dataDir, "Minaturia Provinces.csv")));
    technologyList = ProvinceParser.parseItems(new FileReader(new File(dataDir, "Minaturia Technology.csv")));

    for (Entity tech : technologyList) {
        entityMap.put(tech.getName(), tech);
    }
    for (Country country : countryList) {
        File invFile = new File(dataDir, country.getName() + " Inventory.csv");
        if (!invFile.exists()) {
            invFile.createNewFile();
        }
        ProvinceParser.parseInventory(new FileReader(invFile), country, entityMap);
    }

    JFrame frame = new JFrame("Minaturia");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new BorderLayout());

    JLayeredPane economyPanel = new JLayeredPane();
economyPanel.setBackground(Color.BLACK);
economyPanel.setPreferredSize(new Dimension(2000, 1250));

JLayeredPane productionPanel = new JLayeredPane();
productionPanel.setBackground(Color.BLACK);
productionPanel.setPreferredSize(new Dimension(2000, 1250));

JLayeredPane researchPanel = new JLayeredPane();
researchPanel.setBackground(Color.BLACK);
researchPanel.setPreferredSize(new Dimension(2000, 1250));


    JPanel topPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(new Color(255, 255, 255, 25));
            for (int y = 0; y < getHeight(); y += 4) {
                g.drawLine(0, y, getWidth(), y);
            }
        }
    };
    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
    topPanel.setBackground(Color.BLACK);
    topPanel.setPreferredSize(new Dimension(1920, 100));

    JLabel countryNameLabel = new JLabel("COUNTRY: NONE SELECTED");
    JLabel capitalLabel = new JLabel("Capital: ");
    JLabel populationLabel = new JLabel("Population: ");

    JLabel[] labels = {countryNameLabel, capitalLabel, populationLabel};
    for (JLabel label : labels) {
        label.setFont(new Font("Monospaced", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.LEFT);
    }

    topPanel.add(countryNameLabel, BorderLayout.WEST);
    topPanel.add(capitalLabel, BorderLayout.WEST);
    topPanel.add(populationLabel, BorderLayout.WEST);
 
    JPanel buttonPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(new Color(255, 255, 255, 20));
            for (int y = 0; y < getHeight(); y += 4) {
                g.drawLine(0, y, getWidth(), y);
            }
        }
    };

    buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
    buttonPanel.setPreferredSize(new Dimension(1920, 20));
    buttonPanel.setBackground(Color.BLACK);

    Font buttonFont = new Font("Monospaced", Font.BOLD, 12);

    JButton mapButton = new JButton("Map");
    JButton economyButton = new JButton("Economy");
    JButton productionButton = new JButton("Production");
    JButton researchButton = new JButton("Research");


    JButton[] buttons = {mapButton, economyButton, productionButton, researchButton};
    for (JButton btn : buttons) {
        styleButton(btn, buttonFont);
        buttonPanel.add(btn);
    }

    topPanel.add(buttonPanel);
    frame.add(topPanel, BorderLayout.NORTH);

    JPanel infoPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(new Color(255, 255, 255, 20));
            for (int y = 0; y < getHeight(); y += 4) {
                g.drawLine(0, y, getWidth(), y);
            }
        }
    };
    infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
    infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    infoPanel.setPreferredSize(new Dimension(300, 980));
    infoPanel.setBackground(Color.BLACK);

    Font font = new Font("Monospaced", Font.PLAIN, 14);

    JTextField nameField = createBlinkingCaretField(10);
    JTextField languageField = createBlinkingCaretField(10);
    JTextField populationField = createBlinkingCaretField(10);
    JTextField terrainField = createBlinkingCaretField(10);
    JTextField tierField = createBlinkingCaretField(10);
    JTextField cityTypeField = createBlinkingCaretField(10);
    JTextField budget1Field = createBlinkingCaretField(10);
    JTextField budget2Field = createBlinkingCaretField(10);
    JButton updateButton = new JButton("Save Changes");

    JTextField[] fields = {
        nameField, languageField, populationField,
        terrainField, tierField, cityTypeField, budget1Field, budget2Field
    };

    for (JTextField field : fields) {
        styleTextField(field, font);
    }

    styleButton(updateButton, font);

        addField(infoPanel, "Name:", nameField, font);
        addField(infoPanel, "Language:", languageField, font);
        addField(infoPanel, "Population:", populationField, font);
        addField(infoPanel, "Terrain:", terrainField, font);
        addField(infoPanel, "Tier:", tierField, font);
        addField(infoPanel, "City Type:", cityTypeField, font);
        addField(infoPanel, "Food Production:", budget1Field, font);
        addField(infoPanel, "Fuel Production:", budget2Field, font);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(updateButton);

        // Setup layered pane for svg and overlays
        JLayeredPane svgWrapper = new JLayeredPane(); 
        svgWrapper.setPreferredSize(new Dimension(2000, 1250)); // your actual SVG size
        svgWrapper.add(svgCanvas, JLayeredPane.DEFAULT_LAYER);
        svgWrapper.setBackground(Color.BLACK);
        svgWrapper.setOpaque(true);
        svgWrapper.setBounds(0, 0, 2000, 1250);  // Add this!
        JPanel cards = new JPanel(new CardLayout());
        cards.setPreferredSize(new Dimension(2000, 1250));
        cards.add(svgWrapper, "Map");
        cards.add(economyPanel, "Economy");
        cards.add(productionPanel, "Production");
        cards.add(researchPanel, "Research");

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

        productionPanel.add(fuelSlider, JLayeredPane.PALETTE_LAYER);
        productionPanel.add(resourceSelect, JLayeredPane.PALETTE_LAYER);
        productionPanel.add(foodInput, JLayeredPane.PALETTE_LAYER);

        svgCanvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
        svgCanvas.setBackground(Color.BLACK);
        svgCanvas.setBounds(0, 0, 2000, 1250);
        AffineTransform at = AffineTransform.getScaleInstance(zoomFactor, zoomFactor);
        svgCanvas.setRenderingTransform(at, true);

        JScrollPane scrollPane = new JScrollPane(cards); 
        scrollPane.setViewportView(cards);
        scrollPane.setPreferredSize(new Dimension(1000, 625));
 
        scrollPane.setBackground(Color.BLACK);
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
    @Override
    protected void configureScrollBarColors() {
        this.thumbColor = Color.WHITE;
        this.trackColor = Color.BLACK;
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createInvisibleButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createInvisibleButton();
    }

    private JButton createInvisibleButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }
});

scrollPane.getHorizontalScrollBar().setUI(new BasicScrollBarUI() {
    @Override
    protected void configureScrollBarColors() {
        this.thumbColor = Color.WHITE;
        this.trackColor = Color.BLACK;
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createInvisibleButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createInvisibleButton();
    }

    private JButton createInvisibleButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }
});
scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 8));

       /*for (String svgFile : SvgFiles){
            File s = new File(dataDir, svgFile);
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
        } */
        File svgFile = new File(dataDir, "Minaturia Map.svg");
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
        Document parsedDoc = factory.createDocument(svgFile.toURI().toString());
        svgCanvas.setDocument(parsedDoc);
        // Initially show map
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
            Province province = provinceList.get(Integer.parseInt(id) - 1);
            String encodedID = province.getCountry().replace("'", "");  // escape apostrophes
            encodedID = encodedID.replaceAll("\\s+", "-"); 
            el.setAttribute("style", "fill: url(#" + encodedID + ");");

            System.out.println("fill:(#" + province.getCountry() + ");stroke:white;stroke-width:1;");

        }

        mapButton.addActionListener(e -> {
            CardLayout c = (CardLayout)cards.getLayout();
            c.show(cards, "Map");
        });
        economyButton.addActionListener(e -> {
            CardLayout c = (CardLayout)cards.getLayout();
            c.show(cards, "Economy");
        });
        productionButton.addActionListener(e -> {
            CardLayout c = (CardLayout)cards.getLayout();
            c.show(cards, "Production");
        });
        researchButton.addActionListener(e -> {
            CardLayout c = (CardLayout)cards.getLayout();
            c.show(cards, "Research");
        });

        foodInput.addActionListener(e -> {
            if (currentProvince == null || currentProvince.getCountry() == null) {
                JOptionPane.showMessageDialog(svgCanvas, "No province selected.");
            } else {
                updateInventoryForCountry(currentProvince.getCountry());
                try {
                    ProvinceParser.writeInventoryToCSV(getCountryByName(currentProvince.getCountry()), new File(dataDir, currentProvince.getCountry() + " Inventory.csv").getAbsolutePath());

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
                                    String encodedID = currentProvince.getCountry().replace("'", "");  // escape apostrophes
                                    encodedID = encodedID.replaceAll("\\s+", "-");  
                                    el.setAttribute("style", "fill: url(#" + encodedID + ");");
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
                                    String encodedID = provinceList.get(Integer.parseInt(selectedElement.getAttribute("id"))-1).getCountry().replace("'", "");  // escape apostrophes
                                    encodedID = encodedID.replaceAll("\\s+", "-");  
                                    selectedElement.setAttribute("style", "fill: url(#" + encodedID + ");");
                                }

                                el.setAttribute("style", "fill:white;stroke:white;stroke-width:1;");
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
        frame.add(scrollPane, BorderLayout.CENTER);

        // *** Make frame resizable ***
        frame.setResizable(true);

        // *** Add component listener to reposition overlays on resize ***
        cards.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {

                Dimension size = scrollPane.getViewport().getExtentSize();

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
                System.out.println("layeredPane preferred size: " + cards.getPreferredSize());
                System.out.println("scroll view size: " + scrollPane.getViewport().getViewSize());
                // When zoom changes or size changes:

                svgCanvas.revalidate();
                svgCanvas.repaint();
                svgWrapper.revalidate();
                svgWrapper.repaint();
                scrollPane.revalidate();
                scrollPane.repaint();
            }
        });

svgCanvas.addMouseWheelListener(new MouseWheelListener() {
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
        if (notches < 0) {
            zoomFactor = Math.min(zoomFactor + zoomStep, zoomMax);
        } else {
            zoomFactor = Math.max(zoomFactor - zoomStep, zoomMin);
        }

        Point mousePoint = e.getPoint();
        AffineTransform currentTransform = svgCanvas.getRenderingTransform();

        try {
            Point2D mouseInUserSpace = currentTransform.inverseTransform(mousePoint, null);
            AffineTransform newTransform = new AffineTransform();
            newTransform.translate(mousePoint.getX(), mousePoint.getY());
            newTransform.scale(zoomFactor, zoomFactor);
            newTransform.translate(-mouseInUserSpace.getX(), -mouseInUserSpace.getY());

            svgCanvas.setRenderingTransform(newTransform, true);

            // DO NOT resize components here

            svgCanvas.revalidate();
            svgCanvas.repaint();

            scrollPane.revalidate();
            scrollPane.repaint();

        } catch (NoninvertibleTransformException ex) {
            ex.printStackTrace();
        }
    }
});
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        System.out.println("svgWrapper size: " + svgWrapper.getSize());

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
        int startX = 580;
        int startY = 420;
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
            if (startY > ((Integer.parseInt(svgDoc.getElementById("inventoryBox").getAttribute("height")) + Integer.parseInt(svgDoc.getElementById("inventoryBox").getAttribute("y")))-15)){
                svgDoc.getElementById("inventoryBox").setAttribute("height", (Integer.parseInt(svgDoc.getElementById("inventoryBox").getAttribute("height"))+40) + "");
            }
        }

        svgCanvas.repaint();
    }

private void styleButton(JButton button, Font font) {
    button.setFont(font);
    button.setBackground(Color.BLACK);
    button.setForeground(Color.WHITE);
    button.setFocusPainted(false);
    button.setBorder(BorderFactory.createLineBorder(Color.WHITE));
}

private void styleTextField(JTextField field, Font font) {
    field.setFont(font);
    field.setBackground(Color.BLACK);
    field.setForeground(Color.WHITE);
    field.setCaretColor(Color.WHITE);
    field.setBorder(BorderFactory.createLineBorder(Color.WHITE));
}

private JTextField createBlinkingCaretField(int columns) {
    JTextField field = new JTextField(columns);
    Caret caret = new DefaultCaret() {
        @Override
        protected synchronized void damage(Rectangle r) {
            if (r == null) return;
            x = r.x;
            y = r.y;
            width = 1;
            height = r.height;
            repaint();
        }
    };
    caret.setBlinkRate(500);
    field.setCaret(caret);
    return field;
}

private void showStyledDialog(String message) {
    JTextArea textArea = new JTextArea(message);
    textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
    textArea.setBackground(Color.BLACK);
    textArea.setForeground(Color.WHITE);
    textArea.setEditable(false);
    textArea.setBorder(BorderFactory.createLineBorder(Color.WHITE));
    JOptionPane.showMessageDialog(null, textArea);
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
