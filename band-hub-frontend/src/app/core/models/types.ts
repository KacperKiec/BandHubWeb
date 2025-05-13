import {GenreEnum} from './genre.model';
import {InstrumentEnum} from './instrument.model';

export interface filterElement {
  name: string,
  value: string,
}

export interface AccountRequest {
  firstName: string,
  lastName: string,
  birthDate: string,
  gender: boolean,
  email: string,
  password: string,
  city: string,
  country: string,
  roles: [{name: string}],
}

export interface AccountResponse {
  id: number,
  firstName: string,
  lastName: string,
  birthDate: Date,
  gender: boolean
  city: string,
  country: string
  image?: string;
}

export interface Musician {
  id: number;
  firstName: string;
  lastName: string;
  birthday: Date;
  gender: string;
  city: string;
  country: string;
  bio: string;
  isLookingBand: boolean;
  genres: GenreEnum[];
  instruments: InstrumentEnum[];
  image?: string;
}

export interface MusicianRequest {
  id: number,
  bio: string,
  isLookingBand: boolean,
  genres: Genre[],
  instruments: Instrument[]
}

export interface Band {
  id: number;
  name: string;
  city: string;
  bio: string;
  isLookingMembers: boolean;
  musicians: Musician[];
  genres: GenreEnum[];
  founderId: number;
  image?: string;
}

export interface SearchDisplayItem {
  id: number;
  name: string;
  city: string;
  genres: GenreEnum[];
  isLookingFor: boolean;
  image: string;
}

export type ErrorResponse = {
  errorCode: string;
  message: string;
}

export interface Page<T> {
  content: T[];
  page: PageInfo
}

export type PageInfo = {
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}

export interface Address {
  street: string;
  number: string;
  city: string;
  postalCode: string;
  country: string;
  latitude: number;
  longitude: number;
}

export interface Genre {
  id: number,
  name: string
}

export interface Instrument{
  id: number,
  name: string
}
