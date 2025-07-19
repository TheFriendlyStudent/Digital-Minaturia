package org.whogames.digitalminaturia;

public class Province {

    private int id;
    private String name;
    private String country;
    private String language;
    private int population;
    private String terrain;
    private int tier;
    private String cityType;
    private long budget1;
    private long budget2;

    public Province(int id, String name, String country, String language, int population,
            String terrain, int tier, String cityType, long budget1, long budget2) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.language = language;
        this.population = population;
        this.terrain = terrain;
        this.tier = tier;
        this.cityType = cityType;
        this.budget1 = budget1;
        this.budget2 = budget2;
    }

    // Getters (optional)
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getLanguage() {
        return language;
    }

    public int getPopulation() {
        return population;
    }

    public String getTerrain() {
        return terrain;
    }

    public int getTier() {
        return tier;
    }

    public String getCityType() {
        return cityType;
    }

    public long getBudget1() {
        return budget1;
    }

    public long getBudget2() {
        return budget2;
    }

    // Setters (optional)
    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public void setTerrain(String terrain) {
        this.terrain = terrain;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public void setCityType(String cityType) {
        this.cityType = cityType;
    }

    public void setBudget1(long budget1) {
        this.budget1 = budget1;
    }

    public void setBudget2(long budget2) {
        this.budget2 = budget2;
    }

    // toString for debugging (optional)
    @Override
    public String toString() {
        return String.format("Province{id=%d, name='%s', country='%s', language='%s', population=%d, terrain='%s', tier=%d, cityType='%s', budget1=%d, budget2=%d}",
                id, name, country, language, population, terrain, tier, cityType, budget1, budget2);
    }
}
