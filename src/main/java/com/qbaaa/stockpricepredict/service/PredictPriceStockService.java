package com.qbaaa.stockpricepredict.service;

import com.qbaaa.stockpricepredict.models.PredictPriceCompanies;
import com.qbaaa.stockpricepredict.repository.HistoricalPriceCompaniesRepository;
import com.qbaaa.stockpricepredict.repository.PredictPriceCompaniesRepository;
import com.qbaaa.stockpricepredict.response.CompanyResponse;
import com.qbaaa.stockpricepredict.response.HistorialPredictResponse;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PredictPriceStockService {

    @Value("${PredictPriceStock.key}")
    private String key;

    @Autowired
    private PredictPriceCompaniesRepository predictPriceCompaniesRepository;

    @Autowired
    private HistoricalPriceCompaniesRepository historicalPriceCompaniesRepository;

    public CompanyResponse predictPriceStockOneDays(String symbolStock, String windowSize) {
        LocalDate dateStart = LocalDate.now().minusDays(1).minusYears(5);
        LocalDate dateEnd = LocalDate.now();

        if (dateEnd.getDayOfWeek().getValue() == 6)
            dateEnd = dateEnd.plusDays(2);
        else if (dateEnd.getDayOfWeek().getValue() == 7)
            dateEnd = dateEnd.plusDays(1);

        HttpResponse predictDataPriceStock = Unirest.get("http://127.0.0.1:5000//stock?symbolStock=" + symbolStock +
                "&windowSize=" + windowSize + "&dateStart=" + dateStart + "&dateEnd=" + dateEnd + "&key=" + key).asJson();

        JSONObject jsonBody = new JSONObject(predictDataPriceStock.getBody().toString());

        JSONArray jsonTrainData = jsonBody.getJSONArray("trainData");

        List<LocalDate> tempDate = new ArrayList<>();
        List<Double> tempPriceActual = new ArrayList<>();
        List<Double> tempPricePredict = new ArrayList<>();

        for (int i = 0; i < jsonTrainData.length(); i++) {
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

        for (int i = 0; i < jsonPredictNextDay.length(); i++) {
            JSONObject dateStock = new JSONObject(jsonPredictNextDay.get(i).toString());
            String date = dateStock.getString("date");
            localDate = LocalDate.parse(date);
            priceClose = dateStock.getDouble("price");
        }

        predictPriceCompaniesRepository.save(new PredictPriceCompanies(symbolStock, windowSize, localDate, priceClose));

        return new CompanyResponse(tempDate, tempPriceActual, tempPricePredict, localDate, priceClose);
    }

    public HistorialPredictResponse historialPredictPriceStock(String symbolStock, String windowSize) {

        StringBuilder sb = new StringBuilder();

        sb.append("Przewidywanie dla ").append(symbolStock).append(" z rozmiarem okna ").append(windowSize).append('\n');
        sb.append("Data;Rzeczywista cena;Przewidywana cena;Blad prognozowania\n");

        HistorialPredictResponse historialPredictResponse = new HistorialPredictResponse();
        historialPredictResponse.setSymbol(symbolStock);
        historialPredictResponse.setWindowSize(windowSize);

        List<PredictPriceCompanies> historialPredict = predictPriceCompaniesRepository.findBySymbolAndWindowSize(symbolStock, windowSize);

        double MSE = 0;
        double MAE = 0;

        for (PredictPriceCompanies ele : historialPredict) {
            historialPredictResponse.addDatesTest(ele.getDate());

            sb.append(ele.getDate().toString());
            sb.append(';');

            Double tempActualPrice = (historicalPriceCompaniesRepository.findBySymbolAndDate(symbolStock, ele.getDate())).getPriceClose();
            historialPredictResponse.addPricesActualTest(tempActualPrice);

            sb.append(tempActualPrice);
            sb.append(';');

            historialPredictResponse.addPricePredicTest(ele.getPriceClose());

            sb.append(ele.getPriceClose());
            sb.append(';');

            double different = tempActualPrice - ele.getPriceClose();

            different = round(different, 2);
            MSE = MSE + Math.pow(different,2);
            MAE = MAE + Math.abs(different);
            sb.append(different);
            sb.append('\n');
        }

        MSE = MSE / historialPredictResponse.getPricePredicTest().size();
        MSE = round(MSE, 4);
        MAE = MAE / historialPredictResponse.getPricePredicTest().size();
        MAE = round(MAE, 4);

        sb.append("\nMSE= ").append(MSE).append("\n").append("MAE= ").append(MAE);

        String fileName = symbolStock + "_" + windowSize + ".csv";

        try (PrintWriter writer = new PrintWriter(new File(fileName))) {

            writer.write(sb.toString());

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return historialPredictResponse;
    }

    public double round(double ele, double pow)
    {
        ele *= Math.pow(10, pow);
        ele = Math.round(ele);
        ele /= Math.pow(10, pow);

        return ele;
    }
}
