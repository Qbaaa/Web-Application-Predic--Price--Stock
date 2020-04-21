import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  form: any = {};
  isSuccessful = false;
  isRegisterFailed = false;
  errorMessage = '';

  constructor(private authService: AuthService) { }

  ngOnInit() {
  }

  onSubmit() {
    this.form.role = [ 'user' ];
    this.authService.register(this.form).subscribe(
      data => {
        this.isSuccessful = true;
        this.isRegisterFailed = false;
      },
      err => {
        this.errorMessage = err.error.message;
        this.isRegisterFailed = true;
      }
    );

  }

}
