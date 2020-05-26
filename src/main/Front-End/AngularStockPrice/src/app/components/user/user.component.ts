import { Component, OnInit } from '@angular/core';
import { AppComponent } from "../../app.component";
import { UserService } from "../../services/user.service";
import * as Highcharts from 'highcharts';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
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

  dateNextDay;
  priceNextDay;

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
    console.log(this.selectCompany);
    this.loading = true;

    this.userService.predictPriceStock(this.selectCompany).subscribe(
      data => {
        this.pressedAnalyze = true;

        this.chartOptions.subtitle = {
          text: "Company: " + this.selectCompany
        };

        this.chartOptions.xAxis = {
          title:{
            text: "Data"
          },
          categories: data.datesTrain
        };

        this.chartOptions.series = [
          {
            name: 'Przewidywania',
            marker: {
              symbol: 'circle'
            },
            color: '#FF0000',
            data: data.pricePredicTrain
          },
          {
            name: 'Rzeczywiste',
            marker: {
              symbol: 'circle'
            },
            color: '#0000FF',
            data: data.pricesActualTrain
          }
        ];

        this.updateFlag = true;
        this.dateNextDay = data.dateNextDay;
        this.priceNextDay = data.priceNextDay


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
