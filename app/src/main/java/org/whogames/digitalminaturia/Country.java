package org.whogames.digitalminaturia;

import java.util.ArrayList;
import java.util.HashMap;

public class Country {

    private String name;
    private ArrayList<Province> provinces;
    private HashMap<Entity, Integer> inventory = new HashMap<>();
    private ArrayList<Squad> squads = new ArrayList<>();

    // Added fields from CSV
    private String capital;
    private int population;
    private int cities;
    private int universities;
    private double gdp;
    private double taxRate;
    private double gdpGrowth;
    private long baseBudget;
    private double debtRatio;
    private long militarySpending;
    private double inflationRate;
    private double gdpPerCapita;
    private double incomePerCapita;
    private double medianIncome;
    private double happiness;

    public Country(String name) {
        this.name = name;
        this.provinces = new ArrayList<>();
    }

    public int sumOfProvincesPopulation() {
        int totalPopulation = 0;
        for (Province province : provinces) {
            totalPopulation += province.getPopulation();
        }
        return totalPopulation;
    }

    public Country(String name, String capital, int population, int cities, int universities, double gdp, double taxRate, double gdpGrowth, long baseBudget, double debtRatio, long militarySpending, double inflationRate, double gdpPerCapita, double incomePerCapita, double medianIncome, double happiness) {
        this.name = name;
        this.capital = capital;
        this.population = population;
        this.cities = cities;
        this.universities = universities;
        this.gdp = gdp;
        this.taxRate = taxRate;
        this.gdpGrowth = gdpGrowth;
        this.baseBudget = baseBudget;
        this.debtRatio = debtRatio;
        this.militarySpending = militarySpending;
        this.inflationRate = inflationRate;
        this.gdpPerCapita = gdpPerCapita;
        this.incomePerCapita = incomePerCapita;
        this.medianIncome = medianIncome;
        this.happiness = happiness;
        this.provinces = new ArrayList<>();
    }

    public void addProvince(Province province) {
        provinces.add(province);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Province> getProvinces() {
        return provinces;
    }

    // --- Getters and setters for new fields ---
    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public int getCities() {
        return cities;
    }

    public void setCities(int cities) {
        this.cities = cities;
    }

    public int getUniversities() {
        return universities;
    }

    public void setUniversities(int universities) {
        this.universities = universities;
    }

    public double getGdp() {
        return gdp;
    }

    public void setGdp(double gdp) {
        this.gdp = gdp;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public double getGdpGrowth() {
        return gdpGrowth;
    }

    public void setGdpGrowth(double gdpGrowth) {
        this.gdpGrowth = gdpGrowth;
    }

    public long getBaseBudget() {
        return baseBudget;
    }

    public void setBaseBudget(long baseBudget) {
        this.baseBudget = baseBudget;
    }

    public double getDebtRatio() {
        return debtRatio;
    }

    public void setDebtRatio(double debtRatio) {
        this.debtRatio = debtRatio;
    }

    public long getMilitarySpending() {
        return militarySpending;
    }

    public void setMilitarySpending(long militarySpending) {
        this.militarySpending = militarySpending;
    }

    public double getInflationRate() {
        return inflationRate;
    }

    public void setInflationRate(double inflationRate) {
        this.inflationRate = inflationRate;
    }

    public double getGdpPerCapita() {
        return gdpPerCapita;
    }

    public void setGdpPerCapita(double gdpPerCapita) {
        this.gdpPerCapita = gdpPerCapita;
    }

    public double getIncomePerCapita() {
        return incomePerCapita;
    }

    public void setIncomePerCapita(double incomePerCapita) {
        this.incomePerCapita = incomePerCapita;
    }

    public double getMedianIncome() {
        return medianIncome;
    }

    public void setMedianIncome(double medianIncome) {
        this.medianIncome = medianIncome;
    }

    public double getHappiness() {
        return happiness;
    }

    public void setHappiness(double happiness) {
        this.happiness = happiness;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name + ":\n");
        for (Province p : provinces) {
            sb.append("  - ").append(p.toString()).append("\n");
        }
        return sb.toString();
    }

    public HashMap<Entity, Integer> getInventory() {
        return inventory;
    }

    public void setInventory(HashMap<Entity, Integer> inventory) {
        this.inventory = inventory;
    }

    public ArrayList<Squad> getSquads() {
        return squads;
    }

    public void setSquads(ArrayList<Squad> squads) {
        this.squads = squads;
    }

    public void setProvinces(ArrayList<Province> provinces) {
        this.provinces = provinces;
    }

    public void setName(String name) {
        this.name = name;
    }
}
