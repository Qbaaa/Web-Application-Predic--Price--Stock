package com.qbaaa.stockpricepredict.service;

import com.qbaaa.stockpricepredict.response.CompanyResponse;
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
public class PredictPriceStock {

    @Value("${PredictPriceStock.key}")
    private String key;

    public CompanyResponse predictPriceStockOneDays(String symbolStock){
        LocalDate dateStart = LocalDate.now().minusDays(1).minusYears(5);
        LocalDate dateEnd = LocalDate.now();

        if(dateEnd.getDayOfWeek().getValue() == 6)
            dateEnd = dateEnd.plusDays(2);
        else if(dateEnd.getDayOfWeek().getValue() == 7)
            dateEnd = dateEnd.plusDays(1);

        HttpResponse predictDataPriceStock = Unirest.get("http://127.0.0.1:5000//stock?symbolStock="
                + symbolStock + "&dateStart=" + dateStart + "&dateEnd=" + dateEnd + "&key=" + key).asJson();

        JSONObject jsonBody = new JSONObject(predictDataPriceStock.getBody().toString());

        JSONArray jsonTrainData = jsonBody.getJSONArray("trainData");

        List<LocalDate> tempDate = new ArrayList<>();
        List<Double> tempPriceActual = new ArrayList<>();
        List<Double> tempPricePredict = new ArrayList<>();

        for (int i = 0; i < jsonTrainData.length(); i++)
        {
            JSONObject dateStock = new JSONObject(jsonTrainData.get(i).toString());
            String date = dateStock.getString("date");
            LocalDate localDate = LocalDate.parse(date);
            Double priceCloseOriginal = dateStock.getDouble("originalPrice");
            Double priceClosePredic = dateStock.getDouble("predictPrice");

            tempDate.add(localDate);
            tempPriceActual.add(priceCloseOriginal);
            tempPricePredict.add(priceClosePredic);
        }

        JSONArray jsonPredictNextDay = jsonBody.getJSONArray("predictNextDay");

        LocalDate localDate = null;
        Double priceClose = null;

        for (int i = 0; i < jsonPredictNextDay.length(); i++)
        {
            JSONObject dateStock = new JSONObject(jsonPredictNextDay.get(i).toString());
            String date = dateStock.getString("date");
            localDate = LocalDate.parse(date);
            priceClose = dateStock.getDouble("price");
        }

        return new CompanyResponse(tempDate,tempPriceActual,tempPricePredict, localDate, priceClose);
    }
}
