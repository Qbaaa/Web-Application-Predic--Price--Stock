import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { RegisterComponent } from './components/register/register.component';
import { LoginComponent } from './components/login/login.component';
import { ProfilComponent } from "./components/profil/profil.component";
import { AdminComponent } from "./components/admin/admin.component";
import { UserComponent } from "./components/user/user.component";
import {HistorialPredictComponent} from "./components/historial-predict/historial-predict.component";

const routes: Routes = [
  { path: 'register', component: RegisterComponent},
  { path: 'login', component: LoginComponent},
  { path: 'profil', component: ProfilComponent},
  { path: 'admin', component: AdminComponent},
  { path: 'user', component : UserComponent},
  { path: 'historial', component : HistorialPredictComponent},
  { path: '', redirectTo: 'login' , pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
