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
  structures = ["Input-Conv1D-MaxPooling1D-Flatten-Dense-Dense", "Input-Conv1D-MaxPooling1D-Conv1D-MaxPooling1D-Flatten-Dense-Dense"];
  selectStructure = this.structures[0];
  conv1 = [8,16,32,64,128];
  selectConv1 = this.conv1[4];
  conv2 = [8,16,32,64,128];
  selectConv2 = this.conv2[3];
  dense1 = [8,16,32,64,128];
  selectDense1 = this.dense1[3];
  init_mode = ["uniform", "normal" , "zero", "he_normal", "he_uniform", "glorot_normal", "glorot_uniform", "lecun_normal", "lecun_uniform"];
  selectInitMode = this.init_mode[0];
  activations = ["relu", "tanh", "sigmoid", "softmax", "linear"];
  selectActivation = this.activations[0];
  optimizers = ["adam", "SGD", "RMSprop", "Adamax", "Adagrad"];
  selectOptimizer = this.optimizers[0];
  batch_size = [1, 2, 4, 8, 16, 32, 64, 128];
  selectBatchSize = this.batch_size[7];
  epoches = [50, 100, 150];
  selectEpoche = this.epoches[1];
  loading = false;

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

  optimization() {
    this.loading = true;

    this.adminService.optimization(this.selectStructure, this.selectConv1, this.selectConv2, this.selectDense1,
      this.selectInitMode, this.selectActivation, this.selectOptimizer, this.selectBatchSize, this.selectEpoche)
      .subscribe(
      data => {
        alert(data.message);
        this.loading = false;
      },
      err => {
        console.log(err.error.message);
        this.loading = false;
      }
    );
  }

}


