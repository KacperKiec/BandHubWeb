import {Genre} from './genre.model';
import {Musician} from './musician.model';

export interface Band {
  id: number;
  name: string;
  city: string;
  country: string;
  bio: string;
  isLookingMembers: boolean;
  musicians: Musician[];
  genres: Genre[];
  founderId: number;
  image?: string;
}

export interface BandRequest {
  name: string;
  city: string;
  country: string;
  bio: string;
  isLookingMembers: boolean;
  genres: Genre[];
  founderId: number;
}

export interface BandUpdateRequest {
  id: number,
  name: string;
  city: string;
  country: string;
  bio: string;
  isLookingMembers: boolean;
  genres: Genre[];
  founderId: number;
}
