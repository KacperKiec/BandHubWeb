import {Genre} from './genre.model';
import {Instrument} from './instrument.model';

export interface filterElement {
  name: string,
  value: string,
}

export interface Account {
  firstName: string,
  lastName: string,
  birthday: Date,
  gender: boolean,
  email: string,
  password: string,
  city: string,
  country: string
}

export interface Musician {
  id: number;
  firstName: string;
  lastName: string;
  birthday: Date;
  gender: string;
  city: string;
  bio: string;
  isLookingBand: boolean;
  genres: Genre[];
  instruments: Instrument[];
  image?: string;
}

export interface Band {
  id: number;
  name: string;
  city: string;
  bio: string;
  isLookingMembers: boolean;
  musicians: Musician[];
  genres: Genre[];
  founderId: number;
  image?: string;
}

export interface SearchDisplayItem {
  id: number;
  name: string;
  city: string;
  genres: Genre[];
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
