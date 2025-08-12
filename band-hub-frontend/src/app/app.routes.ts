import {Routes} from '@angular/router';
import {BandsComponent} from './bands/bands/bands.component';
import {MusiciansComponent} from './musicians/musicians/musicians.component';
import {HomeComponent} from './start/home/home.component';
import {MusicianDetailsComponent} from './musicians/musician-details/musician-details.component';
import {BandDetailsComponent} from './bands/band-details/band-details.component';
import {LoginComponent} from './core/auth/login/login.component';
import {RegisterComponent} from './core/auth/register/register.component';
import {PasswordReminderComponent} from './core/auth/password-reminder/password-reminder.component';
import {ProfileComponent} from './user/profile/profile.component';
import {EditProfileComponent} from './user/edit/edit-profile/edit-profile.component';
import {UserBandsComponent} from './bands/user-bands/user-bands.component';
import {InvitationsComponent} from './invitations/invitations/invitations.component';
import {RentingPanelComponent} from './user/renting-panel/renting-panel.component';
import {OffersComponent} from './offers/offers/offers.component';
import {UserBookingsComponent} from './bookings/user-bookings/user-bookings.component';
import {UserEventsComponent} from './events/user-events/user-events.component';
import {EventsComponent} from './events/events/events.component';
import {ChatComponent} from './chat/chat/chat.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'home', component: HomeComponent },
  { path: 'bands', component: BandsComponent },
  { path: 'musicians', component: MusiciansComponent },
  { path: 'musicians/:id', component: MusicianDetailsComponent },
  { path: 'bands/:id', component: BandDetailsComponent },
  { path: 'auth/login', component: LoginComponent },
  { path: 'auth/register', component: RegisterComponent },
  { path: 'auth/remind', component: PasswordReminderComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'profile/edit', component: EditProfileComponent },
  { path: 'user/bands', component: UserBandsComponent },
  { path: 'user/renting', component: RentingPanelComponent },
  { path: 'invitations', component: InvitationsComponent },
  { path: 'offers', component: OffersComponent },
  { path: 'user/bookings', component: UserBookingsComponent},
  { path: 'user/events', component: UserEventsComponent },
  { path: 'events', component: EventsComponent },
  { path: 'chat', component: ChatComponent }
]
