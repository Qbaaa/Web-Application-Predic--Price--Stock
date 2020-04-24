import {Component, OnInit} from '@angular/core';
import {TokenStorageService} from "./services/token-storage.service";

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

  constructor(private tokenStorage: TokenStorageService) {}

  ngOnInit() {
    this.isLoggedIn = !!this.tokenStorage.getToken();

    if(this.isLoggedIn){
      const user = this.tokenStorage.getUser();
      this.roles = user.roles;

      this.showAdmin = this.roles.includes("ROLE_ADMIN");
      this.showUser = this.roles.includes("ROLE_USER");

      this.username = user.username;
    }
  }

  loginOut(){
    this.tokenStorage.loginOut();
    window.location.reload();
  }

}
