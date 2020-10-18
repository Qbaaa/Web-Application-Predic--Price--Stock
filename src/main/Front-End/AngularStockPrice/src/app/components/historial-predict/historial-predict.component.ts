import { Component, OnInit } from '@angular/core';
import { AppComponent } from "../../app.component";
import { UserService } from "../../services/user.service";
import * as Highcharts from 'highcharts';

@Component({
  selector: 'app-historial-predict',
  templateUrl: './historial-predict.component.html',
  styleUrls: ['./historial-predict.component.css']
})
export class HistorialPredictComponent implements OnInit {
  window_size = [5,8,10];

  companies = [
    {
      company: 'Apple',
      symbol: 'AAPL'
    },
    {
      company: 'Microsoft',
      symbol: 'MSFT'
    },
    {
      company: 'Amazon',
      symbol: 'AMZN'
    },
    {
      company: 'Tesla',
      symbol: 'TSLA'
    },
    {
      company: 'Intel',
      symbol: 'INTC'
    },
    {
      company: 'HP Inc.',
      symbol: 'HPQ'
    },
    {
      company: 'Oracle',
      symbol: 'ORCL'
    },
    {
      company: 'Advanced Micro Devices',
      symbol: 'AMD'
    },
    {
      company: 'eBay',
      symbol: 'EBAY'
    },
    {
      company: 'Visa',
      symbol: 'V'
    },
    {
      company: 'Nvidia',
      symbol: 'NVDA'
    }
  ];
  loading = false;
  pressedAnalyze = false;
  selectCompany = this.companies[0].symbol;
  selectWindowSize = this.window_size[0];
  highcharts = Highcharts;
  updateFlag = false;
  chartOptions = {
    chart: {
      type: "spline",
      zoomType: 'x',
    },
    title: {
      text: "Stock Price"
    },
    subtitle: {
      text: ""
    },
    xAxis:{
      title:{
        text: "Data"
      },
      categories: []
    },
    yAxis: {
      title:{
        text:"Cenna Zamknięcia"
      }
    },
    tooltip: {
      crosshairs: [true,true],
      shared: true,
      valueSuffix:" USD"
    },
    series: [
      {
        name: 'Przewidywania',
        marker: {
          symbol: 'circle'
        },
        color: '#FF0000',
        data: []
      },
      {
        name: 'Rzeczywiste',
        marker: {
          symbol: 'circle'
        },
        color: '#0000FF',
        data: []
      }
    ]
  };

  constructor(private userService: UserService, private appComponent: AppComponent) { }

  ngOnInit() {
  }

  analyze(){
    this.loading = true;

    this.userService.historialPriceStock(this.selectCompany, this.selectWindowSize).subscribe(
      data => {
        this.pressedAnalyze = true;

        this.chartOptions.subtitle = {
          text: "HistoricalPriceCompanies: " + this.selectCompany
        };

        this.chartOptions.xAxis = {
          title:{
            text: "Data"
          },
          categories: data.datesTest
        };

        this.chartOptions.series = [
          {
            name: 'Przewidywania',
            marker: {
              symbol: 'circle'
            },
            color: '#FF0000',
            data: data.pricePredicTest
          },
          {
            name: 'Rzeczywiste',
            marker: {
              symbol: 'circle'
            },
            color: '#0000FF',
            data: data.pricesActualTest
          }
        ];

        this.updateFlag = true;
        this.loading = false;
      },
      err => {
        console.log(err.error.message);
        this.pressedAnalyze = false;
        this.loading = false;
      }
    );
  }

}
