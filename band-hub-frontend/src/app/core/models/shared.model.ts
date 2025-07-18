import {Genre} from './genre.model';

export interface filterElement {
  name: string,
  value: string,
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

export interface CarouselElement {
  id: number;
  name: string;
}
