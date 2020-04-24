import { Component, OnInit } from '@angular/core';
import { TokenStorageService } from "../../services/token-storage.service";
import { UserService } from "../../services/user.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-profil',
  templateUrl: './profil.component.html',
  styleUrls: ['./profil.component.css']
})
export class ProfilComponent implements OnInit {
  currentUser: any;
  showUser = false;
  showAdmin = false;
  errorMessage = '';

  constructor(private token: TokenStorageService, private userService: UserService) {
  }

  ngOnInit() {
    if(!!this.token.getToken()){
      this.currentUser = this.token.getUser();

      this.showAdmin = this.currentUser.roles.includes("ROLE_ADMIN");
      this.showUser = this.currentUser.roles.includes("ROLE_USER");
    }

  }

  deleteUser() {
    if (confirm("Czy jesteś pewien, że chcesz usunąć konto?")) {
      alert("Twoje konto zostanie ustunięte.");
      this.userService.deleteUser(this.currentUser.email).subscribe(
        data => {
          this.errorMessage = data.message;
          alert(this.errorMessage);
          this.token.loginOut();
          window.location.reload();
        },
        err => {
          this.errorMessage = err.error.message;
          alert(this.errorMessage);
        }
        );
    }
  }


}
