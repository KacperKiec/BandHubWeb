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
