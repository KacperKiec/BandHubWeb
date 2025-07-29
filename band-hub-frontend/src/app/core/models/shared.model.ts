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

export interface MapPoint {
  id: number;
  name: string;
  latitude: number;
  longitude: number;
}

export interface CoordinatesRanges {
  minLatitude: number;
  maxLatitude: number;
  minLongitude: number;
  maxLongitude: number;
}

export interface TimeRange {
  startTime: string;
  endTime: string;
}
