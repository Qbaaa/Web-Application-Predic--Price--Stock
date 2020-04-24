import { Component, OnInit } from '@angular/core';
import {AdminService} from "../../services/admin.service";
import { AppComponent } from "../../app.component";
import { UserResponse } from "../../response/user-response";

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  errorMessage = '';
  users : UserResponse[];
  selectUser;

  constructor(private adminService: AdminService, private appComponent: AppComponent) { }

  ngOnInit() {
    this.adminService.getUser().subscribe(
      data =>{
        this.users = data;
        this.selectUser = this.users[0].email;
      },
      err=>{
        this.errorMessage = err.error.message;
      }
    );
  }

  deleteUser(){
    this.adminService.deleteUser(this.selectUser).subscribe(
      data => {
        this.errorMessage = data.message;
        alert(this.errorMessage);
        window.location.reload();
      },
      err => {
        this.errorMessage = err.error.message;
        alert(this.errorMessage);
      }
    );
  }

}
