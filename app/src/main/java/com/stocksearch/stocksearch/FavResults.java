package com.stocksearch.stocksearch;

/**
 * Created by kavya on 5/2/2016.
 */
public class FavResults {
    private String symbol = "";
    private String lp = "";
    private String cp = "";
    private String name = "";
    private String mc = "";

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setLP(String lp) {
        this.lp = "$ "+lp;
    }

    public String getLP() {
        return lp;
    }
    public void setCP(String cp) {
        if(!cp.contains("-")){
            cp = '+'+cp;
        }
        this.cp = cp + '%';
    }

    public String getCP() {
        return cp;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setMC(String mc) {
        this.mc = "Market Cap : "+mc;
    }

    public String getMC() {
        return mc;
    }
}
