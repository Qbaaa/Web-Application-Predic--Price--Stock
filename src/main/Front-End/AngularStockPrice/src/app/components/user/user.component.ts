import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
  companies = [
    {
      company: 'Google',
      symbol: 'GOOGL'
    },
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
      company: 'Cisco',
      symbol: 'CSCO'
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

  selectCompany = this.companies[0].symbol;

  constructor() { }

  ngOnInit() {
  }

  analyze(){

  }

}
