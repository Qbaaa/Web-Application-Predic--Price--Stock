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
import java.io.FileOutputStream;
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

    public String optimizationNN(String structure, String conv1, String conv2, String dense1, String initMode,
                               String activation, String optimizer, String batchSize, String epoche)
    {
        HttpResponse optimizationCCN = Unirest.get("http://127.0.0.1:5000//configuration?structure=" + structure +
                "&conv1=" + conv1 + "&conv2=" + conv2 + "&dense1=" + dense1 + "&initMode=" + initMode +
                "&activation=" + activation + "&optimizer=" + optimizer + "&batchSize=" + batchSize + "&epoche=" + epoche + "&key=" + key).asJson();

        StringBuilder metric = new StringBuilder();
        StringBuilder configuration = new StringBuilder();

        JSONObject jsonBody = new JSONObject(optimizationCCN.getBody().toString());
        JSONArray jsonLoss = jsonBody.getJSONArray("Loss");
        for (int i = 0; i < jsonLoss.length(); i++) {
            JSONObject loss = new JSONObject(jsonLoss.get(i).toString());
            String lossTrain = loss.getString("Train");
            String lossVal = loss.getString("Val");
            metric.append("Loss \nTrain: ").append(lossTrain).append("\nVal: ").append(lossVal).append('\n');
        }

        JSONArray jsonMae = jsonBody.getJSONArray("MAE");
        for (int i = 0; i < jsonMae.length(); i++) {
            JSONObject mae = new JSONObject(jsonMae.get(i).toString());
            String maeTrain = mae.getString("Train");
            String maeVal = mae.getString("Val");
            metric.append("MAE \nTrain: ").append(maeTrain).append("\nVal: ").append(maeVal).append('\n');
        }

        JSONArray jsonMape = jsonBody.getJSONArray("MAPE");
        for (int i = 0; i < jsonMape.length(); i++) {
            JSONObject mape = new JSONObject(jsonMape.get(i).toString());
            String mapeTrain = mape.getString("Train");
            String mapeVal = mape.getString("Val");
            metric.append("MAPE \nTrain: ").append(mapeTrain).append("\nVal: ").append(mapeVal).append('\n');
        }


        configuration.append("Structure: ").append(structure).append(";Conv1: ").append(conv1).append(";Conv2: ").append(conv2).
                append(";Dense1: ").append(dense1).append(";initMode1: ").append(initMode).append(";Actywation: ").append(activation).
                append(";optimizer: ").append(optimizer).append(";batchSize1: ").append(batchSize).append(";epoche: ").append(epoche).append("\n");

        try (PrintWriter writer = new PrintWriter(new FileOutputStream(new File("optimizer.csv"), true ))){
            writer.write(configuration.toString());
            writer.append(metric.toString());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return metric.toString();
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
        double MAPE = 0;

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
            MAPE =  MAPE + Math.abs(different) / tempActualPrice;
            sb.append(different);
            sb.append('\n');
        }

        MSE = MSE / historialPredictResponse.getPricePredicTest().size();
        MSE = round(MSE, 4);
        MAE = MAE / historialPredictResponse.getPricePredicTest().size();
        MAE = round(MAE, 4);
        MAPE = (MAPE / historialPredictResponse.getPricePredicTest().size()) * 100;

        sb.append("\nMSE= ").append(MSE).append("\nMAE= ").append(MAE).append("\nMAPE= ").append(MAPE);

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
