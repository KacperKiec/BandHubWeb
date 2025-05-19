import {Instrument} from './instrument.model';
import {Genre} from './genre.model';

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
  genres: Genre[];
  instruments: Instrument[];
  image?: string;
}

export interface MusicianRequest {
  id: number,
  bio: string,
  isLookingBand: boolean,
  genres: Genre[],
  instruments: Instrument[]
}
