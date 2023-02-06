package com.rbc.stock.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@Setter

public class Stock {
    public Stock(String quarter, String stock, String date, String open, String high, String low, String close, String volume, String percent_change_price, String percent_change_volume_over_last_wk, String previous_weeks_volume, String next_weeks_open, String next_weeks_close, String percent_change_next_weeks_price, String days_to_next_dividend, String percent_return_next_dividend) {
        this.quarter = quarter;
        this.stock = stock;
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.percent_change_price = percent_change_price;
        this.percent_change_volume_over_last_wk = percent_change_volume_over_last_wk;
        this.previous_weeks_volume = previous_weeks_volume;
        this.next_weeks_open = next_weeks_open;
        this.next_weeks_close = next_weeks_close;
        this.percent_change_next_weeks_price = percent_change_next_weeks_price;
        this.days_to_next_dividend = days_to_next_dividend;
        this.percent_return_next_dividend = percent_return_next_dividend;
    }

    String quarter,stock,date,open,high,low,close,volume,percent_change_price,percent_change_volume_over_last_wk,previous_weeks_volume,next_weeks_open,next_weeks_close,percent_change_next_weeks_price,days_to_next_dividend,percent_return_next_dividend;

public Stock(String record){

    String[] data = record.split(",");
    Arrays.parallelSetAll(data, i -> data[i].trim());

    this.quarter = Optional.of(data[0]).isEmpty() ? "" : data[0];
    this.stock =  Optional.of(data[1]).isEmpty() ? "" : data[1];
    this.date =  Optional.of(data[2]).isEmpty() ? "" : data[2];
    this.open =  Optional.of(data[3]).isEmpty() ? "" : data[3];
    this.high = Optional.of(data[4]).isEmpty() ? "" : data[4];
    this.low =  Optional.of(data[5]).isEmpty() ? "" : data[5];
    this.close =  Optional.of(data[6]).isEmpty() ? "" : data[6];
    this.volume =  Optional.of(data[7]).isEmpty() ? "" : data[7];
    this.percent_change_price = Optional.of(data[8]).isEmpty() ? "" : data[8];
    this.percent_change_volume_over_last_wk =  Optional.of(data[9]).isEmpty() ? "" : data[9];
    this.previous_weeks_volume = Optional.of(data[10]).isEmpty() ? "" : data[10];
    this.next_weeks_open =  Optional.of(data[11]).isEmpty() ? "" : data[11];
    this.next_weeks_close =  Optional.of(data[12]).isEmpty() ? "" : data[12];
    this.percent_change_next_weeks_price = Optional.of(data[13]).isEmpty() ? "" : data[13];
    this.days_to_next_dividend =  Optional.of(data[14]).isEmpty() ? "" : data[14];
    this.percent_return_next_dividend =  Optional.of(data[15]).isEmpty() ? "" : data[15];

}
    public Stock() {
    }


}
