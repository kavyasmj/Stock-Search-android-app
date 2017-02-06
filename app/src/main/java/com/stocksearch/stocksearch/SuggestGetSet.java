package com.stocksearch.stocksearch;

/**
 * Created by kavya on 5/4/2016.
 */
public class SuggestGetSet {

    String symbol,name,exchange;
    public SuggestGetSet(String symbol, String name, String exchange){
        this.setSymbol(symbol);
        this.setName(name);
        this.setExchange(exchange);
    }
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
