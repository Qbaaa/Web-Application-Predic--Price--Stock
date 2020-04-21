import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  title = 'StockPricePredict';
  private roles: string[];
  isLoggedIn = false;
  showUser = false;
  showAdmin = false;
  username: string;

  ngOnInit() {
  }
}
