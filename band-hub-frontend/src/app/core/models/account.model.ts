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
