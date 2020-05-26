package com.qbaaa.stockpricepredict.service;

import com.qbaaa.stockpricepredict.models.Company;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApiFinancial {

    @Value("${ApiFinancial.apikey}")
    private String apiKey;

    public List<Company> ReadingHistorialPriceStock(String symbolStock, LocalDate dateStart, LocalDate dateEnd)
    {

        List<Company> records = new ArrayList<>();

        HttpResponse historialDataPriceStock = Unirest.get("https://financialmodelingprep.com/api/v3/historical-price-full/"
                + symbolStock + "?from=" + dateStart + "&to=" + dateEnd + "&apikey=" + apiKey).asJson();

        JSONObject jsonData = new JSONObject(historialDataPriceStock.getBody().toString());

        JSONArray jsonHistorical = jsonData.getJSONArray("historical");

        for (int i = 0; i < jsonHistorical.length(); i++)
        {
            JSONObject dateStock = new JSONObject(jsonHistorical.get(i).toString());

            String date = dateStock.getString("date");
            LocalDate localDate = LocalDate.parse(date);

            Double priceClose = dateStock.getDouble("close");

            Company tempStock = new Company(symbolStock,localDate, priceClose);
            records.add(tempStock);

        }

        return records;
    }

}
